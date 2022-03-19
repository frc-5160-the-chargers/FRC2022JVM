package frc.robot.utils

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

/**
 * Displays a set of PID constants to the SmartDashboard.
 * @param key the SmartDashboard key to set
 * @param pidConstants the constants to display
 */
fun putPID(key: String, pidConstants: PIDConstants) { // TODO: Namespace extension function when supported
    SmartDashboard.putNumber(key + "_kP", pidConstants.kP)
    SmartDashboard.putNumber(key + "_kI", pidConstants.kI)
    SmartDashboard.putNumber(key + "_kD", pidConstants.kD)
}

/**
 * Gets a set of PID constants from the SmartDashboard
 * @param key the SmartDashboard key to set
 * @return the constants from the SmartDashboard
 */
fun getPID(key: String): PIDConstants =
    PIDConstants(
        SmartDashboard.getNumber(key + "_kP", 0.0),
        SmartDashboard.getNumber(key + "_kI", 0.0),
        SmartDashboard.getNumber(key + "_kD", 0.0)
    )