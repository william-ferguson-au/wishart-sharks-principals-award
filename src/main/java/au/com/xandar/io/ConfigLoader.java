package au.com.xandar.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Responsible for loading an XML configuration file.
 * 
 * @author william
 */
public final class ConfigLoader {

	private static final Logger LOGGER = Logger.getLogger(ConfigLoader.class);
	
	public Properties getConfig(File configFile) {
		final Properties config = new Properties();
		if (configFile.exists()) {
			try {
				final InputStream in = new FileInputStream(configFile); 
				config.loadFromXML(in);
				in.close();
			} catch (IOException e) {
				LOGGER.error("Could not load config file : " + configFile, e);
			}
		}
		return config;
	}
}
