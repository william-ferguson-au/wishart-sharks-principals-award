package au.com.xandar.swimclub.awards.process;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

import org.apache.log4j.Logger;

import au.com.xandar.swimclub.awards.Athlete;

/**
 * Responsible for writing the Atheletes for each age division from highest to lowest to a PrintStream.
 *  
 * @author william
 */
final class PrincipalsAwardReportGenerator {

	private static final Logger LOGGER = Logger.getLogger(PrincipalsAwardReportGenerator.class);
	
	private final PrintWriter writer;

    public PrincipalsAwardReportGenerator(PrintStream stream) {
		this.writer = new PrintWriter(stream);
	}

    public void render(Collection<File> meetFiles, Collection<Athlete> athletes) {

		// Collate the Meet Dates.
		final Set<Date> meetDates = new TreeSet<Date>();
		for (final Athlete athlete : athletes) {
			meetDates.addAll(athlete.getDates());
		}

		// Write Header info.
		this.writeMeetDates(meetFiles);

        // filter Athletes based on age division and print each division.
        for (Division division: Division.values()) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Determining Division: " + division);
            final List<Athlete> ageDivision = filterAthletes(athletes, division);
            final DivisionFormatter divisionFormatter = new DivisionFormatter(writer, division);
            divisionFormatter.render(meetDates, ageDivision);
        }
		LOGGER.info("EligibilityReport complete");
		
		this.writer.println();
		this.writer.flush();
	}

    /**
     * @return List of Athletes that belong to the supplied Division.
     */
    private List<Athlete> filterAthletes(Collection<Athlete> athletes, Division division) {
        final List<Athlete> divisionAthletes = new ArrayList<Athlete>();
        for (Athlete athlete : athletes) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Checking Athlete: " + athlete + " Age:" + athlete.getAge());
            if (division.suitsAthlete(athlete) && athlete.nrNights() > 0) {
                divisionAthletes.add(athlete);
            }
        }
        return divisionAthletes;
    }

    /**
	 * Write the prelude and header record.
	 */
	private void writeMeetDates(Collection<File> meetDates) {
		
		// Show Meet Dates.
		this.writer.println("Principal's Awards based on");
		int i = 1;
		for (final File file : meetDates) {
			this.writer.printf("  %2$2d  %1$s", file.toString(), i++);
            this.writer.println();
		}
		this.writer.println();
	}
}
