package frc.robot.subsystems;

import java.util.function.BiFunction;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import frc.robot.SuperPIDController;
import frc.robot.SuperPIDControllerGroup;
import frc.robot.utils.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.RelativeEncoder;

import static frc.robot.subsystems.Drivetrain.State.*;

public class Drivetrain extends SubsystemBase {
    private final Powertrain powertrain;
    private final NavX navx;

    private final RelativeEncoder encoderLeft;
    private final RelativeEncoder encoderRight;

    private final String turnPidKey = "Drivetrain Turn PID";
    private final PIDConstants turnPidConstants = new PIDConstants(-0.1, 0, -0.01);

    private final String positionPidKey = "Drivetrain Position PID";
    private final PIDConstants positionPidConstants = new PIDConstants(0.035, 0, 0.001);

    private final double maxAutoPower = .4;
    private final double gearRatio = 1/10.71;
    private final double wheelDiameter = 6;
    private final double kFStraight = 0.15;
    private final double kFTurn = 0;
    private final double turn_tolerance = 1;
    private final double positionTolerance = 1;

    private State state;
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

        final DoubleSupplier turnInput = this::getDistance;
        final DoubleConsumer turnOutput = x -> rotation=x;
        final BiFunction<Double, Double, Double> turnFeedfowards = (target, error) -> 0.0;
        final Range turnOutputRange = new Range(-maxAutoPower, maxAutoPower);
        turnPid = new SuperPIDController.Builder(turnPidConstants, turnInput, turnOutput)
            .dashPidKey(turnPidKey)
            .feedForward(turnFeedfowards)
            .outputRange(turnOutputRange)
            .tolerance(turn_tolerance)
            .build();

        final DoubleSupplier positionInput = this::getDistance;
        final DoubleConsumer positionOutput = x -> {power=x; powertrain.mode= powertrain.ARCADE_DRIVE;};
        final BiFunction<Double, Double, Double> positionFeedfowards = (target, error) -> 0.0;
        final Range positionOutputRange = new Range(-maxAutoPower, maxAutoPower);
        positionPid = new SuperPIDController.Builder(positionPidConstants, positionInput, positionOutput)
            .dashPidKey(positionPidKey)
            .feedForward(positionFeedfowards)
            .outputRange(positionOutputRange)
            .tolerance(positionTolerance)
            .build();

        pidControllerGroup = new SuperPIDControllerGroup(turnPid, positionPid);

        reset();
    }

    private void reset(){
        state = MANUAL_DRIVE;
        encoderLeft.setPosition(0);
        encoderRight.setPosition(1);
    }

    public double getPosition() {
        double[] positions = {encoderLeft.getPosition(), encoderRight.getPosition()};
        double rotations = Utils.average(positions);
        double rotations_adjusted = rotations* gearRatio;
        double radians = rotations_adjusted*2*Math.PI;

        return radians;
    }

    public double getRotationalVelocity() {
        double[] velocities = { encoderLeft.getVelocity(), encoderRight.getVelocity() };
        double rpm = Utils.average(velocities);
        double rpm_adjusted = rpm * gearRatio;
        double omega = (2*rpm_adjusted*Math.PI)/60;

        return omega;
    }

    public double getDistance() {
        double rotations = getPosition();
        double distance = rotations*(wheelDiameter/2);
        return distance;
    }
    
    public double getVelocity() {
        double radsec = getRotationalVelocity();
        double vel = radsec*(wheelDiameter /2);
        return vel;
    }

    public void tankDrive(double left_power, double right_power) {
        pidControllerGroup.stopAll();
        state = MANUAL_DRIVE;
        powertrain.tank_drive(left_power, right_power);
    }

    public void curvatureDrive(double power, double rotation) {
        pidControllerGroup.stopAll();
        state = MANUAL_DRIVE;
        powertrain.curvature_drive(power, rotation);
    }

    public void driveStraight(double power) {
        if (state!=AIDED_DRIVE_STRAIGHT){
            pidControllerGroup.stopAll();
            state = AIDED_DRIVE_STRAIGHT;
            powertrain.mode = powertrain.ARCADE_DRIVE;
            turnPid.setTarget(navx.get_heading());
        }
        this.power = power;
    }

    enum State {
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
