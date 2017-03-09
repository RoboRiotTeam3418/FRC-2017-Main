package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;

public class DriveStraightActionDistance implements Action {
	
	private double mDistanceSetPoint;
	private double mAngleSetpoint;
	private double mEncoderCorrectionSpeed;
	private double mAngleCorrectionSpeed;
	private int errorCounts;
	private final int requiredErrorCounts;
	
	private final double mLinearMaxSpeed;
	private final double mLinearMinSpeed;
	private final double mLinearDeadzone;
	private final double mRotationalMaxSpeed;
	private final double mRotationalMinSpeed;
	private final double mRotationalDeadzone;
	
	private Drivetrain mDrivetrain;
	private Encoder mEncoder;
	private ADXRS450_Gyro mGyro;
	
    public DriveStraightActionDistance(double distance) {
    	mDrivetrain = Drivetrain.getInstance();
    	mEncoder = Drivetrain.getInstance().mRightEncoder;
    	mGyro = HardwareMap.getInstance().mGyro;
    	
    	mDistanceSetPoint = distance;
    	mAngleSetpoint = mGyro.getAngle();
    	errorCounts = 0;
    	requiredErrorCounts = 50;
    	
    	mLinearMaxSpeed = .75;
    	mLinearMinSpeed = .28;
    	mLinearDeadzone = 1;
    	mRotationalMaxSpeed = .5;
    	mRotationalMinSpeed = .03;
    	mRotationalDeadzone = .25;
    }
    
    public DriveStraightActionDistance(double distance, double LinearMaxSpeed, double LinearMinSpeed, double LinearDeadzone, double RotationalMaxSpeed, double RotationalMinSpeed, double RotationalDeadzone) {
    	mDrivetrain = Drivetrain.getInstance();
    	mEncoder = Drivetrain.getInstance().mRightEncoder;
    	mGyro = HardwareMap.getInstance().mGyro;
    	
    	mDistanceSetPoint = distance;
    	mAngleSetpoint = mGyro.getAngle();
    	errorCounts = 0;
    	requiredErrorCounts = 50;
    	
    	mLinearMaxSpeed = LinearMaxSpeed;
    	mLinearMinSpeed = LinearMinSpeed;
    	mLinearDeadzone = LinearDeadzone;
    	mRotationalMaxSpeed = RotationalMaxSpeed;
    	mRotationalMinSpeed = RotationalMinSpeed;
    	mRotationalDeadzone = RotationalDeadzone;
    }
    
    @Override
	public void start() {
		mDrivetrain.lowGear();
	}
    
    @Override
	public void update() {
    	calcEncoderSpeed();
    	calcGyroSpeed();
    	System.out.println("drivetrain speed = " + mEncoderCorrectionSpeed);
		mDrivetrain.setTankDriveSpeed(mEncoderCorrectionSpeed + mAngleCorrectionSpeed, mEncoderCorrectionSpeed + -mAngleCorrectionSpeed);
	}
    
    @Override
	public boolean isFinished() {
		if (isEncoderOnTarget()) {
			return true;
		}
		return false;
	}
    
	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
		mDrivetrain.resetEncoders();
		System.out.println("finished with drive straight (distance) action");
	}
	
	private double calcGyroError() {
		return mAngleSetpoint - mGyro.getAngle();
	}
	
	private double calcEncoderError(){
		return mDistanceSetPoint - mEncoder.getDistance();
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
	
	private void calcEncoderSpeed() {
		mEncoderCorrectionSpeed = calcEncoderError() * .5;
		if (mEncoderCorrectionSpeed < mLinearMinSpeed && mEncoderCorrectionSpeed > 0 ) {
			mEncoderCorrectionSpeed = mLinearMinSpeed;
		} else if (mEncoderCorrectionSpeed > -mLinearMinSpeed && mEncoderCorrectionSpeed < 0 ) {
			mEncoderCorrectionSpeed = -mLinearMinSpeed;
		}
		
		if (mEncoderCorrectionSpeed > mLinearMaxSpeed) {
			mEncoderCorrectionSpeed = mLinearMaxSpeed;
		} else if (mEncoderCorrectionSpeed < -mLinearMaxSpeed) {
			mEncoderCorrectionSpeed = -mLinearMaxSpeed;
		}
	}
	
	private boolean isGyroOnTarget() {
		if (Math.abs(calcGyroError()) < mRotationalDeadzone){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isEncoderOnTarget() {
		if( Math.abs(calcEncoderError()) < mLinearDeadzone)
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
