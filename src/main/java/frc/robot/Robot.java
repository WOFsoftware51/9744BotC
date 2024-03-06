// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  
  private final XboxController driver = new XboxController(0);
  private final XboxController operator = new XboxController(1);
  private final Timer m_timer = new Timer();
  private CANSparkMax m_leftMotor;
  private CANSparkMax m_rightMotor;
  private CANSparkMax m_leftMotor2;
  private CANSparkMax m_rightMotor2;
  private CANSparkMax m_shooter1;
  private CANSparkMax m_shooter2;
  private CANSparkMax m_shooter3;
  private CANSparkMax m_floorIntake;
  private CANSparkMax m_hanger;
  private DifferentialDrive m_myRobot;


  public Robot() {

  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_leftMotor = new CANSparkMax(1, MotorType.kBrushed);
    m_rightMotor = new CANSparkMax(3, MotorType.kBrushed);
    m_leftMotor2 = new CANSparkMax(2, MotorType.kBrushed);
    m_rightMotor2 = new CANSparkMax(4, MotorType.kBrushed);
    m_shooter1 = new CANSparkMax(5, MotorType.kBrushed);
    m_shooter2 = new CANSparkMax(6, MotorType.kBrushed);
    m_shooter3 = new CANSparkMax(7, MotorType.kBrushed);
    m_floorIntake = new CANSparkMax(8, MotorType.kBrushed);
    m_hanger = new CANSparkMax(9, MotorType.kBrushed);

    m_leftMotor.restoreFactoryDefaults();
    m_rightMotor.restoreFactoryDefaults();
    m_leftMotor2.restoreFactoryDefaults();
    m_rightMotor2.restoreFactoryDefaults();

   // m_rightMotor.setInverted(true);
    m_leftMotor.setInverted(true);
    m_leftMotor2.follow(m_leftMotor);
    m_rightMotor2.follow(m_rightMotor);

    m_myRobot = new DifferentialDrive(m_leftMotor, m_rightMotor);


  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    m_timer.restart();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    if(m_timer.get() > 0.5) { //Charges up
      m_shooter3.set(1.0);
      // m_shooter2.set(1.0); //TODO maybe?
    }

    if(m_timer.get() > 1.5) { //Shoots
      m_shooter1.set(1.0);
      m_shooter2.set(1.0);
    }

    if(m_timer.get() > 4){
      m_myRobot.curvatureDrive(0.3, 0,true);
    }

    if(m_timer.get() > 10){
      m_myRobot.curvatureDrive(0, 0,true);
      m_shooter1.set(0.0);
      m_shooter2.set(0.0);
      m_shooter3.set(0.0);
    }

  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
    //Boost
    if(driver.getRightBumper()) {
        m_myRobot.curvatureDrive(-driver.getLeftY(), -driver.getRightX()*0.3,true);
    }
    else {
        m_myRobot.curvatureDrive(-driver.getLeftY()*0.5, -driver.getRightX()*0.3,true);
    }


    //Brake
    if(driver.getLeftTriggerAxis()>0.8) {
      m_leftMotor.setIdleMode(IdleMode.kBrake);
      m_rightMotor.setIdleMode(IdleMode.kBrake);
      m_leftMotor2.setIdleMode(IdleMode.kBrake);
      m_rightMotor2.setIdleMode(IdleMode.kBrake);
    }
    else {
      m_leftMotor.setIdleMode(IdleMode.kCoast);
      m_rightMotor.setIdleMode(IdleMode.kCoast);
      m_leftMotor2.setIdleMode(IdleMode.kCoast);
      m_rightMotor2.setIdleMode(IdleMode.kCoast);
    }
    

    //Speaker Shooter
    if(operator.getRightTriggerAxis()>0.8) { //Shoot
      m_shooter2.set(-1.0);
    }
    else {
      m_shooter2.set(0.0);
    }

    
    //Floor intake
    if(operator.getRightBumper()) { //Intake
      m_floorIntake.set(-0.8);
      m_shooter1.set(-0.8);
    }
    else if(operator.getLeftBumper()){ //Spit
      m_floorIntake.set(0.8);
      m_shooter1.set(0.8);
    }
    else {
      m_floorIntake.set(0.0);
      m_shooter1.set(0.0);
    }
    
      

    //Hanger TODO
    if(operator.getXButton()){
      m_hanger.set(0.5); //TEST
    }
    else if(operator.getYButton()){
      m_hanger.set(-0.5);
    }
    else{
      m_hanger.set(0.0);
    }


    if(operator.getAButton()){ //Charge
      m_shooter3.set(-1.0);
    }
    else{
      m_shooter3.set(0.0);
    }

  }



  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
