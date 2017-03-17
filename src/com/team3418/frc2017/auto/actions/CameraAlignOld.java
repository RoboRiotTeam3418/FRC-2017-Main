package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.plugins.MinionVision;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class CameraAlignOld implements Action{
	// 10 degrees per 88 pixels at 3'
	private final double mDegreesStart = 10;
	private final double mPixelStart = 88;
	
	
	private final int requiredErrorCounts;
	private final double mRotationalMaxSpeed;
	private final double mRotationalMinSpeed;
	private final double mRotationalDeadzone;
	
	private boolean finished;
	private state mState;
	private int errorCounts;
	private double mAngleSetpoint;
	private double mAngleCorrectionSpeed;
	
	
	private ADXRS450_Gyro mGyro;
	private MinionVision mMinionVision;
	private Drivetrain mDrivetrain;
	
	private enum state {
		CALC_FIRST_ERROR, CALC_SECOND_ERROR, CORRECT_FIRST_ERROR, CORRECT_SECOND_ERROR
	}
	
	public CameraAlignOld() {
		mGyro = HardwareMap.getInstance().mGyro;
		mGyro.reset();
		mMinionVision = MinionVision.getInstance();
		mDrivetrain = Drivetrain.getInstance();
		
		finished = false;
		errorCounts = 0;
		mAngleSetpoint = 0;
		mAngleCorrectionSpeed = 0;
		
		mRotationalMaxSpeed = .45;
    	mRotationalMinSpeed = .35;
    	mRotationalDeadzone = 1.5;
    	errorCounts = 0;
    	requiredErrorCounts = 50;
    	
    	mState = state.CALC_FIRST_ERROR;
	}

	@Override
	public void start() {
		
	}

	@Override
	public void update() {
		switch(mState) {
		case CALC_FIRST_ERROR:
			mAngleSetpoint = calcDegreesToPixel(mDegreesStart, mPixelStart, calcCameraError());
			mState = state.CORRECT_FIRST_ERROR;
			System.out.println("first error degree correction = " + calcDegreesToPixel(mDegreesStart, mPixelStart, calcCameraError()));
			break;
		case CORRECT_FIRST_ERROR:
			calcGyroSpeed();
			mDrivetrain.setTankDriveSpeed(mAngleCorrectionSpeed, -mAngleCorrectionSpeed);
			System.out.println(mAngleCorrectionSpeed);
			if (isGyroOnTarget()) {
				finished = true;
				mState = state.CALC_SECOND_ERROR;
			}
			break;
		case CALC_SECOND_ERROR:
			mGyro.reset();
			mAngleSetpoint = calcDegreesToPixelCorrected(mDegreesStart, mPixelStart, calcCameraError());
			mState = state.CORRECT_SECOND_ERROR;
			System.out.println("second error degree correction = " + calcDegreesToPixelCorrected(mDegreesStart, mPixelStart, calcCameraError()));
			break;
		case CORRECT_SECOND_ERROR:
			calcGyroSpeed();
			mDrivetrain.setTankDriveSpeed(mAngleCorrectionSpeed, -mAngleCorrectionSpeed);
			if (isGyroOnTarget()) {
				finished = true;
				System.out.println("finished");
			}
			break;
		}
	}

	@Override
	public boolean isFinished() {
		if (finished) {
			return true;
		}
		return false;
	}

	@Override
	public void done() {
		
	}
	
	private double calcCameraError() {//in pixels
		return mMinionVision.getCombinedTargetX()-160;
	}
	
	private double calcDegreesToPixelCorrected(double degrees, double pixels, double pixelError) {// returns degrees to correct for original overshot
		return ((degrees / pixels + Math.abs(pixelError)) * pixelError);
	}
	
	private double calcDegreesToPixel(double degrees, double pixels, double pixelError) {//returns degrees to turn for target correction
		return ((degrees / pixels) * pixelError);
	}
	
	private double calcGyroError() {//calculates gyro error relative to degree setpoint
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
		if( Math.abs(calcGyroError()) < mRotationalDeadzone)
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
}
