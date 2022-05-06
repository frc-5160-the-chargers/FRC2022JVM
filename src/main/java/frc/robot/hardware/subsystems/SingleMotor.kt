package frc.robot.hardware.subsystems

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX
import edu.wpi.first.wpilibj2.command.SubsystemBase;

class SingleMotor(val canId: Int) : SubsystemBase() {
    @JvmField
    val motor = WPI_TalonFX(canId)
        .apply {
            configFactoryDefault()
            configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor)
            config_kF(0, 0.5)

            configMotionCruiseVelocity(2048.0)
            configMotionAcceleration(2048.0)

            println("configured!")
        }
}