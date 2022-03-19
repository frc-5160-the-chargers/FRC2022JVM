package frc.robot.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMax.IdleMode
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup
import edu.wpi.first.wpilibj2.command.SubsystemBase

class Powertrain : SubsystemBase() {
    val left1 = CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless)
    val left2 = CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless)
    val right1 = CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless)
    val right2 = CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless)
    val motors = listOf(left1, left2, right1, right2)
        .onEach(::configureSpark)
    val differentialDrive = DifferentialDrive(
        MotorControllerGroup(left1, left2),
        MotorControllerGroup(right1, right2)
    )

    private var power = 0.0
    private var rotation = 0.0
    private var leftPower = 0.0
    private var rightPower = 0.0

    var mode: Mode? = null

    init {
        reset()
    }

    private fun configureSpark(motor: CANSparkMax) {
        motor.enableVoltageCompensation(11.0)
        motor.setSmartCurrentLimit(39)
        motor.idleMode = IdleMode.kBrake
        motor.closedLoopRampRate = 1.0
        motor.inverted = true
        motor.burnFlash()
    }

    fun reset() {
        mode = Mode.CURVATURE_DRIVE
        power = 0.0
        rotation = 0.0
        leftPower = 0.0
        rightPower = 0.0
    }

    fun tankDrive(leftPower: Double, rightPower: Double) {
        mode = Mode.TANK_DRIVE
        setTankPowers(leftPower, rightPower)
    }

    private fun setTankPowers(leftPower: Double, rightPower: Double) {
        rotation = 0.0
        power = rotation
        this.leftPower = leftPower
        this.rightPower = rightPower
    }

    fun arcadeDrive(power: Double, rotation: Double) {
        mode = Mode.ARCADE_DRIVE
        setArcadePowers(power, rotation)
    }

    fun curvatureDrive(power: Double, rotation: Double) {
        mode = Mode.CURVATURE_DRIVE
        setArcadePowers(power, rotation)
    }

    private fun setArcadePowers(power: Double, rotation: Double) {
        rightPower = 0.0
        leftPower = rightPower
        this.power = power
        this.rotation = rotation
    }

    override fun periodic() {
        when (mode) {
            Mode.TANK_DRIVE -> {
                differentialDrive.tankDrive(leftPower, rightPower, false)
            }
            Mode.ARCADE_DRIVE -> {
                differentialDrive.arcadeDrive(power, rotation, false)
            }
            Mode.CURVATURE_DRIVE -> {
                differentialDrive.curvatureDrive(power, rotation, true)
            }
            null -> {}
        }
    }

    enum class Mode {
        TANK_DRIVE,
        CURVATURE_DRIVE,
        ARCADE_DRIVE;
    }
}