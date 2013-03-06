package au.com.xandar.swimclub.awards.process;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import au.com.xandar.swimclub.awards.Athlete;
import au.com.xandar.swimclub.awards.AwardsConfig;
import au.com.xandar.swimclub.awards.load.LoadAthletesAction;

/**
 * Produces the ELigibilityReport for a List of Athletes.
 * 
 * @author william
 */
public final class ProduceAwardsReportAction implements ActionListener {
	
	private static final Logger LOGGER = Logger.getLogger(ProduceAwardsReportAction.class);
	
	private final LoadAthletesAction loadAthletes; // TODO this should really be an interface that returns a File.
    private final DefaultListModel meetList;
    private final List<File> meetFiles = new ArrayList<File>();
	private final JTextField nrBestNightsField;
	private final AwardsConfig config;
    private final JFileChooser chooser = new JFileChooser();
	
	public ProduceAwardsReportAction(LoadAthletesAction loadAthletes, DefaultListModel meetList, JTextField nrBestNightsField, AwardsConfig config) {
		this.loadAthletes = loadAthletes;
        this.meetList = meetList;
		this.nrBestNightsField = nrBestNightsField;
		this.config = config;
		this.chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		this.chooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
	}
	
	public void actionPerformed(ActionEvent event) {

        // Get the Meet files that were chosen.
        for (int i = 0; i < meetList.getSize(); i++) {
            final File file = (File) meetList.get(i);
            meetFiles.add(file);
        }

		// Select outputFile
		this.chooser.setCurrentDirectory(this.config.getAwardResultsFolder());
	    final int returnVal = this.chooser.showSaveDialog((Component) event.getSource());
	    if (returnVal != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }
		
	    // Confirm its ok to overwrite outputFile (if it already exists)
		final File outputFile = this.chooser.getSelectedFile();
		if (outputFile.exists()) {
			final int confirmationResult = JOptionPane.showConfirmDialog(
					(Component) event.getSource(), 
					new String[] {"Results file already exists : " + outputFile, "Overwrite?"}, 
					"Output file exists",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (confirmationResult == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		this.config.setAwardResultsFolder(this.chooser.getCurrentDirectory());

		// Parse the Athletes File and get a Collection of Athletes.
        final AthletesFileParser athletesParser = new AthletesFileParser();
		try {
			athletesParser.parseAthletes(this.loadAthletes.getFile());
		} catch (IOException e) {
			LOGGER.error("Could not parse AthleteFile: " + this.loadAthletes.getFile(), e);
		}
		
		// Parse all Meet Files and get a Collection of Athletes and their points for each meet.
        final PointsReportParser pointsReportParser = new PointsReportParser(athletesParser.getAthletes());
		for (final File file : meetFiles) {
			try {
				pointsReportParser.parseMeet(file);
			} catch (IOException e) {
				LOGGER.error("Could not parse MeetFile: " + file, e);
			}
		}
		
		// Open outputFile ready for writing.
		final PrintStream outStream;
		try {
			outStream = new PrintStream(new FileOutputStream(outputFile));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					(Component) event.getSource(), 
					new String[] {"Could not write Principal's Awards file", e.getMessage()}, 
					"Failure",
					JOptionPane.ERROR_MESSAGE);
			LOGGER.error("Could not write Principal's Awards file : " + outputFile, e);
			return;
		}

		// Run the Collection through a PointsCalculator first.
		final Integer nrBestNights = new Integer(this.nrBestNightsField.getText());
		final AthletePointsCalculator calculator = new AthletePointsCalculator();
		final List<Athlete> sortedAthletes = calculator.calculateTotals(pointsReportParser.getAthletes(), nrBestNights);

        // Outputs the Athletes ordered from highest points to lowest points in each of the age divisions. 
		final PrincipalsAwardReportGenerator formatter = new PrincipalsAwardReportGenerator(outStream);
		formatter.render(meetFiles, sortedAthletes);
		outStream.close();

        if (pointsReportParser.hasErrors()) {
            // Notify user of completion
            JOptionPane.showMessageDialog(
                    (Component) event.getSource(),
                    new String[] {"Some athletes in the meet reports could not be found in Team manager. Check the ClubChampions.log to find out who they were, rectify them in the Meet, re-export and try again."}, 
                    "ERRORS READING MEET REPORTS",
                    JOptionPane.OK_OPTION);
        } else {
            // Notify user of completion
            JOptionPane.showMessageDialog(
                    (Component) event.getSource(),
                    new String[] {"Processing is complete"},
                    "Processing complete",
                    JOptionPane.OK_OPTION);
        }
	}
	
	private File getAlphaOutputFile(File file) {
		final String fileName = file.getName();
		final String trimmedFileName = fileName.substring(0, fileName.indexOf("."));
		return new File(file.getParentFile(), trimmedFileName + "-alpha.txt");
	}
}
