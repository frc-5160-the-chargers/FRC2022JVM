// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.pid.PIDConstants;

import com.revrobotics.CANSparkMax.IdleMode;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
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

    public static final  class oiConstants{
        public static final double driver_deadband = .05;
        public static final double drivetrain_rotation_assist_deadband = .05;

        public static final double lift_deadband = .05;
        public static final double climb_deadband = .1;

        public static final double color_wheel_deadband = .1;
    }
}
