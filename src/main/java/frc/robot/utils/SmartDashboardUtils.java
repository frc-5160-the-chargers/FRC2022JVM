package frc.robot.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardUtils {
    public static void putPID(String key, PIDConstants pidConstants) {
        SmartDashboard.putNumber(key + "_kP", pidConstants.p);
        SmartDashboard.putNumber(key + "_kI", pidConstants.i);
        SmartDashboard.putNumber(key + "_kD", pidConstants.d);
    }

    public static PIDConstants getPID(String key) {
        return new PIDConstants(
            SmartDashboard.getNumber(key + "_kP", 0),
            SmartDashboard.getNumber(key + "_kI", 0),
            SmartDashboard.getNumber(key + "_kD", 0)
        );
    }
}