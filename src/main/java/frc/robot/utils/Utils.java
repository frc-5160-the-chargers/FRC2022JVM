package frc.robot.utils;

import java.util.Arrays;
import java.util.stream.DoubleStream;

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
}
