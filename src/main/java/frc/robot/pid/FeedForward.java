package frc.robot.pid;

@FunctionalInterface
public interface FeedForward {
    double calculateFeedForward(final double target, final double error);
}
