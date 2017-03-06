package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.Constants;
import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class TurnAction implements Action {

	private double mGyroSetpoint;
	private double mGyroCorrectionSpeed;

	private Drivetrain mDrivetrain;
	private ADXRS450_Gyro mGyro;
	
    public TurnAction(double angle) {
    	mDrivetrain = Drivetrain.getInstance();
    	mGyro = HardwareMap.getInstance().mGyro;
        mGyroSetpoint = mGyro.getAngle() + angle;
    }
    
    @Override
	public void start() {
		mDrivetrain.lowGear();
	}
    
    @Override
	public void update() {
    	calcGyroSpeed();
		mDrivetrain.setTankDriveSpeed(mGyroCorrectionSpeed, -mGyroCorrectionSpeed);
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
		return mGyroSetpoint - mGyro.getAngle();
	}
	
	private void calcGyroSpeed() {
		mGyroCorrectionSpeed = calcGyroError() * .05;
		if (mGyroCorrectionSpeed < Constants.kTurnMinSpeed && mGyroCorrectionSpeed > 0 ) {
			mGyroCorrectionSpeed = Constants.kTurnMinSpeed;
		}
		
		if (mGyroCorrectionSpeed > -Constants.kTurnMinSpeed && mGyroCorrectionSpeed < 0 ) {
			mGyroCorrectionSpeed = -Constants.kTurnMinSpeed;
		}
		
		if (mGyroCorrectionSpeed > Constants.kTurnMaxSpeed ) {
			mGyroCorrectionSpeed = Constants.kTurnMaxSpeed;
		}
		
		if (mGyroCorrectionSpeed < -Constants.kTurnMaxSpeed ) {
			mGyroCorrectionSpeed = -Constants.kTurnMaxSpeed;
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
