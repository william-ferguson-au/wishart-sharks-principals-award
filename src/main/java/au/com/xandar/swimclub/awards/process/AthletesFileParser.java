package au.com.xandar.swimclub.awards.process;

import au.com.xandar.parsing.CSVLineParser;
import au.com.xandar.swimclub.awards.Athlete;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazyMap;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses a file containing a all the Athletes and compiles a List of Athletes with their ages.
 *
 * @author william
 */
public final class AthletesFileParser {

    private static final int NAME_OFFSET = 18;
    private static final int GENDER_OFFSET = 19;
    private static final int AGE_OFFSET = 20;
    private static final int DOB_OFFSET = 21;

	/**
	 * Transforms an Athlete into an {@link au.com.xandar.swimclub.awards.Athlete} object.
	 */
	static final class AthleteTransformer implements Transformer {
		public Object transform(Object input) {
			final String name = (String) input;
			return new Athlete(name);
		}
	}

	private static final Logger LOGGER = Logger.getLogger(AthletesFileParser.class);

	@SuppressWarnings("unchecked")
	private final Map<String, Athlete> map = LazyMap.decorate(new HashMap<String, Athlete>(), new AthleteTransformer());

	/**
	 * Returns the Map of name->Athlete parsed from the file.
	 */
	public Map<String, Athlete> getAthletes() {
        // Copy into a new Map so that the Map we pass back is static and doesn't create new Athletes on the fly if an unknown name is supplied to #get. 
        final Map<String, Athlete> athletes = new HashMap<String, Athlete>();
        athletes.putAll(this.map);
		return athletes;
	}

	/**
	 * Parses the Athlete's file.
	 */
	public void parseAthletes(File athleteFile) throws IOException {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(athleteFile)));
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

                // Name has changed in TM6 to be "FirstName LastName" instead of "LastName, FirstName" but MM4 exports have remained the same.
				final String name = tokens.get(NAME_OFFSET).toUpperCase();
                final int breakingSpace = name.indexOf(" ");
                final String firstName = name.substring(0, breakingSpace);
                final String lastName = name.substring(breakingSpace + 1);
                final String fullName = lastName + ", " + firstName;

                final String gender = tokens.get(GENDER_OFFSET);
				final String age = tokens.get(AGE_OFFSET);
                final String dob = tokens.get(DOB_OFFSET);

				if (LOGGER.isInfoEnabled()) LOGGER.info("AthleteName:'" + fullName + "' age:'" + age + "' gender:'" + gender + "' dob:'" + dob +"'");

				final Athlete athlete = this.map.get(fullName); // Will populate the Map entry with an Athlete->AthletePoints
				athlete.setAge(new Integer(age.trim()));
			}
		} finally {
			reader.close();
		}

        if (LOGGER.isInfoEnabled()) LOGGER.info(""); // Just as a spacer before any PointsReportParser errors.
	}
}