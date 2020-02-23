/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.Constants.LimelightConstants;
import frc.robot.Constants.ShooterConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight extends SubsystemBase {
  /**
   * Creates a new Limelight.
   */
    private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    private NetworkTableEntry tx = table.getEntry("tx");
    private NetworkTableEntry ty = table.getEntry("ty");
    private NetworkTableEntry ta = table.getEntry("ta");
    private NetworkTableEntry tv = table.getEntry("ta");
    private NetworkTableEntry ledMode = table.getEntry("ledMode");
    private int loop;


  public Limelight() {
    /**
     * tx - Horizontal Offset
     * ty - Vertical Offset 
     * ta - Area of target 
     * tv - Target Visible
     */

    
    this.tx = table.getEntry("tx");
    this.ty = table.getEntry("ty");
    this.ta = table.getEntry("ta");
    this.ta = table.getEntry("tv");
    this.ledMode = table.getEntry("ledMode");
    this.loop = 0; 
    setPipeline(LimelightConstants.DRIVE_PIPELINE);
    setLedOn(false);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
    //read values periodically
    double x = this.tx.getDouble(0.0);
    double y = this.ty.getDouble(0.0);
    double area = this.ta.getDouble(0.0);


    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);

  }

  /**Returns distance to target in inches */
  public double distanceToTargetInInches(){
    double cameraAngle = LimelightConstants.CAMERA_ANGLE; 
    double angleToTarget = this.tx.getDouble(0.0);
    double camHeight = LimelightConstants.CAMERA_HEIGHT;
    double targetHeight = LimelightConstants.TARGET_HEIGHT;
    double distance =  ((targetHeight-camHeight) / Math.tan(cameraAngle+angleToTarget));
    
    return distance;
    
  }

  /** Returns if limelight can see defined retroreflective target */
  public boolean hasTarget(){
   // this.table.getEntry("ledMode").setNumber(3);
    SmartDashboard.putNumber("tv; ", tv.getDouble(0));
    if(tv.getDouble(0) != 0)
      return true;
    

    return false;
  }

  public NetworkTableEntry getTx() {
    return tx;
  }

  public NetworkTableEntry getTy() {
    return ty;
  }

  public NetworkTableEntry getTa() {
    return ta;
  }

  public void setPipeline(int pipeline){
    this.table.getEntry("pipeline").setNumber(pipeline);
  }

  public void setLedOn(boolean isOn) {
    if (isOn){
      ledMode.setNumber(LimelightConstants.LED_ON);
    } else {
      ledMode.setNumber(LimelightConstants.LED_OFF);
    }
  }
  
  /**Returns the angle to targed in degrees negative values to the left and positive to the right
   * used for turn to target
   */
  public double getAngleOfError(){
    //+1 is a fudge factor cor camera mounting
    return getTy().getDouble(0.0) + 1;
  }

  public void autoTurnToTarget(Drivetrain drivetrain, Shooter shooter){
    while (turnToTarget(drivetrain,shooter));
  }
  public boolean turnToTarget(Drivetrain drivetrain, Shooter shooter){
    SmartDashboard.putString("turnToTarget ","Started");
    double turn = 0;
    double min = ShooterConstants.MIN_TURN;
    boolean check = hasTarget();
    SmartDashboard.putString("Target ","" + check);
    SmartDashboard.putString("Initial TY","" + getAngleOfError());
    if(Math.abs(getAngleOfError()) >= LimelightConstants.TURN_TO_TARGET_TOLERANCE && hasTarget()){
      turn = getAngleOfError()*0.03;     
      if (Math.abs(turn) < min){
        turn = turn > 0 ? min:-min;
      }
      drivetrain.getDrive().tankDrive(-turn, turn);
      SmartDashboard.putString("Loop TY:",this.loop++ + ":" + getAngleOfError());
    } else {
      //This prevents errors on the console from not updated drive train.
      drivetrain.getDrive().tankDrive(0, 0);
    }
    SmartDashboard.putString("Ending TY","" + getAngleOfError());
    SmartDashboard.putString("Turning Complete","Turning Complete");
    return ! (Math.abs(getAngleOfError()) >= LimelightConstants.TURN_TO_TARGET_TOLERANCE && hasTarget());
  }

  public void beforeTurnToTarget(){
    setLedOn(true);
    switchPipeline(true);
  }

  public void afterTurnToTarget(){
    setLedOn(false);
    switchPipeline(false);
  }
  public void switchPipeline(boolean targeting){
    if(targeting == true){
      setPipeline(LimelightConstants.TARGET_PIPELINE);
    } else {
      setPipeline(LimelightConstants.DRIVE_PIPELINE);
    }
  }
  
}

