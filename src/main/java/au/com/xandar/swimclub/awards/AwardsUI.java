package au.com.xandar.swimclub.awards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import au.com.xandar.io.ConfigLoader;
import au.com.xandar.swimclub.awards.exit.SaveConfigAction;
import au.com.xandar.swimclub.awards.load.LoadAthletesAction;
import au.com.xandar.swimclub.awards.load.LoadMeetsAction;
import au.com.xandar.swimclub.awards.process.ProduceAwardsReportAction;
import au.com.xandar.ui.action.WindowCloser;

/**
 * Captures the location of the Points reports, where to generate the Principal's Award output 
 * and allows generation of the output.
 * 
 * @author william
 */
public final class AwardsUI extends JFrame {

	// load pane
	private final JButton loadAthletesButton = new JButton("Load Athletes");
	private final JButton loadMeetsButton = new JButton("Load Meets");
	private final DefaultListModel meetListModel = new DefaultListModel();
	private final JList meetList = new JList(this.meetListModel);

	// process pane
	private final JTextField nrNightsTextField = new JTextField("13"); 
	private final JButton processButton = new JButton("Awards Report");
	
	//private final List<Athlete> athletes = new ArrayList<Athlete>();

	public AwardsUI() {
		
		final File configFile = new File("awards-config.xml");
		final ConfigLoader loader = new ConfigLoader();
		final Properties config = loader.getConfig(configFile);
		final AwardsConfig awardsConfig = new AwardsConfig(config);
		
        this.setTitle("SwimClub Prinicpal's Award Processor");
        this.setForeground(Color.BLUE);
        
		final JPanel loadPane = new JPanel();
		loadPane.setLayout(new BorderLayout());
		loadPane.add(this.meetList, BorderLayout.CENTER);
		this.meetList.setPreferredSize(new Dimension(300, 400));
		this.meetList.setCellRenderer(new ListRenderer());
		
		// Left Pane (of Load Panel)
		final JPanel loadLeftPane = new JPanel();
		loadLeftPane.setLayout(new BoxLayout(loadLeftPane, BoxLayout.Y_AXIS));
		loadLeftPane.add(this.loadAthletesButton, BorderLayout.NORTH);
		loadLeftPane.add(this.loadMeetsButton, BorderLayout.CENTER);
		loadPane.add(loadLeftPane, BorderLayout.WEST);
		
		final JPanel processPane = new JPanel();
		processPane.setLayout(new BorderLayout());
		processPane.add(new JLabel("Nr of best nights (typically N-3)"), BorderLayout.WEST);
		processPane.add(this.nrNightsTextField, BorderLayout.CENTER);
		processPane.add(this.processButton, BorderLayout.EAST);
		
        this.setLayout(new BorderLayout());
        this.add(loadPane, BorderLayout.CENTER);
        this.add(processPane, BorderLayout.SOUTH);

        this.loadMeetsButton.setEnabled(false); // disabled until Athletes are loaded.
        this.processButton.setEnabled(false);
        
		final LoadAthletesAction loadAthletes = new LoadAthletesAction(awardsConfig, this.loadMeetsButton);
		this.loadAthletesButton.addActionListener(loadAthletes);
		this.loadMeetsButton.addActionListener(new LoadMeetsAction(this.meetListModel, awardsConfig, this.processButton));
		this.processButton.addActionListener(new ProduceAwardsReportAction(loadAthletes, this.meetListModel, this.nrNightsTextField, awardsConfig));
        this.addWindowListener(new WindowCloser(new SaveConfigAction(configFile, config)));
        
        this.pack();
	}
	
	public static void main(final String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final AwardsUI ui = new AwardsUI();
				ui.setVisible(true);
		    }
		});
	}
}
