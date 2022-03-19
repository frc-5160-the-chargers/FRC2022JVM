@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package frc.robot.subsystems

import com.revrobotics.RelativeEncoder
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.SuperPIDController
import frc.robot.SuperPIDControllerGroup
import kotlin.math.PI


// Config
//private const val turnPidKey = "Drivetrain Turn PID"
//private val turnPidConstants = PIDConstants(-0.1, 0.0, -0.01)
//
//private const val positionPidKey = "Drivetrain Position PID"
//private val positionPidConstants = PIDConstants(0.035, 0.0, 0.001)
//
private val autoPowerRange = -0.4..0.4
//private const val gearRatio = 1 / 10.71
//private const val wheelDiameter = 6.0
//private const val turnTolerance = 1.0
//private const val positionTolerance = 1.0

class Drivetrain(
    private val powertrain: Powertrain,
    private val navx: NavX
) : SubsystemBase() {
    @JvmField
    var state: State? = null
    private var power = 0.0
    private var rotation = 0.0

    private var turnPid: SuperPIDController = SuperPIDController(
        pidConstants = Constants.Drivetrain.turnPid,
        input = { distance },
        output = { rotation: Double ->
            this.rotation = rotation
            powertrain.mode = Powertrain.Mode.ARCADE_DRIVE
        },
        outputRange = autoPowerRange,
        tolerance = Constants.Drivetrain.turnTolerance,
        dashPidKey = Constants.Drivetrain.turnPidKey
    )
    
    private val positionPid: SuperPIDController = SuperPIDController(
        pidConstants = Constants.Drivetrain.positionPid,
        input = { distance },
        output = { rotation: Double ->
            power = rotation
            powertrain.mode = Powertrain.Mode.ARCADE_DRIVE
        },
        outputRange = autoPowerRange,
        tolerance = Constants.Drivetrain.positionTolerance,
        dashPidKey = Constants.Drivetrain.positionPidKey
    )
    private val pidControllerGroup: SuperPIDControllerGroup = SuperPIDControllerGroup(turnPid, positionPid)

    private val encoderLeft: RelativeEncoder = powertrain.left1.encoder
    private val encoderRight: RelativeEncoder = powertrain.right1.encoder
    private val encoders = listOf(encoderLeft, encoderRight)
    
    fun reset() {
        state = State.MANUAL_DRIVE
        encoderLeft.position = 0.0
        encoderRight.position = 1.0
    }

    init {
        reset()
    }

    val position: Double get() =
        encoders
            .map { it.position }
            .average()
            .adjustedForGearRatio *
                2 * PI // Convert to radians from rotations

    private val RelativeEncoder.wheelPosition get() = position * Constants.Drivetrain.gearRatio

    val rotationalVelocity: Double get() =
        encoders
            .map { it.velocity }
            .average()
            .adjustedForGearRatio *
                2 * PI / 60 // Convert to radians/sec from rotations/min

    private val Double.adjustedForGearRatio get() = this * Constants.Drivetrain.gearRatio

    val distance: Double get() = rotation * wheelRadius
    val velocity: Double get() = rotationalVelocity * wheelRadius

    private val wheelRadius: Double get() = Constants.Drivetrain.wheelDiameter / 2

    fun tankDrive(leftPower: Double, rightPower: Double) {
        pidControllerGroup.stopAll()
        state = State.MANUAL_DRIVE
        powertrain.tankDrive(leftPower, rightPower)
    }

    fun curvatureDrive(power: Double, rotation: Double) {
        pidControllerGroup.stopAll()
        state = State.MANUAL_DRIVE
        powertrain.curvatureDrive(power, rotation)
    }

    fun driveStraight(power: Double) {
        if (state != State.AIDED_DRIVE_STRAIGHT) {
            pidControllerGroup.stopAll()
            state = State.AIDED_DRIVE_STRAIGHT
            powertrain.mode = Powertrain.Mode.ARCADE_DRIVE
            turnPid.target = navx.heading
        }
        this.power = power
    }

    fun driveToPosition(position: Double) {
        if (state != State.PID_STRAIGHT) {
            pidControllerGroup.stopAll()
            powertrain.reset()
            state = State.PID_STRAIGHT
            positionPid.target = position + distance
        }
    }

    fun setPowerScaling(newScaling: Double) {
        powertrain.differentialDrive.setMaxOutput(newScaling)
    }

    fun stop() {
        state = State.STOPPED
        tankDrive(0.0, 0.0)
    }

    override fun periodic() {
        pidControllerGroup.executeAll()
        powertrain.periodic()
    }

    enum class State(val value: Int) {
        // 0-9: Manual Modes
        MANUAL_DRIVE(0),
        // 10-20: Aided Modes
        AIDED_DRIVE_STRAIGHT(10),
        STOPPED(11),
        // 20-29: PID Modes
        PID_TURNING(20),
        PID_STRAIGHT(21),
        PID_LIMELIGHT_TURNING(22),
        PID_LIMELIGHT_DRIVE(23);
    }
}