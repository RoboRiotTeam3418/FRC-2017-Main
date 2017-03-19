package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class TurnActionAngle implements Action {

	private Drivetrain mDrivetrain = Drivetrain.getInstance();
	private ADXRS450_Gyro mGyro = HardwareMap.getInstance().mGyro;
	
	private double mAngleSetpoint;
	private double mAngleCorrectionSpeed;
	
	private int mErrorCounts = 0;
	private int mRequiredErrorCounts = 50;
	
	private double mRotationalMaxSpeed = .65;
	private double mRotationalMinSpeed = .35;
	private double mRotationalDeadzone = 1.5;
	
    public TurnActionAngle(double angle) {
        mAngleSetpoint = mGyro.getAngle() + angle;
    }
    
    public TurnActionAngle(double angle, double rotationalDeadzone) {
        mAngleSetpoint = mGyro.getAngle() + angle;
        mRotationalDeadzone = rotationalDeadzone;
    }
    
    public TurnActionAngle(double angle, int requiredErrorCounts) {
        mAngleSetpoint = mGyro.getAngle() + angle;
        mRequiredErrorCounts = requiredErrorCounts;
    }
    
    public TurnActionAngle(double angle, double RotationalMaxSpeed, double RotationalMinSpeed, double RotationalDeadzone) {
        mAngleSetpoint = mGyro.getAngle() + angle;
        
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
    	calcGyroSpeed();
		mDrivetrain.setTankDriveSpeed(mAngleCorrectionSpeed, -mAngleCorrectionSpeed);
		//System.out.println("error = " + calcGyroError() + " deadzone is " + mRotationalDeadzone + " correction speed = " + mAngleCorrectionSpeed );
		System.out.println(mGyro.getAngle());
	}
    
    @Override
	public boolean isFinished() {
		if (isGyroOnTarget()) {
			return true;
		}
		return false;
	}
    
	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
		System.out.println("finished with turn (angle) action");
	}
	
	private double calcGyroError() {
		return mAngleSetpoint - mGyro.getAngle();
	}
	
	private void calcGyroSpeed() {
		mAngleCorrectionSpeed = calcGyroError() * .175;
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
