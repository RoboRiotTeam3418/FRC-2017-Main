package com.team3418.frc2017;


import com.team3418.frc2017.auto.AutoExecuter;
import com.team3418.frc2017.auto.modes.LeftGearExitLeftMode;
import com.team3418.frc2017.plugins.MinionVision;
import com.team3418.frc2017.subsystems.Agitator;
import com.team3418.frc2017.subsystems.Climber;
import com.team3418.frc2017.subsystems.Drivetrain;
import com.team3418.frc2017.subsystems.Intake;
import com.team3418.frc2017.subsystems.Shooter;

import edu.wpi.first.wpilibj.IterativeRobot;


public class Robot extends IterativeRobot {
	//initalize main parts of the robot
	HardwareMap mHardwareMap;
	ControlBoard mControlBoard;
	SmartDashboardInteractions mSmartDashboardInteractions;
	MinionVision mMinionVision;
	
	//initialize subsystems
	Agitator mAgitator;
	Climber mClimber;
	Drivetrain mDrivetrain;
	Intake mIntake;
	Shooter mShooter;
	
	AutoExecuter mAutoExecuter = null;
	
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
		mSmartDashboardInteractions = new SmartDashboardInteractions();
		mMinionVision = MinionVision.getInstance();
		mMinionVision.startVision();
		
		mAgitator = Agitator.getInstance();
		mClimber = Climber.getInstance();
		mDrivetrain = Drivetrain.getInstance();
		mIntake = Intake.getInstance();
		mShooter = Shooter.getInstance();
		
		mSmartDashboardInteractions.initWithDefaults();
		
		stopAllSubsystems();
	}
	
	@Override
	public void autonomousInit() {
		if (mAutoExecuter != null) {
            mAutoExecuter.stop();
        }
        mAutoExecuter = null;
        
        
        mAutoExecuter = new AutoExecuter();
        mAutoExecuter.setAutoRoutine(mSmartDashboardInteractions.getSelectedAutonMode());
        //mAutoExecuter.setAutoRoutine(new LeftGearExitLeftMode());
        mAutoExecuter.start();
		
		stopAllSubsystems();
		updateAllSubsystems();
		
	}
	
	
	@Override
	public void autonomousPeriodic() {
		
		updateAllSubsystems();
		
	}

	@Override
	public void disabledInit(){
		
		if (mAutoExecuter != null) {
            mAutoExecuter.stop();
        }
        mAutoExecuter = null;
        
        //mMinionVision.stopVision();
        mDrivetrain.resetEncoders();
		
		stopAllSubsystems();
		updateAllSubsystems();
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
        
        //mMinionVision.startVision();
        
		stopAllSubsystems();
		mDrivetrain.lowGear();
		updateAllSubsystems();
	}
	
	@Override
	public void teleopPeriodic() {
		//System.out.println("target width = " + mMinionVision.getCombinedTargetWidth() + " target distance = " + mMinionVision.getTargetDistanceFromCamera());
		
		//intake
		if(mControlBoard.getSecondaryIntakeButton()) {
			mIntake.intake();
		} else {
			mIntake.stop();
		}
		
		
		
		
		//---------------------------------------------------------------
		
		
		if (mControlBoard.getClimberForwardButton()) {
			mClimber.forward();
		} else if (mControlBoard.getClimberHoldButton()) {
			mClimber.hold();
		} else if (mControlBoard.getClimberReverseButton()) {
			mClimber.reverse();
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
		
		
		
		
		//System.out.println("ADXR_D = " + mHardwareMap.mGyro.getAngle());
		
		//System.out.println(Math.random());
		
		updateAllSubsystems();
		
		/*
		if (mControlBoard.getDriverRightTrigger() > .2){
			mIntake.setRollerSpeed(mControlBoard.getDriverRightTrigger());
		}
		
		if (mControlBoard.getDriverIncreaseSpeed()) {
			mShooter.setTargetShooterRpm((mShooter.getTargetShooterRpm()+1));
			System.out.println(mShooter.getTargetShooterRpm());
		} else if (mControlBoard.getDriverDecreaseSpeed()) {
			mShooter.setTargetShooterRpm((mShooter.getTargetShooterRpm()-1));
			System.out.println(mShooter.getTargetShooterRpm());
		}
		*/
		
		
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

