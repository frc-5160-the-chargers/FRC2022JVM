package frc.robot;


/**
 * A class representing a drive along an arc-shaped path.
 */
public class ArcDrive {
    public boolean isValid;
    public final double radius;
    public final double theta;
    public final double maxVelocity;

    public final double l_l; // Who knows what these do
    public final double l_r;
    public final double v_l;
    public final double v_r;
    public final double t;
    public final double dTheta_dT;

    /**
     * Creates a new ArcDrive object.
     * @param radius the radius of the arc to drive in
     * @param thetaDeg the angular distance to drive for, in degrees
     * @param maxVelocity the maximum allowed velocity during the drive
     * @param wheelbase the distance between the centers of the front and back wheels of the robot
     */
    ArcDrive(final double radius, final double thetaDeg, final double maxVelocity, final double wheelbase) {
        isValid = radius > 0 && wheelbase/2 < radius;

        this.radius = radius;
        this.theta = thetaDeg;
        this.maxVelocity = maxVelocity;

        double thetaRad = Math.toRadians(thetaDeg);

        if (thetaRad < 0) {
            //Turn left
            l_l = (radius + wheelbase / 2) * thetaRad; // John wrote all of this, idek
            l_r = (radius - wheelbase / 2) * thetaRad;

            v_l = maxVelocity;
            v_r = ((radius - wheelbase / 2) / (radius + wheelbase / 2)) * maxVelocity;
        } else {
            //Turn right
            l_l = (radius - wheelbase / 2) * thetaRad; // ¯\_(ツ)_/¯
            l_r = (radius + wheelbase / 2) * thetaRad;

            v_l = ((radius - wheelbase / 2) / (radius + wheelbase / 2)) * maxVelocity;
            v_r = maxVelocity;
        }

        t = l_l / v_l; // Can't help you here

        dTheta_dT = thetaRad / t;
    }
}
