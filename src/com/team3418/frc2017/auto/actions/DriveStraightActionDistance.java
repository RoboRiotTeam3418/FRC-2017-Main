package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.Constants;
import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;

public class DriveStraightActionDistance implements Action {
	
	private double mDistanceSetPoint;
	private double mGyroSetpoint;
	private double mEncoderCorrectionSpeed;
	private double mGyroCorrectionSpeed;

	private Drivetrain mDrivetrain;
	private Encoder mEncoder;
	private ADXRS450_Gyro mGyro;
	
    public DriveStraightActionDistance(double distance) {
    	mDrivetrain = Drivetrain.getInstance();
    	mEncoder = Drivetrain.getInstance().mRightEncoder;
    	mGyro = HardwareMap.getInstance().mGyro;
    	
    	mDistanceSetPoint = distance;
        mGyroSetpoint = mGyro.getAngle();
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
		mDrivetrain.setTankDriveSpeed(mEncoderCorrectionSpeed + mGyroCorrectionSpeed, mEncoderCorrectionSpeed + -mGyroCorrectionSpeed);
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
		return mGyroSetpoint - mGyro.getAngle();
	}
	
	private double calcEncoderError(){
		return mDistanceSetPoint - mEncoder.getDistance();
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
	
	private void calcEncoderSpeed() {
		mEncoderCorrectionSpeed = calcEncoderError() * .5;
		if (mEncoderCorrectionSpeed < Constants.kEncoderMinSpeed && mEncoderCorrectionSpeed > 0 ) {
			mEncoderCorrectionSpeed = Constants.kEncoderMinSpeed;
		}
		
		if (mEncoderCorrectionSpeed > -Constants.kEncoderMinSpeed && mEncoderCorrectionSpeed < 0 ) {
			mEncoderCorrectionSpeed = -Constants.kEncoderMinSpeed;
		}
		
		if (mEncoderCorrectionSpeed > Constants.kEncoderMaxSpeed) {
			mEncoderCorrectionSpeed = Constants.kEncoderMaxSpeed;
		}
		
		if (mEncoderCorrectionSpeed < -Constants.kEncoderMaxSpeed) {
			mEncoderCorrectionSpeed = -Constants.kEncoderMaxSpeed;
		}
	}
	
	private boolean isGyroOnTarget() {
		if (calcGyroError() < Constants.kGyroDeadzone && calcGyroError() > -Constants.kGyroDeadzone){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isEncoderOnTarget() {
		if (calcEncoderError() < Constants.kEncoderDeadzone && calcEncoderError() > -Constants.kEncoderDeadzone) {
			System.out.println("encoder is on target");
			return true;
		} else {
			return false;
		}
	}
}
