package com.team3418.frc2017;

import com.team3418.frc2017.auto.AutoExecuter;
import com.team3418.frc2017.auto.AutoRoutine;
import com.team3418.frc2017.auto.actions.Action;
import com.team3418.frc2017.auto.actions.DriveStraightAction;
import com.team3418.frc2017.plugins.Pipeline;
import com.team3418.frc2017.plugins.Vision;
import com.team3418.frc2017.subsystems.Agitator;
import com.team3418.frc2017.subsystems.Climber;
import com.team3418.frc2017.subsystems.Drivetrain;
import com.team3418.frc2017.subsystems.Intake;
import com.team3418.frc2017.subsystems.Shooter;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


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
	
	AutoExecuter mAutoExecuter = null;
	
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
		mDrivetrain.lowGear();
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
		
		/*
		mGearVision = new Vision("Gear_Vision", mGearCamera, mPipeline, 320, 240, 30);
		mGearVision.setCameraParameters(20, 4000, 0);
		
		mShooterVision = new Vision("ShooterVision", mShooterCamera, mPipeline, 320, 240, 30);
		mShooterVision.setCameraParameters(20, 4000, 0);
		
		mGearVision.startVision();
		mShooterVision.startVision();
		*/
		stopAllSubsystems();
	}
	
	@Override
	public void autonomousInit() {
		if (mAutoExecuter != null) {
            mAutoExecuter.stop();
        }
        mAutoExecuter = null;
        
        
        mAutoExecuter = new AutoExecuter();
        mAutoExecuter.setAutoRoutine(new AutoRoutine());
        mAutoExecuter.start();
		
		stopAllSubsystems();
		updateAllSubsystems();
		
		
		x = 0;
		y = 0;
	}
	
	
	@Override
	public void autonomousPeriodic() {
		
		
	}

	@Override
	public void disabledInit(){
		
		if (mAutoExecuter != null) {
            mAutoExecuter.stop();
        }
        mAutoExecuter = null;
		
		stopAllSubsystems();
		updateAllSubsystems();
		//mGearVision.stopVision();
		//mShooterVision.stopVision();
	}
	
	@Override
	public void disabledPeriodic(){
		
	}
	
	@Override
	public void teleopInit(){
		
		if (mAutoExecuter != null) {
            mAutoExecuter.stop();
        }
        mAutoExecuter = null;
        
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
		SmartDashboard.putNumber("Left_Drivetrain_Encoder_Distance", mHardwareMap.mLeftDrivetrainEncoder.getDistance());
		SmartDashboard.putNumber("Right_Drivetrain_Encoder_Distance", mHardwareMap.mRightDrivetrainEncoder.getDistance());
		
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

