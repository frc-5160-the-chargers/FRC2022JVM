package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class oi extends SubsystemBase{
    XboxController driver_controller = new XboxController(0);
    XboxController operator_controller = new XboxController(1);
}
