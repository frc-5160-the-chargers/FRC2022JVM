package frc.robot.subsystems;

import frc.robot.utils.Range;
import frc.robot.utils.Utils;
import frc.robot.Constants.oiConstants;
import frc.robot.Constants.drivetrainConstants;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class OI extends SubsystemBase{
    XboxController driver_controller = new XboxController(0);
    XboxController operator_controller = new XboxController(1);

    private double deadzone(double i, double dz){
        if (Math.abs(i) <= dz){
            return 0;
        }
        else {
            return i;
        }
    }

    //Driver

    public boolean ready_straight_assist(){
        double[] output = get_raw_output();
        return (output[0] <= oiConstants.drivetrain_rotation_assist_deadband);
    }

    public double[] get_raw_output(){
        double x = driver_controller.getRightX();
        double y = driver_controller.getLeftY();
        return new double[]{
            x,
            y // Moving the controller to the left gives a negative number, but should represent a positive (clockwise) rotation
        };
    }

    public double[] get_curvature_output(){
        double[] output = get_raw_output();
        double x = -Math.copySign(Math.abs(deadzone(output[0], oiConstants.driver_deadband)), output[0]);
        double y = Math.copySign(Math.abs(deadzone(output[1], oiConstants.driver_deadband)), output[1]);
        if (get_beast_mode()){
            y *= -1;
        }
        return new double[]{x*.5, y*.7};
    }

    public boolean get_beast_mode(){
        return driver_controller.getBButton();
    }

    public boolean get_update_pid_dash(){
        return driver_controller.getXButtonPressed();
    }
    
    public boolean get_toggle_pid_type_pressed(){
        return driver_controller.getBackButtonPressed();
    }

    public boolean get_enable_pid(){
        return driver_controller.getAButtonPressed();
    }

    public boolean get_manual_control_override(){  
        return driver_controller.getBButtonPressed();
    }

    public boolean get_update_telemetry(){
        return driver_controller.getYButtonPressed();
    }

    public double get_turbo_mode_modifier(){
        return driver_controller.getRightTriggerAxis();
    }

    public double process_turbo_mode(){
        double modifier = get_turbo_mode_modifier();
        double x = Utils.mapValueInRange(modifier, new Range(0,1), new Range(drivetrainConstants.max_motor_power, drivetrainConstants.turbo_mode_power));
        return x;   
    }

    //Operator

    public boolean get_intake_outtake(){
        return operator_controller.getAButton();
    }

    public boolean get_intake_intake(){
        return operator_controller.getBButton();
    }
        
    public boolean get_intake_raise(){
        return operator_controller.getRightBumperPressed();
    }

    public boolean get_intake_lower(){
        return operator_controller.getLeftBumperPressed();
    }

    public boolean get_shooter_spinup(){
        return operator_controller.getXButton();
    }

    public boolean get_shooter_fire(){
        return operator_controller.getYButton();
    }

    public boolean get_position_control(){
        return operator_controller.getBackButtonPressed();
    }
    
    public boolean get_rotation_control(){
        return operator_controller.getStartButtonPressed();
    }
}
