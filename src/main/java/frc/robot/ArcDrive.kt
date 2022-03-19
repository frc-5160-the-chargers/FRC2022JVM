package frc.robot

/**
 * A class representing a drive along an arc-shaped path.
 * @property radius the radius of the arc to drive in
 * @property thetaDeg the angular distance to drive for, in degrees
 * @property maxVelocity the maximum allowed velocity during the drive
 * @property wheelbase the distance between the centers of the front and back wheels of the robot
 */
class ArcDrive(val radius: Double, val thetaDeg: Double, val maxVelocity: Double, val wheelbase: Double) {
    val isValid: Boolean = radius > 0 && wheelbase / 2 < radius

    val l_l: Double // Who knows what these do
    val l_r: Double
    val v_l: Double
    val v_r: Double
    val t: Double
    val dTheta_dT: Double

    init {
        val thetaRad = Math.toRadians(thetaDeg)
        if (thetaRad < 0) {
            //Turn left
            l_l = (radius + wheelbase / 2) * thetaRad // John wrote all of this, idek
            l_r = (radius - wheelbase / 2) * thetaRad
            v_l = maxVelocity
            v_r = (radius - wheelbase / 2) / (radius + wheelbase / 2) * maxVelocity
        } else {
            //Turn right
            l_l = (radius - wheelbase / 2) * thetaRad // ¯\_(ツ)_/¯
            l_r = (radius + wheelbase / 2) * thetaRad
            v_l = (radius - wheelbase / 2) / (radius + wheelbase / 2) * maxVelocity
            v_r = maxVelocity
        }
        t = l_l / v_l // Can't help you here
        dTheta_dT = thetaRad / t
    }
}
