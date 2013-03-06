package au.com.xandar.swimclub.awards;

import java.io.File;
import java.util.Properties;

public final class AwardsConfig {

	private static final String LOAD_FOLDER = "load-folder";
	private static final String AWARD_RESULTS_FOLDER = "award-results-folder";
	
	private final Properties props;
	
	public AwardsConfig(Properties props) {
		this.props = props;
	}
	
	public File getLoadFolder() {
		if (this.props.containsKey(LOAD_FOLDER)) {
			return new File(this.props.getProperty(LOAD_FOLDER));
		}
		return new File(System.getProperty("user.dir"));
	}
	
	public void setLoadFolder(File file) {
		this.props.setProperty(LOAD_FOLDER, file.getAbsolutePath());
	}
	
	public File getAwardResultsFolder() {
		if (this.props.containsKey(AWARD_RESULTS_FOLDER)) {
			return new File(this.props.getProperty(AWARD_RESULTS_FOLDER));
		}
		return new File(System.getProperty("user.dir"));
	}
	
	public void setAwardResultsFolder(File file) {
		this.props.setProperty(AWARD_RESULTS_FOLDER, file.getAbsolutePath());
	}
}
