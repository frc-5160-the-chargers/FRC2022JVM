package frc.robot.utils;

import java.util.Arrays;

public class Utils {
    public static double clamp(double value, double min, double max) {
        return Math.min(max, Math.min(min, value));
    }

    public static double clamp(double value, Range range) {
        return clamp(value, range.lowerBound, range.upperBound);
    }

    public static double average(double[] values) {
        return Arrays.stream(values)
            .average()
            .orElse(Double.NaN);
    }

    public static double mapValueInRange(final double value, final Range fromRange, final Range toRange) {
        double clampedValue = clamp(value, fromRange);
        double proportionIntoRange = (clampedValue - fromRange.lowerBound) / fromRange.getSpread();
        double distanceIntoToRange = proportionIntoRange * toRange.getSpread();
        return toRange.lowerBound + distanceIntoToRange;
    }
}
