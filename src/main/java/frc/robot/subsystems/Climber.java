/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends SubsystemBase {
  private WPI_TalonSRX extensionMotor = new WPI_TalonSRX(ClimberConstants.EXTENSION_MOTOR);
  private WPI_TalonFX leftMotor1 = new WPI_TalonFX(ClimberConstants.LEFT_MOTOR);
  private WPI_TalonFX rightMotor1 = new WPI_TalonFX(ClimberConstants.RIGHT_MOTOR);
  private Solenoid ratchet = new Solenoid(ClimberConstants.RATCHED_SOLENOID_CHANNEL);
  /**
   * Creates a new Climber.
   */
  public Climber() {
    
    leftMotor1.configFactoryDefault();
    rightMotor1.configFactoryDefault();
    extensionMotor.configFactoryDefault();
    extensionMotor.configContinuousCurrentLimit(6);
    extensionMotor.setNeutralMode(NeutralMode.Coast);

    leftMotor1.configOpenloopRamp(.25);
    rightMotor1.configOpenloopRamp(.25);

    leftMotor1.setInverted(true);
    rightMotor1.setInverted(true);
    extensionMotor.setInverted(false);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void extend(double velocity){
    extensionMotor.set(ControlMode.PercentOutput,velocity*.60);
    leftMotor1.set(ControlMode.PercentOutput,-velocity);
    rightMotor1.set(ControlMode.PercentOutput,-velocity);
  }

  public void retract(double velocity){
    leftMotor1.set(ControlMode.PercentOutput,velocity);
    rightMotor1.set(ControlMode.PercentOutput,velocity);
    extensionMotor.set(ControlMode.PercentOutput,.05);
  }

  public void hold(){
    extensionMotor.setNeutralMode(NeutralMode.Brake);
    leftMotor1.setNeutralMode(NeutralMode.Brake);
    rightMotor1.setNeutralMode(NeutralMode.Brake);
    leftMotor1.set(ControlMode.PercentOutput,0);
    rightMotor1.set(ControlMode.PercentOutput,0);
    extensionMotor.set(ControlMode.PercentOutput,0);
  }
  public void roll(double velocity){
    leftMotor1.set(ControlMode.PercentOutput,velocity);
    rightMotor1.set(ControlMode.PercentOutput,-velocity);

  }
}
//List of parts: Two Talon SRX's Mag encoder, Rahcet release, Parmeter, Velocity, wpilib import components. Liam