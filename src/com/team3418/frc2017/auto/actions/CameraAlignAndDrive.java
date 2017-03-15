package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.plugins.MinionVision;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;


public class CameraAlignAndDrive implements Action {
	
	private int errorCounts;
	
	private final int requiredErrorCounts;
	
	private double mStartTime;
	private double mTimeElapsed;
	private double mTimeToWait;
	
	private final double mLinearSpeed;
	
	private final double mCameraMaxSpeed;
	private final double mCameraMinSpeed;
	private final double mCameraDeadzone;
	
	private double mCameraCorrectionSpeed;
	
	private final double mRotationalMaxSpeed;
	private final double mRotationalMinSpeed;
	private final double mRotationalDeadzone;
	
	private boolean mReadyToDrive;
	
	private double mAngleSetpoint;
	private double mAngleCorrectionSpeed;
	private ADXRS450_Gyro mGyro;
	
	private MinionVision mMinionVision;
	private Drivetrain mDrivetrain;
	
    public CameraAlignAndDrive(double timetowait) {
    	mMinionVision = MinionVision.getInstance();
    	mDrivetrain = Drivetrain.getInstance();
    	mGyro = HardwareMap.getInstance().mGyro;
    	
    	mTimeToWait = timetowait;
    	
    	errorCounts = 0;
    	requiredErrorCounts = 50;
    	
    	mLinearSpeed = -.5;
    	
    	mCameraMaxSpeed = .45;
    	mCameraMinSpeed = .35;
    	mCameraDeadzone = 5;
    	
    	mRotationalMaxSpeed = .35;
    	mRotationalMinSpeed = .15;
    	mRotationalDeadzone = 0;
    }
    
    @Override
	public void start() {
		mDrivetrain.lowGear();
	}
    
    @Override
	public void update() {
    	calcCameraSpeed();
    	calcGyroSpeed();
    	System.out.println(isCameraOnTarget());
    	System.out.println("camera correction speed = " + mCameraCorrectionSpeed);
    	System.out.println("time elapsed = " + mTimeElapsed + " target time = " + mTimeToWait);
    	if (isCameraOnTarget()) {
    		mReadyToDrive = true;
    	}
    	
    	if (mReadyToDrive == true) {
    		/*
    		mDrivetrain.setTankDriveSpeed(mLinearSpeed + mAngleCorrectionSpeed, mLinearSpeed + -mAngleCorrectionSpeed);
    		mTimeElapsed = Timer.getFPGATimestamp() - mStartTime;
    		*/
    	} else {
    		mDrivetrain.setTankDriveSpeed(mCameraCorrectionSpeed,-mCameraCorrectionSpeed);
    		mStartTime = Timer.getFPGATimestamp();
    		mTimeElapsed = 0;
    		mAngleSetpoint = mGyro.getAngle();
    	}
	}
    
    @Override
	public boolean isFinished() {
		if ((mTimeElapsed) > mTimeToWait) {
			return true;
		}
		return false;
	}
    
	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
		System.out.println("finished with turn (camera) action");
	}
	
	private double calcCameraError() {
		return mMinionVision.getCombinedTargetX()-160;
	}
	
	private void calcCameraSpeed() {
		mCameraCorrectionSpeed = calcCameraError() * .013;
		if (mCameraCorrectionSpeed < mCameraMinSpeed && mCameraCorrectionSpeed > 0 ) {
			mCameraCorrectionSpeed = mCameraMinSpeed;
		} else if (mCameraCorrectionSpeed > -mCameraMinSpeed && mCameraCorrectionSpeed < 0 ) {
			mCameraCorrectionSpeed = -mCameraMinSpeed;
		}
		
		if (mCameraCorrectionSpeed > mCameraMaxSpeed ) {
			mCameraCorrectionSpeed = mCameraMaxSpeed;
		} else if (mCameraCorrectionSpeed < -mCameraMaxSpeed ) {
			mCameraCorrectionSpeed = -mCameraMaxSpeed;
		}
	}
	
	private boolean isCameraOnTarget() {
		if( Math.abs(calcCameraError()) < mCameraDeadzone)
		{
			//Increase the number counts within the error
			errorCounts++;
		} else {
			//We're not there yet, so reset the counter
			errorCounts = 0;
		}
		//If we've been within the error for long enough...
		if(errorCounts >= requiredErrorCounts)
		{
			//We're done!
			return true;
		} else {
			return false;
		}
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
