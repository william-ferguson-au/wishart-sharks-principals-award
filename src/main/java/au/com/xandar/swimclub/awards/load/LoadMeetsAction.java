package au.com.xandar.swimclub.awards.load;

import au.com.xandar.swimclub.awards.AwardsConfig;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Loads the file containing the swims for all athletes and collates the results. 
 *  
 * @author william
 */
public final class LoadMeetsAction implements ActionListener {
	
	private static final Logger LOGGER = Logger.getLogger(LoadMeetsAction.class);
	
	private final DefaultListModel meetFileList;
	private final AwardsConfig config;
    private final JFileChooser chooser = new JFileChooser();
    private final JButton awardsReportButton; 
	
	public LoadMeetsAction(DefaultListModel meetFileList, AwardsConfig config, JButton awardsReportButton) {
		this.meetFileList = meetFileList;
		this.config = config;
		this.awardsReportButton = awardsReportButton;
	}
	
	public void actionPerformed(ActionEvent event) {

		this.chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		this.chooser.setCurrentDirectory(this.config.getLoadFolder());
		this.chooser.setFileFilter(new FileNameExtensionFilter("Comma Separated Files", "csv"));
		this.chooser.setMultiSelectionEnabled(true);
	    final int returnVal = this.chooser.showOpenDialog((Component) event.getSource());
	    if (returnVal != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }

	    for (final File file : this.chooser.getSelectedFiles()) {
	    	if (!this.meetFileList.contains(file)) {
			    this.meetFileList.addElement(file);
	    	}
	    }
	    this.config.setLoadFolder(this.chooser.getCurrentDirectory());

	    // Make the next button available and stop reuse of this one.
	    this.awardsReportButton.setEnabled(true);
	    final JButton thisButton = (JButton) event.getSource();
	    thisButton.setEnabled(false);
	    
		LOGGER.info("Load complete");
	}
}
