package frc.robot.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A utility class providing various extensions to the {@link SmartDashboard} class.
 */
public class SmartDashboardUtils {
    /**
     * Displays a set of PID constants to the SmartDashboard.
     * @param key the SmartDashboard key to set
     * @param pidConstants the constants to display
     */
    public static void putPID(String key, PIDConstants pidConstants) {
        SmartDashboard.putNumber(key + "_kP", pidConstants.kP);
        SmartDashboard.putNumber(key + "_kI", pidConstants.kI);
        SmartDashboard.putNumber(key + "_kD", pidConstants.kD);
    }

    /**
     * Gets a set of PID constants from the SmartDashboard
     * @param key the SmartDashboard key to set
     * @return the constants from the SmartDashboard
     */
    public static PIDConstants getPID(String key) {
        return new PIDConstants(
            SmartDashboard.getNumber(key + "_kP", 0),
            SmartDashboard.getNumber(key + "_kI", 0),
            SmartDashboard.getNumber(key + "_kD", 0)
        );
    }

    // Prevent construction of this class
    private SmartDashboardUtils() {}
}