/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutonoumousBackAndShootGroup extends SequentialCommandGroup {
  /**
   * Creates a new AutonoumousBackAndShootGroup.
   */
  public AutonoumousBackAndShootGroup(Drivetrain drivetrain, Limelight limelight, Shooter shooter, Conveyor conveyor) {
    super(new AutoDriveCommand(-0.5,12, drivetrain),
      new InstantCommand(() -> limelight.beforeTurnToTarget()),
      new FindTarget(limelight),
      new InstantCommand(() -> shooter.spinUp()),
      new AutoTurnToTargetCommand(limelight, drivetrain, shooter),
      new FireAll(shooter, conveyor),
      new InstantCommand(() -> limelight.afterTurnToTarget())
      ) ;
  }
}
