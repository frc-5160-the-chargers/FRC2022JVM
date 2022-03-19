package frc.robot.subsystems

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj2.command.SubsystemBase

class NavX : SubsystemBase() {
    private val navx = AHRS()

    init {
        reset()
    }

    fun reset() {
        navx.reset()
    }

    //in degrees
    val heading: Double
        get() = navx.angle

    val isConnected: Boolean
        get() = navx.isConnected

    override fun periodic() {}
}