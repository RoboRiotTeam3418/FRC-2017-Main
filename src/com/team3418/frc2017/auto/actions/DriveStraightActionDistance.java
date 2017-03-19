package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;

public class DriveStraightActionDistance implements Action {
	
	private Drivetrain mDrivetrain = Drivetrain.getInstance();
	private Encoder mEncoder = Drivetrain.getInstance().mRightEncoder;
	private ADXRS450_Gyro mGyro = HardwareMap.getInstance().mGyro;
	
	private double mDistanceSetPoint;
	private double mAngleSetpoint = mGyro.getAngle();
	private double mEncoderCorrectionSpeed;
	private double mAngleCorrectionSpeed;
	private int mErrorCounts = 0;
	private int mRequiredErrorCounts = 50;
	
	private double mLinearMaxSpeed = .7;
	private double mLinearMinSpeed = .28;
	private double mLinearDeadzone = 1;
	private double mRotationalMaxSpeed = .5;
	private double mRotationalMinSpeed = .03;	
	
    public DriveStraightActionDistance(double distance) {
    	mDistanceSetPoint = distance;
    }
    
    public DriveStraightActionDistance(double distance, double maxSpeed) {
    	mDistanceSetPoint = distance;
    	mLinearMaxSpeed = maxSpeed;
    }
    
    public DriveStraightActionDistance(double distance, double maxSpeed, double deadzone) {
    	mDistanceSetPoint = distance;
    	mLinearMaxSpeed = maxSpeed;
    	mLinearDeadzone = deadzone;
    }
    
    public DriveStraightActionDistance(double distance, double maxSpeed, int requiredErrorCounts) {
    	mDistanceSetPoint = distance;
    	mLinearMaxSpeed = maxSpeed;
    	mRequiredErrorCounts = requiredErrorCounts;
    }
    
    @Override
	public void start() {
		mDrivetrain.lowGear();
		mDrivetrain.resetEncoders();
	}
    
    @Override
	public void update() {
    	calcEncoderSpeed();
    	calcGyroSpeed();
    	System.out.println(calcGyroError());
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
	
	private boolean isEncoderOnTarget() {
		if(Math.abs(calcEncoderError()) < mLinearDeadzone) {
			mErrorCounts++;
		} else {
			mErrorCounts = 0;
		}
		
		if(mErrorCounts >= mRequiredErrorCounts) {
			return true;
		} else {
			return false;
		}
	}
}
