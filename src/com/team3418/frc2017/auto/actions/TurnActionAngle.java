package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class TurnActionAngle implements Action {

	private double mAngleSetpoint;
	private double mAngleCorrectionSpeed;
	private int errorCounts;
	private final int requiredErrorCounts;
	
	private final double allowableAngleError;
	private final double mRotationalMaxSpeed;
	private final double mRotationalMinSpeed;
	private final double mRotationalDeadzone;

	private Drivetrain mDrivetrain;
	private ADXRS450_Gyro mGyro;
	
    public TurnActionAngle(double angle) {
    	mDrivetrain = Drivetrain.getInstance();
    	mGyro = HardwareMap.getInstance().mGyro;
        mAngleSetpoint = mGyro.getAngle() + angle;
        
        mRotationalMaxSpeed = .5;
    	mRotationalMinSpeed = .03;
    	mRotationalDeadzone = .25;
    	errorCounts = 0;
    	requiredErrorCounts = 50;
    	allowableAngleError = 1;
    }
    
    public TurnActionAngle(double angle, double RotationalMaxSpeed, double RotationalMinSpeed, double RotationalDeadzone) {
    	mDrivetrain = Drivetrain.getInstance();
    	mGyro = HardwareMap.getInstance().mGyro;
        mAngleSetpoint = mGyro.getAngle() + angle;
        errorCounts = 0;
    	requiredErrorCounts = 50;
    	allowableAngleError = 1;
        
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
		System.out.println("error = " + calcGyroError() + " deadzone is " + mRotationalDeadzone + " correction speed = " + mAngleCorrectionSpeed );
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
		System.out.println("finished with turn action");
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
	
	private boolean isGyroOnTarget() {
		if( Math.abs(calcGyroError() - mGyro.getAngle()) < allowableAngleError)
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
