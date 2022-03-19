package frc.robot.subsystems;

import java.util.function.BiFunction;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import frc.robot.SuperPIDController;
import frc.robot.SuperPIDControllerGroup;
import frc.robot.Constants.drivetrainConstants;
import frc.robot.utils.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.RelativeEncoder;

import frc.robot.Constants.drivetrainConstants;

import static frc.robot.subsystems.Drivetrain.State.*;

public class Drivetrain extends SubsystemBase {
    private final Powertrain powertrain;
    private final NavX navx;

    private final RelativeEncoder encoderLeft;
    private final RelativeEncoder encoderRight;

    public State state;
    private double power;
    private double rotation;

    private final SuperPIDController turnPid;
    private final SuperPIDController positionPid;
    private final SuperPIDControllerGroup pidControllerGroup;

    public Drivetrain(Powertrain powertrain, NavX navx) {
        this.powertrain = powertrain;
        this.navx = navx;

        encoderLeft = powertrain.left1.getEncoder();
        encoderRight = powertrain.right1.getEncoder();

        final DoubleSupplier turnInput = () -> { return navx.get_heading(); };
        final DoubleConsumer turnOutput = x -> { rotation=x; };
        final BiFunction<Double, Double, Double> turnFeedfowards = (target, error) -> 0.0;
        final Range turnOutputRange = new Range(-drivetrainConstants.max_motor_power, drivetrainConstants.max_motor_power);
        turnPid = new SuperPIDController.Builder(drivetrainConstants.turn_pid, turnInput, turnOutput)
            .dashPidKey(drivetrainConstants.turn_pid_key)
            .feedForward(turnFeedfowards)
            .outputRange(turnOutputRange)
            .tolerance(drivetrainConstants.turn_tolerance)
            .build();

        final DoubleSupplier positionInput = this::getDistance;
        final DoubleConsumer positionOutput = x -> {power=x;};
        final BiFunction<Double, Double, Double> positionFeedfowards = (target, error) -> 0.0;
        final Range positionOutputRange = new Range(-drivetrainConstants.max_motor_power, drivetrainConstants.max_motor_power);
        positionPid = new SuperPIDController.Builder(drivetrainConstants.position_pid, positionInput, positionOutput)
            .dashPidKey(drivetrainConstants.position_pid_key)
            .feedForward(positionFeedfowards)
            .outputRange(positionOutputRange)
            .tolerance(drivetrainConstants.position_tolerance)
            .build();

        pidControllerGroup = new SuperPIDControllerGroup(turnPid, positionPid);

        reset();
    }

    public void reset(){
        state = MANUAL_DRIVE;
        encoderLeft.setPosition(0);
        encoderRight.setPosition(1);
    }

    public double getPosition() {
        double[] positions = {encoderLeft.getPosition(), encoderRight.getPosition()};
        double rotations = Utils.average(positions);
        double rotations_adjusted = rotations* drivetrainConstants.gear_ratio;
        double radians = rotations_adjusted*2*Math.PI;

        return radians;
    }

    public double getRotationalVelocity() {
        double[] velocities = { encoderLeft.getVelocity(), encoderRight.getVelocity() };
        double rpm = Utils.average(velocities);
        double rpm_adjusted = rpm * drivetrainConstants.gear_ratio;
        double omega = (2*rpm_adjusted*Math.PI)/60;

        return omega;
    }

    public double getDistance() {
        double rotations = getPosition();
        double distance = rotations*(drivetrainConstants.wheel_diameter/2);
        return distance;
    }
    
    public double getVelocity() {
        double radsec = getRotationalVelocity();
        double vel = radsec*(drivetrainConstants.wheel_diameter /2);
        return vel;
    }

    public void tankDrive(double left_power, double right_power) {
        pidControllerGroup.stopAll();
        state = MANUAL_DRIVE;
        powertrain.tankDrive(left_power, right_power);
    }

    public void curvatureDrive(double power, double rotation) {
        pidControllerGroup.stopAll();
        state = MANUAL_DRIVE;
        powertrain.curvatureDrive(power, rotation);
    }

    public void driveStraight(double power) {
        if (state!=AIDED_DRIVE_STRAIGHT){
            pidControllerGroup.stopAll();
            state = AIDED_DRIVE_STRAIGHT;
            powertrain.mode = Powertrain.Mode.ARCADE_DRIVE;
            turnPid.setTarget(navx.get_heading());
        }
        this.power = power;
    }

    public void driveToPosition(double position){
        if (state != PID_STRAIGHT){
            pidControllerGroup.stopAll();
            powertrain.reset();
            state = PID_STRAIGHT;
            positionPid.setTarget(position + getDistance());
        }
    }

    public void setPowerScaling(double newScaling){
        powertrain.differentialDrive.setMaxOutput(newScaling);
    }

    public void stop(){
        state = STOPPED;
        tankDrive(0, 0);
    }

    @Override
    public void periodic(){
        pidControllerGroup.executeAll();
        if (powertrain.mode == Powertrain.Mode.ARCADE_DRIVE){
            powertrain.arcadeDrive(power, rotation);
        }
    }

    public enum State {
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

        public final int value;
        State(final int value) {
            this.value = value;
        }
    }
}
