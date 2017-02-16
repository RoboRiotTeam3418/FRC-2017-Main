package com.team3418.frc2017;

import com.team3418.frc2017.plugins.Pipeline;
import com.team3418.frc2017.plugins.Vision;
import com.team3418.frc2017.subsystems.Agitator;
import com.team3418.frc2017.subsystems.Climber;
import com.team3418.frc2017.subsystems.Drivetrain;
import com.team3418.frc2017.subsystems.Intake;
import com.team3418.frc2017.subsystems.Shooter;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;


public class Robot extends IterativeRobot {
	//initalize main parts of the robot
	HardwareMap mHardwareMap;
	ControlBoard mControlBoard;
		
	//initialize subsystems
	Agitator mAgitator;
	Climber mClimber;
	Drivetrain mDrivetrain;
	Intake mIntake;
	Shooter mShooter;
	
	Vision mGearVision;
	Vision mShooterVision;
	
	UsbCamera mGearCamera;
	UsbCamera mShooterCamera;
	
	Pipeline mPipeline;
	
	int x, y;
	
	public void updateAllSubsystems(){
		mAgitator.updateSubsystem();
		mClimber.updateSubsystem();
		mDrivetrain.updateSubsystem();
		mIntake.updateSubsystem();
		mShooter.updateSubsystem();
	}
	
	public void stopAllSubsystems(){
		mAgitator.stop();
		mClimber.stop();
		mDrivetrain.stop();
		mDrivetrain.highGear();
		mIntake.stop();
		mShooter.stopFeeder();
		mShooter.stop();
	}
	
	@Override
	public void robotInit() {
		
		
		mHardwareMap = HardwareMap.getInstance();
		mControlBoard = ControlBoard.getInstance();
		
		mAgitator = Agitator.getInstance();
		mClimber = Climber.getInstance();
		mDrivetrain = Drivetrain.getInstance();
		mIntake = Intake.getInstance();
		mShooter = Shooter.getInstance();
		
		mPipeline = new Pipeline();
		
		mGearCamera = new UsbCamera("GearCamera", 0);
		mShooterCamera = new UsbCamera("ShooterCamera", 1);
		
		mGearVision = new Vision("Gear_Vision", mGearCamera, mPipeline, 320, 240, 30);
		mGearVision.setCameraParameters(20, 4000, 0);
		
		mShooterVision = new Vision("ShooterVision", mShooterCamera, mPipeline, 320, 240, 30);
		mShooterVision.setCameraParameters(20, 4000, 0);
		
		mGearVision.startVision();
		mShooterVision.startVision();
		
		stopAllSubsystems();
	}
	
	@Override
	public void autonomousInit() {
		stopAllSubsystems();
		updateAllSubsystems();
		x = 0;
		y = 0;
	}
	
	@Override
	public void autonomousPeriodic() {
		
		
		//All of this is still being tested, do not play with it unless you know what you're doing
		//Uncomment below to test the drive foreward function
		/*
		int x, y;
		System.out.println("Oh geeze here we go!");
		for(x = 0; x != 1000; x++){driveForwardAuto(); System.out.println(x);}
		System.out.println("I'M FINISHED DRIVING!");
		for(y = 0; y != 500; y++){turnRightAuto(); System.out.println(y);}
		System.out.println("I'M FINISHED TURNING!");
		mDrivetrain.setTankDriveSpeed(0, 0);
		*/

		if (x < 100){
			driveForwardAuto();
			System.out.println(x);
			x++;
		} else if (y < 100){
			turnRightAuto();
			System.out.println(y);
			y++;
		}
		
		
		
	}
		
	public void driveForwardAuto(){
		mDrivetrain.lowGear();
		mDrivetrain.setArcadeDriveSpeed(.75, 0);
	}
	
	public void turnRightAuto(){
		mDrivetrain.lowGear();
		mDrivetrain.setArcadeDriveSpeed(0, .75);
	}
	
	@Override
	public void disabledInit(){
		stopAllSubsystems();
		updateAllSubsystems();
		mGearVision.stopVision();
		mShooterVision.stopVision();
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
		
		
		//intake
		if(mControlBoard.getSecondaryIntakeButton()) {
			mIntake.intake();
		} else {
			mIntake.stop();
		}
		
		//---------------------------------------------------------------
		
		//climber
		/*
		if (mControlBoard.getDriverClimberAxis()) {
			mClimber.forward();
		} else if (mControlBoard.getDriverClimberHoldButton()) {
			mClimber.hold();
		} else {
			mClimber.stop();
		}
		*/
		
		
		if (mControlBoard.getClimberForwardButton()) {
			mClimber.forward();
		} else if (mControlBoard.getClimberHoldButton()) {
			mClimber.hold();
		} else {
			mClimber.stop();
		}
		
		

		//-----------------------------------------------------------------
		
		//shooter
		if (mControlBoard.getSecondaryShootButton()) {
			mShooter.shoot();
		} else {
			mShooter.stop();
		}
		//---------------------------------------------------------------

		//  Drive train 
		if(mControlBoard.getDriverHighGearButton()) {
			mDrivetrain.highGear();
		}
		if(mControlBoard.getDriverLowGearButton()) {
			mDrivetrain.lowGear();
		}
		
		mDrivetrain.setTankDriveSpeed(mControlBoard.getDriverLeftY(), mControlBoard.getDriverRightY());
		
		if (mControlBoard.getDriverPov() > -1){
			switch(mControlBoard.getDriverPov()){
			//--------------------------------------------------
			case 0: //forwards
				mDrivetrain.setArcadeDriveSpeed(.5, 0);
				break;
			case 180:// back
				mDrivetrain.setArcadeDriveSpeed(-.5, 0);
				break;
			//--------------------------------------------------
			case 45: // forwards / right
				mDrivetrain.setArcadeDriveSpeed(.5, .25);
				break;
			case 315: // forwards / left
				mDrivetrain.setArcadeDriveSpeed(.5, -.25);
				break;
			//--------------------------------------------------
			case 90:// right
				mDrivetrain.setArcadeDriveSpeed(0, .5);
				break;
			case 270: //left
				mDrivetrain.setArcadeDriveSpeed(0, -.5);
				break;
			//--------------------------------------------------
			case 135:// back / right
				mDrivetrain.setArcadeDriveSpeed(-.5, -.25);
				break;
			case 225:// back / left
				mDrivetrain.setArcadeDriveSpeed(-.5, .25);
				break;
			//--------------------------------------------------
			}
		}
		//---------------------------------------------------
		
		
		
		
		
		//System.out.println(Math.random());
		
		
		updateAllSubsystems();
	}
	
	@Override
	public void testInit( ){
		stopAllSubsystems();
		updateAllSubsystems();
	}
	
	@Override
	public void testPeriodic() {
		
	}
}

