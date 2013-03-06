package au.com.xandar.swimclub.awards;

import java.util.Comparator;

/**
 * Compares AthletesPoints with the Athlete with the higher best nights points coming first.
 * 
 * @author william
 */
public final class AthletePointsComparator implements Comparator<Athlete> {

	@Override
	public int compare(Athlete o1, Athlete o2) {
    	if (!o1.getBestNightsTotal().equals(o2.getBestNightsTotal())) return -o1.getBestNightsTotal().compareTo(o2.getBestNightsTotal());
    	return o1.getName().compareTo(o2.getName());
	}

}
