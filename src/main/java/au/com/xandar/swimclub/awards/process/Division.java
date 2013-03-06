package au.com.xandar.swimclub.awards.process;

import au.com.xandar.swimclub.awards.Athlete;

/**
 * Represents an age division for grouping Athletes of a similar age.
 * <p/>
 * User: William
 * Date: 21/03/2010
 * Time: 12:32:58 AM
 */
public enum Division {

    SUB_JUNIOR (null, 7, "Sub Junior Division"),
    JUNIOR (8, 9, "Junior Division"),
    INTERMEDIATE (10, 11, "Intermediate Division"),
    SUB_SENIOR (12, 13, "Sub Senior Division"),
    SENIOR (14, null, "Senior Division")
    ;

    private final Integer lowerAge;
    private final Integer upperAge;
    private final String name;

    /**
     * @param lowerAge  Null if there is no lower age limit.
     * @param upperAge  Null if there is no upper age limit.
     * @param name      Name by which the division is known. 
     */
    Division(Integer lowerAge, Integer upperAge, String name) {
        this.lowerAge = lowerAge;
        this.upperAge = upperAge;
        this.name = name;
    }

    /**
     * @param athlete   Athlete to check.
     * @return true if the Athlete lies exactly within the division age bounds.
     */
    public boolean suitsAthlete(Athlete athlete) {
        return athlete.getAge() >= getLowerAge() && athlete.getAge() <= getUpperAge();
    }

    private Integer getLowerAge() {
        return lowerAge == null ? Integer.MIN_VALUE : lowerAge;
    }

    private Integer getUpperAge() {
        return upperAge == null ? Integer.MAX_VALUE : upperAge;
    }


    public String toString() {
        return name;
    }
}
