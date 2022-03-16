package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.kauailabs.navx.frc.AHRS;

public class NavX extends SubsystemBase{
    private final AHRS navx = new AHRS();

    public NavX(){
        reset();
    }

    public void reset(){
        navx.reset();
    }

    public double get_heading(){ //in degrees
        return navx.getAngle();
    }

    public boolean is_connected(){
        return navx.isConnected();
    }

    @Override
    public void periodic() {}
}
