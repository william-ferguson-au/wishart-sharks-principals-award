package au.com.xandar.swimclub.awards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Athlete implements Comparable<Athlete> {

	private final String name;
	private Integer age;
	
	private final Map<Date, Integer> meetPoints = new HashMap<Date, Integer>();
	private Integer totalPoints;
	private Integer bestNightsTotalPoints;
	
	public Athlete(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public Integer getAge() {
		return this.age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Integer getPoints(Date meetDate) {
		return this.meetPoints.get(meetDate);
	}
	
	public void setPoints(Date meetDate, Integer points) {
		this.meetPoints.put(meetDate, points);
	}
	
	public Collection<Date> getDates() {
		return this.meetPoints.keySet();
	}

    public int nrNights() {
        return meetPoints.size(); 
    }

	public Integer getTotal() {
		return this.totalPoints;
	}
	
	public Integer getBestNightsTotal() {
		return this.bestNightsTotalPoints;
	}
	
	public void calculateTotals(Integer nrOfBestNights) {
		final List<Integer> points = new ArrayList<Integer>();
		points.addAll(this.meetPoints.values());
		Collections.sort(points, Collections.reverseOrder());
		
		int total = 0;
		int bestNightsTotal = 0;
		int i = 0;
		for (Integer meetPoint : points) {
			total += meetPoint.intValue();
			if (i < nrOfBestNights.intValue()) {
				bestNightsTotal += meetPoint.intValue();
			}
			i++;
		}
		this.totalPoints = new Integer(total);
		this.bestNightsTotalPoints = new Integer(bestNightsTotal);
	}
	
	
	/**
	 * Natural Comparison is by Athlete.
	 */
    public int compareTo(Athlete o) {
    	return this.name.compareTo(o.name);
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o == null) return false;
    	final Athlete other = (Athlete) o;
    	return this.name.equals(other.name);
    }
    
    @Override
    public int hashCode() {
    	return this.name.hashCode();
    }
    
    @Override
    public String toString() {
    	return this.name.toString();
    }
}
