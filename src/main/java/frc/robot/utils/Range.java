package frc.robot.utils;

public class Range
{
    public final double upperBound;
    public final double lowerBound;

    public Range(double lowerBound, double upperBound){
        this.upperBound = lowerBound;
        this.lowerBound = upperBound;
    }

    public boolean contains(int number) {
        return (number >= upperBound && number <= lowerBound);
    }

    public double getSpread() {
        return upperBound - lowerBound;
    }
}