package au.com.xandar.swimclub.awards.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import au.com.xandar.swimclub.awards.Athlete;
import au.com.xandar.swimclub.awards.AthletePointsComparator;

/**
 * Calculates the totals for a Collection of AthletePoints.
 * 
 * @author william
 */
public final class AthletePointsCalculator {

	/**
	 * Calculates the totals for a Collection of AthletePoints and returns a sorted List.
	 */
	public List<Athlete> calculateTotals(Collection<Athlete> athletes, Integer nrOfBestNights) {
		
		for (final Athlete athletePoint : athletes) {
			athletePoint.calculateTotals(nrOfBestNights);
		}
		
		// Get sorted Athletes
		final List<Athlete> sortedAthletes = new ArrayList<Athlete>();
		sortedAthletes.addAll(athletes);
		Collections.sort(sortedAthletes, new AthletePointsComparator()); // Need to sort on total points desc (but only include best N-3) points.

		return sortedAthletes;
	}
}
