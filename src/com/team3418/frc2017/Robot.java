package com.team3418.frc2017;

import com.team3418.frc2017.auto.AutoExecuter;
import com.team3418.frc2017.auto.modes.DriveStraightMode;
import com.team3418.frc2017.subsystems.Agitator;
import com.team3418.frc2017.subsystems.Climber;
import com.team3418.frc2017.subsystems.Drivetrain;
import com.team3418.frc2017.subsystems.ITG3200Subsystem;
import com.team3418.frc2017.subsystems.Intake;
import com.team3418.frc2017.subsystems.Shooter;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;


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
	ITG3200Subsystem mITG3200Subsystem;
	
	private Encoder mRightDrivetrainEncoder;
	private Encoder mLeftDrivetrainEncoder;
	
	
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
		mDrivetrain.highGear();
		mIntake.stop();
		mShooter.stopFeeder();
		mShooter.stop();
		mITG3200Subsystem.resetGyroX();
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
		
		mITG3200Subsystem = ITG3200Subsystem.getInstance();
		mITG3200Subsystem.calibrateGyro();
		
		mLeftDrivetrainEncoder = mDrivetrain.mLeftEncoder;
		mRightDrivetrainEncoder = mDrivetrain.mRightEncoder;
        
		
		
		stopAllSubsystems();
	}
	
	@Override
	public void autonomousInit() {
		if (mAutoExecuter != null) {
            mAutoExecuter.stop();
        }
        mAutoExecuter = null;
        
        
        mAutoExecuter = new AutoExecuter();
        mAutoExecuter.setAutoRoutine(new DriveStraightMode());
        mAutoExecuter.start();
		
		stopAllSubsystems();
		updateAllSubsystems();

		
		/*all of Aidan's stupid stuff
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
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
        
        mDrivetrain.resetEncoders();
		
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
		mDrivetrain.lowGear();
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

