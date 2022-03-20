package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.SuperPIDController;
import frc.robot.Constants.intakeArmConstants;
import frc.robot.utils.Range;

import static frc.robot.subsystems.IntakeArm.State.*;
import static frc.robot.subsystems.IntakeArm.Position.*;

public class IntakeArm extends SubsystemBase{
    private final CANSparkMax armMotor;
    private final RelativeEncoder armEncoder;

    private final SuperPIDController armPid;
    
    private State state;
    private Position position;

    private double power = 0;

    public IntakeArm(){
        armMotor = new CANSparkMax(intakeArmConstants.motor_port, MotorType.kBrushless);
        armEncoder = armMotor.getEncoder();

        state = STOPPED;
        position = MATCH_START;

        armPid = new SuperPIDController.Builder(
            intakeArmConstants.pid_values, 
            () -> get_position(), 
            (x) -> {set_power_raw(x);})
            .dashPidKey(intakeArmConstants.pid_key)
            .feedForward((x, y) -> {return 0.5;})
            .outputRange(new Range(intakeArmConstants.min_power, intakeArmConstants.max_power))
            .tolerance(intakeArmConstants.tolerance)
            .build();
    }

    public enum State {
        STOPPED(0),
        RAISING(1),
        LOWERING(2),
        PID_CONTROLLED(10);

        public final int value;
        State(final int value){
            this.value = value;
        }
    }

    public enum Position{
        MATCH_START(0),
        LOWERED(1),
        RAISED(2);

        public final int value;
        Position(final int value){
            this.value = value;
        }
    }

    public void reset_state(){
        state = STOPPED;
        power = 0;
        position = MATCH_START;
    }
    
    public void reset_encoder(){
        armEncoder.setPosition(0);
    }

    public void reset(){
        reset_state();
        reset_encoder();
    }

    private double get_position(){
        double rawPos = armEncoder.getPosition();
        double adjustedPos = rawPos*intakeArmConstants.gear_ratio;
        return adjustedPos*2*Math.PI;
    }

    public void raise_lift(double power){
        armPid.stop();
        state = RAISING;
        this.power = power;
    }

    public void lower_lift(double power){
        armPid.stop();
        state = LOWERING;
        this.power = power;
    }

    public void send_down(){
        position = LOWERED;
        state = PID_CONTROLLED;
    }

    public void send_up(){
        position = RAISED;
        state = PID_CONTROLLED;
    }

    public void set_match_start(){
        position = MATCH_START;
        state = PID_CONTROLLED;
    }

    public void stop(){
        armPid.stop();
        state = STOPPED;
        power = 0;
    }

    public boolean get_ready(){
        return armPid.isOnTarget();
    }
    
    private void set_position_pid(double position){
        if (state != PID_CONTROLLED){
            armPid.reset();
        }
        armPid.setTarget(position);
        state = PID_CONTROLLED;
    }

    private void set_power_raw(double power){
        this.power = power;
    }

    @Override
    public void periodic(){
        armPid.execute();

        if (position == RAISED){
            armPid.setTarget(intakeArmConstants.up_position);
        }
        else if (position == LOWERED){
            armPid.setTarget(intakeArmConstants.down_position);
            if (Math.abs(armPid.getError()) < 0.2){
                set_power_raw(0);
            }
        }
        else if (position == MATCH_START){
            armPid.setTarget(0.);
        }
        
        if (state == PID_CONTROLLED){
            armMotor.set(power);
        }
        else{
            armPid.setTarget(0.);
        }
    }
}
