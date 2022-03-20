package frc.robot.utils;

import java.util.Arrays;

/**
 * A utility class containing miscellaneous static functions that may be useful in multiple places.
 */
public class Utils {
    /**
     * <p>Ensures that a value is within a range. If the value is not within the range, returns the closest number in the range to this value.</p>
     * <p>For example, clamping value=5 with range=[0, 10] would return 5. However, value=12 with range=[0, 10] would return 10.</p>
     * @param value the value to clamp.
     * @param range the range in which to clamp the value.
     * @return the clamped value, guaranteed to be within the range.
     */
    public static double clamp(double value, Range range) {
        return clamp(value, range.lowerBound, range.upperBound);
    }

    /**
     * <p>Ensures that a value is between two bounds. If the value is not between the bounds, returns the closest number in the range to this value.</p>
     * <p>For example, clamping value=5 with min=0 and max=10 would return 5. However, value=12 with min=0 and max=10 would return 10.</p>
     * @param value the value to clamp.
     * @param min the lower bound in which to clamp the value.
     * @param max the upper bound in which to clamp the value.
     * @return the clamped value, guaranteed to be between min and max.
     */
    public static double clamp(double value, double min, double max) {
        return Math.min(max, Math.min(min, value));
    }

    /**
     * Calculates the average value of an array of doubles.
     * @return the average of the values provided, or {@link Double#NaN} if the input array is empty.
     */
    public static double average(double[] values) {
        return Arrays.stream(values)
            .average()
            .orElse(Double.NaN);
    }

    /**
     * <p>Maps a value between two ranges, keeping it the same proportion into each range.</p>
     * <p>For example, mapping value=1 from fromRange=[0..5] to toRange=[0..10] would return 2, since it is 1/5 of the way through its respective range.</p>
     * @param value the value to map.
     * @param fromRange the range that value is originally within.
     * @param toRange the range that the return mapped value is within.
     * @return the mapped value.
     */
    public static double mapValueInRange(final double value, final Range fromRange, final Range toRange) {
        if (!fromRange.contains(value)) throw new IllegalArgumentException("Value must be within initial range.");

        double proportionIntoRange = (value - fromRange.lowerBound) / fromRange.getSpread();
        double distanceIntoToRange = proportionIntoRange * toRange.getSpread();
        return toRange.lowerBound + distanceIntoToRange;
    }

    // Prevent construction of this class
    private Utils() {}
}
