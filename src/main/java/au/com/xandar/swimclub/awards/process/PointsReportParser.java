package au.com.xandar.swimclub.awards.process;

import au.com.xandar.parsing.CSVLineParser;
import au.com.xandar.swimclub.awards.Athlete;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Parses a Meet Points Report and accumulates the points for each Athlete.
 * 
 * @author william
 */
public final class PointsReportParser {

	private static final Logger LOGGER = Logger.getLogger(PointsReportParser.class);
	
	private final Map<String, Athlete> map;
    private boolean hasErrors = false;

    public PointsReportParser(Map<String, Athlete> athletes) {
        this.map = athletes;
    }

	/**
	 * @return the Collection of Athletes with their points parsed from all files.
	 */
	public Collection<Athlete> getAthletes() {
		return this.map.values();
	}

    /**
     * @return true if there were problems parsing the file such as some athletes not being found.
     */
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
	 * Parses a meet file and accumulates the meet points for each athlete.
     *
     * @param meetFile  File to parse.
     * @throws IOException if the file could not be read.
	 */
	public void parseMeet(File meetFile) throws IOException {
		
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Parsing points for meet: " + meetFile);

		// This needs to correspond to the first part of the filename.
		final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(meetFile)));
		try {
			final CSVLineParser lineParser = new CSVLineParser();
			while (true) {
				
				final String line = reader.readLine();
				if (LOGGER.isDebugEnabled()) LOGGER.debug("Read line :" + line);
				if (line == null) {
					break; // EOF so stop
				}
				
				// Use LineParser to tokenize CSV line into ArrayList of elements (or Map of useful elements).
				final List<String> tokens = lineParser.getParsedContents(line);
				
				if (LOGGER.isDebugEnabled()) {
					int i = 0;
					for (final String token : tokens) {
						LOGGER.debug(i + " : " + token);
						i++;
					}
				}
				
				final String meetString = tokens.get(2);
				final String meetDateString = meetString.substring(18);
				
				final String name = tokens.get(13).toUpperCase();
				final String points = tokens.get(16);

				if (LOGGER.isDebugEnabled()) LOGGER.debug("AthleteName:" + name + " meet:'" + meetDateString + " points:" + points);
				
				final Date meetDate;
				try {
					meetDate = dateFormat.parse(meetDateString);
				} catch (ParseException e) {
					LOGGER.error("Could not parse MeetDate from MeetDate Filename : '" + meetDateString + "'", e);
					return;
				}
				
				final Athlete athlete = this.map.get(name);
                if (athlete == null) {
                    LOGGER.error("Could not find matching athlete for : '" + name + "' in meet: " + meetDateString);
                    hasErrors = true;
                } else {
                    athlete.setPoints(meetDate, new Integer(points));
                }
			}
		} finally {
			reader.close();
		}
	}
}
