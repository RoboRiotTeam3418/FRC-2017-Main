package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.Timer;

public class TurnActionTime implements Action {
	
	private Drivetrain mDrivetrain = Drivetrain.getInstance();
	
	private double mDirection;//right is true left is false
	private double mRotationalSpeed = .65;
	private double mTimeToWait;
	private double mStartTime;
	private double mCurrentTime;
	
	public TurnActionTime(double time, double direction) {
		mTimeToWait = time;
		mDirection = direction;
	}
	
	public TurnActionTime(double time, double direction, double rotationalSpeed) {
		mTimeToWait = time;
		mDirection = direction;
		mRotationalSpeed = rotationalSpeed;
	}
	
	@Override
	public void start() {
		mDrivetrain.lowGear();
		mStartTime = Timer.getFPGATimestamp();
	}

	@Override
	public void update() {
		mCurrentTime = Timer.getFPGATimestamp();
    	mDrivetrain.setTankDriveSpeed(mRotationalSpeed * mDirection, -mRotationalSpeed * mDirection);
	}

	@Override
	public boolean isFinished() {
		if ((mCurrentTime - mStartTime) > mTimeToWait) {
			return true;
		}
		return false;
	}

	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
		System.out.println("finished with turn (timed) action");
	}
}
