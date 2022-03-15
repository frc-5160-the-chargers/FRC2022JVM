package frc.robot.utils;

import edu.wpi.first.math.controller.PIDController;

public class PIDConstants {
    double p;
    double i;
    double d;

    public PIDConstants(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
    }

    public static void updateController(PIDController controller, PIDConstants pidConstants) {
        controller.setPID(pidConstants.p, pidConstants.i, pidConstants.d);
    }
}
