package frc.robot.subsystems

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.utils.Point
import kotlin.math.abs
import kotlin.math.withSign

private const val rotationAssistDeadband = .05
private const val driverDeadband = .05
private const val maxMotorPower = .4
private const val turboModePower = .57

class OI : SubsystemBase() {
    var driver_controller = XboxController(0)
    var operator_controller = XboxController(1)

    private fun deadzone(i: Double, dz: Double): Double {
        return if (abs(i) <= dz) {
            0.0
        } else {
            i
        }
    }

    fun readyStraightAssist(): Boolean {
        val output = getRawOutput()
        return output.x <= rotationAssistDeadband
    }

    fun getRawOutput(): Point {
        val x = driver_controller.rightX
        val y = driver_controller.leftY
        return Point(x, -y)
    }

    fun getCurvatureOutput(): Point {
        val output = getRawOutput()
        val x = -abs(deadzone(output.x, driverDeadband)).withSign(output.x)
        var y = abs(deadzone(output.y, driverDeadband)).withSign(output.y)
        if (getBeastMode()) {
            y *= -1.0
        }
        return Point(x, y)
    }

    fun getBeastMode(): Boolean {
        return driver_controller.bButton
    }

    fun getUpdatePidDash(): Boolean {
        return driver_controller.xButtonPressed
    }

    fun getTogglePidTypePressed(): Boolean {
        return driver_controller.backButtonPressed
    }

    fun getEnablePid(): Boolean {
        return driver_controller.aButtonPressed
    }

    fun getManualControlOverride(): Boolean {
        return driver_controller.bButtonPressed
    }

    fun getUpdateTelemetry(): Boolean {
        return driver_controller.yButtonPressed
    }

    fun getTurboModeModifier(): Double {
        return driver_controller.rightTriggerAxis
    }

    // public double process_turbo_mode(){
    //     double modifier = get_turbo_mode_modifier();
    //     double x = map_value(modifier, 0, 1, max_motor_power, turbo_mode_power);
    //     return x;
    // }
}