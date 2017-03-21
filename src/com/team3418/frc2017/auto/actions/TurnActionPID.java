package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class TurnActionPID implements Action, PIDOutput{

	private ADXRS450_Gyro mGyro;
	private Drivetrain mDrivetrain;
	
	private PIDController mPIDController;
	
	private double mPidRate;
	private double mSetpoint;
	
	public TurnActionPID(double angle) {
		mSetpoint = angle;
		
		mGyro = HardwareMap.getInstance().mGyro;
		mGyro.reset();
		
    	mPIDController = new PIDController(0.75, 0, 0.9, 0, mGyro, this);
    	mPIDController.setInputRange(-180, 180);
    	mPIDController.setOutputRange(-.3, .3);
    	mPIDController.setAbsoluteTolerance(1.0);
    	mPIDController.setContinuous(true);
		
	}

	@Override
	public void start() {
		double setPoint = mGyro.getAngle() + this.mSetpoint;
		if (setPoint > 180) setPoint -= 360;
    	if (setPoint < -180) setPoint += 360;
    	mPIDController.setSetpoint(setPoint);
    	mPIDController.enable();
	}

	@Override
	public void update() {
		mDrivetrain.setTankDriveSpeed(mPidRate, -mPidRate);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
	
	@Override
	public void done() {
		mPIDController.disable();
		mDrivetrain.setTankDriveSpeed(0, 0);
	}
	
	@Override
	public void pidWrite(double output) {
		final double min = .2;
		if (output < min && output > 0) {
			mPidRate = min;
		} else if (output > -min && output < 0) {
			mPidRate = -min;
		} else {
			mPidRate = output;
		}
	}
}