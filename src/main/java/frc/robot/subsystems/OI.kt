package frc.robot.subsystems

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.utils.Point
import frc.robot.utils.scaleBetweenRanges
import kotlin.math.abs
import kotlin.math.withSign

private const val rotationAssistDeadband = .05
private const val driverDeadband = .05
private const val maxMotorPower = .4
private const val turboModePower = .57

class OI : SubsystemBase() {
    var driverController = XboxController(0)
    var operatorController = XboxController(1)

    private fun deadzone(i: Double, dz: Double): Double = if (abs(i) <= dz) 0.0 else i

    val readyStraightAssist: Boolean get() = rawOutput.x <= rotationAssistDeadband

    val rawOutput: Point get() {
        val x = driverController.rightX
        val y = driverController.leftY
        return Point(x, -y)
    }

    val curvatureOutput: Point get() {
        val output = rawOutput
        val x: Double = -abs(deadzone(output.x, driverDeadband)).withSign(output.x)
        var y: Double = abs(deadzone(output.y, driverDeadband)).withSign(output.y)
        if (beastMode) {
            y *= -1.0
        }
        return Point(x, y)
    }

    val beastMode: Boolean by driverController::bButton
    val updatePidDash: Boolean by driverController::xButtonPressed
    val togglePidTypePressed: Boolean by driverController::backButtonPressed
    val enablePid: Boolean by driverController::aButtonPressed
    val manualControlOverride: Boolean by driverController::bButtonPressed
    val updateTelemetry: Boolean by driverController::yButtonPressed
    val turboModeModifier: Double by driverController::rightTriggerAxis

    fun processTurboMode(): Double {
        val modifier: Double = turboModeModifier
        return modifier.scaleBetweenRanges(0.0..1.0, maxMotorPower..turboModePower)
    }

    //Operator
    val intakeOuttake: Boolean by operatorController::aButton
    val intakeIntake: Boolean by operatorController::bButton
    val intakeRaise: Boolean by operatorController::rightBumperPressed
    val intakeLower: Boolean by operatorController::leftBumperPressed
    val climberRaise: Boolean by operatorController::xButton
    val climberClimb: Boolean by operatorController::yButton
    val positionControl: Boolean by operatorController::backButtonPressed
    val rotationControl: Boolean by operatorController::startButtonPressed
}