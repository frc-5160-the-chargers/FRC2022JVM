package frc.robot.subsystems;

import java.util.function.BiFunction;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import frc.robot.subsystems.Powertrain;
import frc.robot.SuperPIDController;
import frc.robot.SuperPIDControllerGroup;
import frc.robot.utils.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.RelativeEncoder;

public class Drivetrain extends SubsystemBase{
    private final Powertrain powertrain;
    private final NavX navx;

    private final RelativeEncoder encoder_left;
    private final RelativeEncoder encoder_right;

    private final RelativeEncoder[] encoders;

    private final int MANUAL_DRIVE = 0;
    private final int AIDED_DRIVE_STRAIGHT = 10;
    private final int STOPPPED = 11;

    private final int PID_TURNING = 20;
    private final int PID_STRAIGHT = 21;
    private final int PID_LIMELIGHT_TURNING = 22;
    private final int PID_LIMELIGHT_DRIVE = 23;

    private final int BOTH = 0;
    private final int LEFT = 1;
    private final int RIGHT = 2;

    private final String turn_pid_key = "Drivetrain Turn PID";
    private final PIDConstants turn_pid_value = new PIDConstants(-0.1, 0, -0.01);

    private final String position_pid_key = "Drivetrain Position PID";
    private final PIDConstants position_pid_value = new PIDConstants(0.035, 0, 0.001);

    private final String limelight_turn_pid_key = "Drivetrain Limelight Turning PID";
    private final PIDConstants limelight_turn_pid_value = new PIDConstants(0.2, 0.001, 0.1);

    private final String limelight_position_pid_key = "Drivetrain Limelight Distance Driving PID";
    private final PIDConstants limelight_position_pid_value = new PIDConstants(0.035, 0, 0.001);

    private final double max_auto_power = .4;
    private final double gear_ratio = 1/10.71;
    private final double wheel_diameter = 6.;
    private final double kF_straight = 0.15;
    private final double kF_turn = 0;
    private final double turn_tolerance = 1;
    private final double position_tolerance = 1;

    private int state;
    private double power;
    private double rotation;

    private final DoubleSupplier turn_supplier;
    private final DoubleConsumer turn_consumer;
    private final BiFunction<Double, Double, Double> turn_feedfowards;
    private final Range turn_output_range;
    private SuperPIDController turn_pid;

    private final DoubleSupplier position_supplier;
    private final DoubleConsumer position_consumer;
    private final BiFunction<Double, Double, Double> position_feedfowards;
    private final Range position_output_range;
    private SuperPIDController position_pid;
    
    SuperPIDController[] pid_controllers;
    private SuperPIDControllerGroup pid_manager;

    public Drivetrain(Powertrain powertrain, NavX navx) {
        this.powertrain = powertrain;
        this.navx = navx;

        encoder_left = powertrain.motors[0].getEncoder();
        encoder_right = powertrain.motors[2].getEncoder();
        encoders = new RelativeEncoder[]{encoder_left, encoder_right};

        turn_supplier = () -> { return get_distance(); };
        turn_consumer = (x)->{rotation=x; powertrain.mode=powertrain.ARCADE_DRIVE;};
        turn_feedfowards = (target, error)->{ return 0.0;};
        turn_output_range = new Range(-max_auto_power, max_auto_power);
        turn_pid = new SuperPIDController.Builder(position_pid_value, turn_supplier, turn_consumer)
            .dashPidKey(turn_pid_key)
            .feedForward(turn_feedfowards)
            .outputRange(turn_output_range)
            .tolerance(turn_tolerance)
        .build();

        position_supplier = () -> { return get_distance(); };
        position_consumer = (x)->{power=x; powertrain.mode=powertrain.ARCADE_DRIVE;};
        position_feedfowards = (target, error)->{ return 0.0; };
        position_output_range = new Range(-max_auto_power, max_auto_power);
        position_pid = new SuperPIDController.Builder(position_pid_value, position_supplier, position_consumer)
            .dashPidKey(position_pid_key)
            .feedForward(position_feedfowards)
            .outputRange(position_output_range)
            .tolerance(position_tolerance)
        .build();

        pid_controllers = new SuperPIDController[]{turn_pid, position_pid};
        pid_manager = new SuperPIDControllerGroup(pid_controllers);

        reset();
    }

    private void reset(){
        state = MANUAL_DRIVE;
        encoder_left.setPosition(0);
        encoder_right.setPosition(1);
    }

    public double get_position(){
        double[] positions = {encoder_left.getPosition(), encoder_right.getPosition()};
        double rotations = Utils.average(positions);
        double rotations_adjusted = rotations*gear_ratio;
        double radians = rotations_adjusted*2*Math.PI;

        return radians;
    }

    public double get_rotational_velocity(){
        double[] velocities = {encoder_left.getVelocity(), encoder_right.getVelocity()};
        double rpm = Utils.average(velocities);
        double rpm_adjusted = rpm*gear_ratio;
        double omega = (2*rpm_adjusted*Math.PI)/60.;

        return omega;
    }

    public double get_distance(){
        double rotations = get_position();
        double distance = rotations*(wheel_diameter/2.);
        return distance;
    }
    
    public double get_velocity(){
        double radsec = get_rotational_velocity();
        double vel = radsec*(wheel_diameter/2);
        return vel;
    }

    public void tank_drive(double left_power, double right_power){
        pid_manager.stopAll();
        state = MANUAL_DRIVE;
        powertrain.tank_drive(left_power, right_power);
    }

    public void curvature_drive(double power, double rotation){
        pid_manager.stopAll();
        state = MANUAL_DRIVE;
        powertrain.curvature_drive(power, rotation);
    }

    public void drive_straight(double power){
        if (state!=AIDED_DRIVE_STRAIGHT){
            pid_manager.stopAll();
            state = AIDED_DRIVE_STRAIGHT;
            powertrain.mode = powertrain.ARCADE_DRIVE;
            turn_pid.setTarget(navx.get_heading());
        }
        power = this.power;
    }

    public void drive_to_position(double position){
        if (state != PID_STRAIGHT){
            pid_manager.stopAll();
            powertrain.reset();
            state = PID_STRAIGHT;
            position_pid.setTarget(position + get_distance());
        }
    }

    public void set_power_scaling(double new_scaling){
        powertrain.differential_drive.setMaxOutput(new_scaling);
    }

    public void stop(){
        state = STOPPPED;
        tank_drive(0, 0);
    }

    @Override
    public void periodic(){
        pid_manager.executeAll();
        if (powertrain.mode == powertrain.ARCADE_DRIVE){
            powertrain.set_arcade_powers(power, rotation);
        }
    }
}
