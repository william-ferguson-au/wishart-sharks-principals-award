package au.com.xandar.swimclub.awards.process;

import au.com.xandar.swimclub.awards.Athlete;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Responsible for writing the Athletes for an age division from highest to lowest to a PrintStream.
 */
final class DivisionFormatter {

    private static final Logger LOGGER = Logger.getLogger(DivisionFormatter.class);

    private final PrintWriter writer;
    private final Division division;
    private int counter = 1;

    public DivisionFormatter(PrintWriter writer, Division division) {
        this.writer = writer;
        this.division = division;
    }

    /**
     * Renders the Athletes for the Division.
     */
    public void render(Collection<Date> meetDates, List<Athlete> athletes) {
        this.writeDivisionHeader(division, meetDates.size(), athletes.size());
        if (LOGGER.isInfoEnabled()) LOGGER.info("Division: " + division + "  Nr Athletes:" + athletes.size());

        for (final Athlete athlete : athletes) {
            renderAthlete(meetDates, athlete);
        }
    }

    /**
     * Write the header for each age division of Athletes.
     */
    private void writeDivisionHeader(Division division, int nrMeetDates, int nrAthletes) {

        this.writer.println();
        this.writer.println("Age Division: " + division + "   (" + nrAthletes + " athletes)");
        this.writer.printf("    Athlete                      ");
        for (int i = 1; i <= nrMeetDates; i++) {
            this.writer.printf("%1$2d ", new Integer(i));
        }
        this.writer.println("Total  Best");
    }

    /**
     * Write the Athlete name, all meet points and total for an Athlete.
     */
    private void renderAthlete(Collection<Date> meetDates, Athlete athlete) {

        writer.printf("%3$3d %1$-23s (%2$2d) ", athlete.getName(), athlete.getAge(), new Integer(counter++));

        if (LOGGER.isDebugEnabled()) LOGGER.debug("Athlete: " + athlete);

        for (final Date meetDate : meetDates) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("   " + meetDate);
            final Integer points = athlete.getPoints(meetDate);
            if (points == null) {
                writer.printf("   ");
            } else {
                writer.printf("%1$2d ", points);
            }
        }
        writer.printf("  %1$3d   %2$3d", athlete.getTotal(), athlete.getBestNightsTotal());
        writer.println();
    }
}