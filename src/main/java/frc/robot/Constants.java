// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.pid.PIDConstants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.XboxController.Button;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class controlBindings {
        public static final int outtake = Button.kA.value;
        public static final int intake = Button.kB.value;

        public static final int lowerArm = Button.kLeftBumper.value;
        public static final int raiseArm = Button.kRightBumper.value;

        public static final int toggleShooter = Button.kX.value;

        public static final int runSerializerForward = Button.kY.value;
        public static final int runSerializerReverse = Button.kLeftStick.value;
    }

    public static final class drivetrainConstants {
        public static final int[] motors_left = {1, 2};
        public static final int[] motors_right = {3, 4};
        
        public static final double voltage_compensation=11;
        public static final int stall_current_limit=39;
        public static final IdleMode default_mode=IdleMode.kBrake;
        public static final double ramp_rate=1;
        public static final boolean reverse_motor=true;

        public static final double distance_between_wheels = 22.5;

        public static final double max_motor_power = .4;
        public static final double max_auto_power = .4;
        public static final double turbo_mode_power = .57;
        
        public static final double kF_straight = .15;
        public static final double kF_turn = 0;

        public static final PIDConstants turn_pid = new PIDConstants(-0.1, 0, -0.01);
        public static final String turn_pid_key = "Drivetrain Turn PID";
        public static final double turn_tolerance = 1;

        public static final PIDConstants position_pid = new PIDConstants(0.035, 0, 0.001);
        public static final String position_pid_key = "Drivetrian Position PID";
        public static final double position_tolerance = 0.5;

        public static final double gear_ratio = (1/10.71);
        public static final double wheel_diameter = 6;
    }

    public static final class oiConstants{
        public static final double driver_deadband = .05;
        public static final double drivetrain_rotation_assist_deadband = .05;

        public static final double lift_deadband = .05;
        public static final double climb_deadband = .1;

        public static final double color_wheel_deadband = .1;
    }
    public static final class intakeRollerConstants{
        public static final int motorPort = 5;

        public static final double rollerPower = .95;
        public static final ControlMode controlMode = ControlMode.PercentOutput;
    }

    public static final class intakeArmConstants{
        public static final int motor_port = 6;
        public static final double max_power = .25;
        public static final double min_power = -.15;
            
        public static final double voltage_compensation=11;
        public static final int stall_current_limit=39;
        public static final IdleMode default_mode=IdleMode.kBrake;
        public static final double ramp_rate=1;
        public static final boolean reverse_motor=true;

        public static final PIDConstants pid_values = new PIDConstants(0.5, 0, 0.005);
        public static final String pid_key = "Intake Lift PID";
        public static final double tolerance = 0;

        public static final double up_position = 0;
        public static final double down_position = -(21*Math.PI)/48;

        public static final double gear_ratio = (1/96.0);
    }

    public static final class shooterConstants{
        public static final int motorPort = 7;
        public static final double enablePower = 0.95;
    }

    public static final class serializerConstants{
        public static final int motorPort1 = 8;
        public static final double enablePower = 0.25;
    }

    public static final class climberConstants {
        public static final int[] motorPorts = {9, 10}; // TODO: Set to actual values
        public static final double gearRatio = 0.2;
        public static final IdleMode defaultMotorMode = IdleMode.kBrake;

        // TODO: May need to tune any of the following
        public static final double motorRunSpeed = 0.5;
        public static final PIDConstants holdPIDValues = new PIDConstants(0.5, 0.0, 0.01);
        public static final double maxMotorHoldPower = 0.75;
        public static final double holdFeedForwardFactor = 0.1; // This may have to be negative
    }
}
