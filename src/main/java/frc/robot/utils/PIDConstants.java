package frc.robot.utils;

import edu.wpi.first.math.controller.PIDController;

/**
 * A data class representing the various constants needed to configure a PID controller.
 */
public class PIDConstants {
    /**
     * The constant that weights the proportional PID term.
     */
    public double kP;
    /**
     * The constant that weights the integral PID term.
     */
    public double kI;
    /**
     * The constant that weights the derivative PID term.
     */
    public double kD;

    public PIDConstants(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    /**
     * Applies the values of a {@link PIDConstants} object to an existing {@link PIDController}.
     */
    public static void updateController(PIDController controller, PIDConstants pidConstants) {
        controller.setPID(pidConstants.kP, pidConstants.kI, pidConstants.kD);
    }
}
