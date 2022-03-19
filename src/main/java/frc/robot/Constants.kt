// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot

import com.revrobotics.CANSparkMax.IdleMode
import frc.robot.utils.PIDConstants

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 *
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
object Constants {
    object Drivetrain {
        const val voltageCompensation = 11.0
        const val stallCurrentLimit = 39
        val defaultMode = IdleMode.kBrake
        const val rampRate = 1.0
        const val reverseMotor = true
        const val distanceBetweenWheels = 22.5
        const val maxMotorPower = .4
        const val maxAutoPower = .4
        const val turboModePower = .57
        const val kfStraight = .15
        const val kfTurn = 0.0
        val turnPid = PIDConstants(-0.1, 0.0, -0.01)
        const val turnPidKey = "Drivetrain Turn PID"
        const val turnTolerance = 1.0
        val positionPid = PIDConstants(0.035, 0.0, 0.001)
        const val positionPidKey = "Drivetrian Position PID"
        const val positionTolerance = 0.5
        const val gearRatio = 1 / 10.71
        const val wheelDiameter = 6.0
    }
}