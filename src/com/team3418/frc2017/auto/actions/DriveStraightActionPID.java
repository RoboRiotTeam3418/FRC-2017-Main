package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class DriveStraightActionPID implements Action, PIDOutput {
	
	private double mPidRate;
	private PIDController mPIDController;
	private double mSetpoint;
	private double mAngleSetpoint;
	
	private Drivetrain mDrivetrain;
	private ADXRS450_Gyro mGyro;
	
	public DriveStraightActionPID(double distance) {
		mDrivetrain = Drivetrain.getInstance();
		mGyro = HardwareMap.getInstance().mGyro;
		
		mPIDController = new PIDController(.3, 0, 1, mDrivetrain.mRightEncoder, this);
		mPIDController.setOutputRange(-.6, .6);
		mPIDController.setAbsoluteTolerance(.5);
		
		mSetpoint = distance;
		mAngleSetpoint = mGyro.getAngle();
	}
	
	@Override
	public void start() {
		mPIDController.setSetpoint(mSetpoint + mDrivetrain.mRightEncoder.getDistance());
		mPIDController.enable();
	}
	
	@Override
	public void update() {
		mDrivetrain.setTankDriveSpeed(mPidRate, mPidRate + 0.06 * (mGyro.getAngle() - mAngleSetpoint));
	}
	
	@Override
	public boolean isFinished() {
		return mPIDController.onTarget();
	}
	
	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
		mPIDController.disable();
	}
	
	@Override
	public void pidWrite(double output) {
		mPidRate = output;
	}
}

