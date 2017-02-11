package com.team3418.frc2017;

import com.team3418.frc2017.subsystems.Agitator;
import com.team3418.frc2017.subsystems.Climber;
import com.team3418.frc2017.subsystems.Drivetrain;
import com.team3418.frc2017.subsystems.Intake;
import com.team3418.frc2017.subsystems.Shooter;
import com.team3418.frc2017.subsystems.Drivetrain.DriveGear;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	//initialize subsystems
	Agitator mAgitator;
	Climber mClimber;
	Drivetrain mDrivetrain;
	Intake mIntake;
	Shooter mShooter;
	
	//other parts of the robot
	ControlBoard mControlBoard;
	Accelerometer mAccelerometer;
	AnalogGyro mAnalogGyro;
	//I2C mI2c;
	
	
	
	public void updateAllSubsystems(){
		mAgitator.updateSubsystem();
		mClimber.updateSubsystem();
		mDrivetrain.updateSubsystem();
		mIntake.updateSubsystem();
		mShooter.updateSubsystem();
	}
	
	public void stopAllSubsystems(){
		mAgitator.stopAgitator();
		mClimber.stop();
		mDrivetrain.setTankDriveSpeed(0, 0);
		mIntake.stopIntakeRoller();
		mShooter.stopFeeder();
	}
	
	@Override
	public void robotInit() {
		mAgitator = new Agitator();
		mClimber = new Climber();
		mDrivetrain = new Drivetrain();
		mIntake = new Intake();
		mShooter = new Shooter();
		
		mAccelerometer = new ADXL345_I2C(Port.kOnboard,Range.k8G);
		mAnalogGyro = new AnalogGyro(0);
		//mI2c = new I2C(Port.kOnboard,84);
	}
	
	@Override
	public void autonomousInit() {
		stopAllSubsystems();
		updateAllSubsystems();
	}
	
	@Override
	public void autonomousPeriodic() {
		//All of this is still being tested, do not play with it unless you know what you're doing
		//Uncomment below to test the drive foreward function
		//driveForwardAuto();
		//austin is a dinugs
		
		
		
	}
		
		public void driveForwardAuto(){
			System.out.println("Oh geeze here we go!");
			mDrivetrain.LowGear();
			for(int x = 0; x != 1000; x++){
			mDrivetrain.setTankDriveSpeed(.1,.1);
			}
			System.out.println("I'M FINISHED!");
			System.out.println("I'M FINISHED!");
		}
		
		
		
		
	
	@Override
	public void disabledInit(){
		stopAllSubsystems();
		updateAllSubsystems();
	}
	
	@Override
	public void disabledPeriodic(){
		
	}
	
	@Override
	public void teleopInit(){
		stopAllSubsystems();
		updateAllSubsystems();
	}
	
	@Override
	public void teleopPeriodic() {
		
		mDrivetrain.setTankDriveSpeed(mControlBoard.getDriverLeftY(), mControlBoard.getDriverRightY());
		
		
		
		
		
		
	}
	
	@Override
	public void testInit(){
		stopAllSubsystems();
		updateAllSubsystems();
	}
	
	@Override
	public void testPeriodic() {
		
	}
}

