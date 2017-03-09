package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class TurnActionPID implements Action, PIDOutput {

	private HardwareMap mHardwareMap;
	private Drivetrain mDrivetrain;
	private PIDController controller;
	private double pidRate;
	private double setPoint;
	private final double mMinTurnSpeed;
	
	public TurnActionPID(double angle) {
		setPoint = angle;
		mMinTurnSpeed = .2;
		mDrivetrain = Drivetrain.getInstance();
		mHardwareMap = HardwareMap.getInstance();
		mHardwareMap.mGyro.reset();
		controller = new PIDController(0.75, 0, 0.9, 0, mHardwareMap.mGyro, this);
    	controller.setInputRange(-180, 180);
    	controller.setOutputRange(-.3, .3);
    	controller.setAbsoluteTolerance(1.0);
    	controller.setContinuous(true);
	}
	
	@Override
	public void start() {
		setPoint = mHardwareMap.mGyro.getAngle() + setPoint;
    	if (setPoint > 180) setPoint -= 360;
    	if (setPoint < -180) setPoint += 360;
    	controller.setSetpoint(setPoint);
    	controller.enable();
    	}

	@Override
	public void update() {
		mDrivetrain.setTankDriveSpeed(pidRate, -pidRate);
	}

	@Override
	public boolean isFinished() {
		return controller.onTarget();
	}

	@Override
	public void done() {
		mDrivetrain.setArcadeDriveSpeed(0, 0);
		controller.disable();
	}

	@Override
	public void pidWrite(double output) {
		if (output < mMinTurnSpeed && output > 0) {
			pidRate = mMinTurnSpeed;
		} else if (output > -mMinTurnSpeed && output < 0) {
			pidRate = -mMinTurnSpeed;
		} else {
			pidRate = output;
		}
	}
}
