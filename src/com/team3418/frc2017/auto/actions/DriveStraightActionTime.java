package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.Constants;
import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;

public class DriveStraightActionTime implements Action {
	
	private double driveSpeed;
	private double mGyroSetpoint;
	private double mGyroCorrectionSpeed;
	
	private double mTimeToWait;
	private double mStartTime;
	private double mCurrentTime;
	
	private Drivetrain mDrivetrain;
	private ADXRS450_Gyro mGyro;
	
	
	
    public DriveStraightActionTime(double time, boolean isForward) {
    	mDrivetrain = Drivetrain.getInstance();
    	mGyro = HardwareMap.getInstance().mGyro;
        mGyroSetpoint = mGyro.getAngle();
        
        if (isForward) {
        	driveSpeed = Constants.kTimedDriveSpeed;
        } else {
        	driveSpeed = -Constants.kTimedDriveSpeed;
        }
        
        
        mTimeToWait = time;
        
        
    }
    
    @Override
	public void start() {
		mDrivetrain.highGear();
		mStartTime = Timer.getFPGATimestamp();
		
	}
    
    @Override
	public void update() {
    	calcGyroSpeed();    
    	mCurrentTime = Timer.getFPGATimestamp();
		mDrivetrain.setTankDriveSpeed(driveSpeed + mGyroCorrectionSpeed, driveSpeed + -mGyroCorrectionSpeed);
	}
    
    @Override
	public boolean isFinished() {
		if (((mCurrentTime - mStartTime) > mTimeToWait)) {
			return true;
		}
		return false;
	}
    
	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
		mDrivetrain.resetEncoders();
		System.out.println("finished with drive straight (timed) action");
	}
	
	private double calcGyroError() {
		return mGyroSetpoint - mGyro.getAngle();
	}
	
	private void calcGyroSpeed() {
		mGyroCorrectionSpeed = calcGyroError() * .05;
		if (mGyroCorrectionSpeed < Constants.kGyroMinSpeed && mGyroCorrectionSpeed > 0 ) {
			mGyroCorrectionSpeed = Constants.kGyroMinSpeed;
		}
		
		if (mGyroCorrectionSpeed > -Constants.kGyroMinSpeed && mGyroCorrectionSpeed < 0 ) {
			mGyroCorrectionSpeed = -Constants.kGyroMinSpeed;
		}
		
		if (mGyroCorrectionSpeed > Constants.kGyroMaxSpeed ) {
			mGyroCorrectionSpeed = Constants.kGyroMaxSpeed;
		}
		
		if (mGyroCorrectionSpeed < -Constants.kGyroMaxSpeed ) {
			mGyroCorrectionSpeed = -Constants.kGyroMaxSpeed;
		}
	}
	
	private boolean isGyroOnTarget() {
		if (calcGyroError() < Constants.kGyroDeadzone && calcGyroError() > -Constants.kGyroDeadzone){
			return true;
		} else {
			return false;
		}
	}
}
	