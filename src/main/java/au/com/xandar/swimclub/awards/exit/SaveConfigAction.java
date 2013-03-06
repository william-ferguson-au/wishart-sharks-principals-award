package au.com.xandar.swimclub.awards.exit;

import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Saves the configuration. 
 *  
 * @author william
 */
public final class SaveConfigAction implements ActionListener {
	
	private static final Logger LOGGER = Logger.getLogger(SaveConfigAction.class);
	
	private final File configFile;
    private final Properties config;
	
	public SaveConfigAction(File configFile, Properties config) {
		this.configFile = configFile;
		this.config = config;
	}
	
	public void actionPerformed(ActionEvent event) {

		try {
			final OutputStream out = new FileOutputStream(this.configFile);
			this.config.storeToXML(out, "Config saved on " + new Date());
			out.close();
		} catch (IOException e) {
			LOGGER.error("Could not save config to " + this.configFile, e);
		}
	}
}
