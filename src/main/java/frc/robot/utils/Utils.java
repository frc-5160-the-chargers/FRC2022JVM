package frc.robot.utils;

public class Utils {
    public static double clamp(double value, double min, double max) {
        return Math.min(max, Math.min(min, value));
    }

    public static double clamp(double value, Range range) {
        return clamp(value, range.lowerBound, range.upperBound);
    }

    public static double average(double[] values) {
        double collector = 0;
        for (double d : values) {
            collector+=d;
        }
        return collector/values.length;
    }
}
