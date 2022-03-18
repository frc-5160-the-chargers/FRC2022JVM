package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class OI extends SubsystemBase {
    XboxController driver_controller = new XboxController(0);
    XboxController operator_controller = new XboxController(1);

    private double rotation_assist_deadband = .05;
    private double driver_deadband = .05;
    private double max_motor_power = .4;
    private double turbo_mode_power = .57;

    private double deadzone(double i, double dz){
        if (Math.abs(i) <= dz){
            return 0;
        }
        else {
            return i;
        }
    }

    public boolean ready_straight_assist(){
        double[] output = get_raw_output();
        return (output[0] <= rotation_assist_deadband);
    }

    public double[] get_raw_output(){
        double x = driver_controller.getRightX();
        double y = driver_controller.getLeftY();
        return new double[]{x, -y};
    }

    public double[] get_curvature_output(){
        double[] output = get_raw_output();
        double x = -Math.copySign(Math.abs(deadzone(output[0], driver_deadband)), output[0]);
        double y = Math.copySign(Math.abs(deadzone(output[1], driver_deadband)), output[1]);
        if (get_beast_mode()){
            y *= -1;
        }
        return new double[]{x, y};
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

    // public double process_turbo_mode(){
    //     double modifier = get_turbo_mode_modifier();
    //     double x = map_value(modifier, 0, 1, max_motor_power, turbo_mode_power);
    //     return x;
    // }
}
