package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.plugins.MinionVision;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class CameraAlign implements Action{
	
	private ADXRS450_Gyro mGyro = HardwareMap.getInstance().mGyro;
	private MinionVision mMinionVision = MinionVision.getInstance();
	private Drivetrain mDrivetrain = Drivetrain.getInstance();
	
	// 10 degrees per 88 pixels at 3' from target (yes it's not perfect but it works)
	private final double mCameraFovDegrees = 60;
	private final double mCameraWidthPixels = 320;
	
	private state mState = state.CALC_FIRST_ERROR;
	private boolean finished = false;

	private double mRotationalMaxSpeed = .45;
	private double mRotationalMinSpeed = .35;
	private double mRotationalDeadzone = 1.5;
	
	private int errorCounts = 0;
	private int requiredErrorCounts = 50;
	
	private double mAngleSetpoint;
	private double mAngleCorrectionSpeed = 0;
	
	private enum state {
		CALC_FIRST_ERROR, CORRECT_FIRST_ERROR
	}
	
	public CameraAlign() {		
		mAngleSetpoint = 0;
		mGyro.reset();
	}

	@Override
	public void start() {
		
	}

	@Override
	public void update() {
		switch(mState) {
		case CALC_FIRST_ERROR:
			calcDegreesToPixel();
			mState = state.CORRECT_FIRST_ERROR;
			System.out.println("camera error correction completed");
			System.out.println("current angle is " + mGyro.getAngle() + " current setpoint is " + mAngleSetpoint);
			break;
		case CORRECT_FIRST_ERROR:
			calcGyroSpeed();
			mDrivetrain.setTankDriveSpeed(mAngleCorrectionSpeed, -mAngleCorrectionSpeed);
			if (isGyroOnTarget()) {
				finished = true;
			}
		}
	}

	@Override
	public boolean isFinished() {
		if (finished) {
			System.out.println("Camera Align Action Completed");
			return true;
		}
		return false;
	}

	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
	}
	
	
	private void calcDegreesToPixel() {//returns degrees to turn for target correction
		mAngleSetpoint = (mMinionVision.getCombinedTargetX() - 160) * (mCameraFovDegrees / mCameraWidthPixels);
	}
	
	private double calcGyroError() {//calculates gyro error relative to setpoint
		return mAngleSetpoint - mGyro.getAngle();
	}
	
	private void calcGyroSpeed() {
		mAngleCorrectionSpeed = calcGyroError() * .25;
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
	
	private boolean isGyroOnTarget() {
		if( Math.abs(calcGyroError()) < mRotationalDeadzone) {
			errorCounts++;
		} else {
			errorCounts = 0;
		}
		if(errorCounts >= requiredErrorCounts) {
			return true;
		} else {
			return false;
		}
	}
}
