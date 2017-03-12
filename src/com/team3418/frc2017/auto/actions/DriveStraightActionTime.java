package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;

public class DriveStraightActionTime implements Action {
	
	private final double mLinearSpeed;
	private final double mRotationalMaxSpeed;
	private final double mRotationalMinSpeed;
	private final double mRotationalDeadzone;
	
	private boolean mIsForward;
	private double mTimeToWait;
	private double mStartTime;
	private double mCurrentTime;
	private double mAngleSetpoint;
	private double mAngleCorrectionSpeed;
	
	private Drivetrain mDrivetrain;
	private ADXRS450_Gyro mGyro;
	
    public DriveStraightActionTime(double time, boolean isForward) {
    	mDrivetrain = Drivetrain.getInstance();
    	mGyro = HardwareMap.getInstance().mGyro;
        mAngleSetpoint = mGyro.getAngle();
        
        mIsForward = isForward;
        mTimeToWait = time;
        mLinearSpeed = .7;
        mRotationalMaxSpeed = .5;
    	mRotationalMinSpeed = .03;
    	mRotationalDeadzone = .25;
    }
    
    public DriveStraightActionTime(double time, boolean isForward, double LinearSpeed, double RotationalMaxSpeed, double RotationalMinSpeed, double RotationalDeadzone) {
    	mDrivetrain = Drivetrain.getInstance();
    	mGyro = HardwareMap.getInstance().mGyro;
        mAngleSetpoint = mGyro.getAngle();
        
        mIsForward = isForward;
        mTimeToWait = time;
        mLinearSpeed = LinearSpeed;
        mRotationalMaxSpeed = RotationalMaxSpeed;
    	mRotationalMinSpeed = RotationalMinSpeed;
    	mRotationalDeadzone = RotationalDeadzone;
    }
    
    @Override
	public void start() {
		mDrivetrain.lowGear();
		mStartTime = Timer.getFPGATimestamp();
	}
    
    @Override
	public void update() {
    	calcGyroSpeed();    
    	mCurrentTime = Timer.getFPGATimestamp();
    	if (mIsForward) {
    		mDrivetrain.setTankDriveSpeed(mLinearSpeed + mAngleCorrectionSpeed, mLinearSpeed + -mAngleCorrectionSpeed);
    	} else {
    		mDrivetrain.setTankDriveSpeed(-mLinearSpeed + mAngleCorrectionSpeed, -mLinearSpeed + -mAngleCorrectionSpeed);
    	}
	}
    
    @Override
	public boolean isFinished() {
		if ((mCurrentTime - mStartTime) > mTimeToWait) {
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
		return mAngleSetpoint - mGyro.getAngle();
	}
	
	private void calcGyroSpeed() {
		mAngleCorrectionSpeed = calcGyroError() * .05;
		if (mAngleCorrectionSpeed < mRotationalMinSpeed && mAngleCorrectionSpeed > 0 ) {
			mAngleCorrectionSpeed = mRotationalMinSpeed;
		} else if (mAngleCorrectionSpeed > -mRotationalMinSpeed && mAngleCorrectionSpeed < 0 ) {
			mAngleCorrectionSpeed = -mRotationalMinSpeed;
		}
		
		if (mAngleCorrectionSpeed > mRotationalMaxSpeed ) {
			mAngleCorrectionSpeed = mRotationalMaxSpeed;
		} else if (mAngleCorrectionSpeed < -mRotationalMaxSpeed ) {
			mAngleCorrectionSpeed = -mRotationalMaxSpeed;
		}
	}
}
	