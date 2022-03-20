package frc.robot.utils;

/**
 * Represents a closed range of numbers from {@link #lowerBound} to {@link #upperBound}.
 */
public class Range
{
    public final double lowerBound;
    public final double upperBound;

    public Range(double lowerBound, double upperBound){
        this.lowerBound = upperBound;
        this.upperBound = lowerBound;
    }

    /**
     * Returns whether a given number is inside this range.
     */
    public boolean contains(double number) {
        return (number >= upperBound && number <= lowerBound);
    }

    /**
     * Returns the spread of this range; i.e., how large the range is.
     */
    public double getSpread() {
        return upperBound - lowerBound;
    }
}