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
 * Loads the file containing all the athletes. 
 *  
 * @author william
 */
public final class LoadAthletesAction implements ActionListener {
	
	private static final Logger LOGGER = Logger.getLogger(LoadAthletesAction.class);
	
	private final AwardsConfig config;
	private final JButton loadMeetsButton;
    private final JFileChooser chooser = new JFileChooser();
    private File athletesFile;
	
	public LoadAthletesAction(AwardsConfig config, JButton loadMeetsButton) {
		this.config = config;
		this.loadMeetsButton = loadMeetsButton;
	}
	
	public void actionPerformed(ActionEvent event) {

		this.chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		this.chooser.setCurrentDirectory(this.config.getLoadFolder());
		this.chooser.setFileFilter(new FileNameExtensionFilter("Comma Separated Files", "csv"));
	    final int returnVal = this.chooser.showOpenDialog((Component) event.getSource());
	    if (returnVal != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }

	    this.athletesFile = this.chooser.getSelectedFile();
	    this.config.setLoadFolder(this.chooser.getCurrentDirectory());
	    
	    // Make the next button available and stop reuse of this one.
	    this.loadMeetsButton.setEnabled(true);
	    final JButton thisButton = (JButton) event.getSource();
	    thisButton.setEnabled(false);
	    
		LOGGER.info("Load complete");
	}
	
	/**
	 * Returns the selected file.
	 */
	public File getFile() {
		return this.athletesFile;
	}
}
