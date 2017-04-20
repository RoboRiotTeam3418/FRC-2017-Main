package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.subsystems.Shooter;

public class ShootAction implements Action {
	
	
	Shooter mShooter;
	boolean mShoot;
	
	public ShootAction(boolean shoot) {
		mShooter = Shooter.getInstance();
		mShoot = shoot;
	}

	@Override
	public void start() {
		if (mShoot) {
			mShooter.shoot();
			System.out.println("shooter is shooting");
		} else {
			mShooter.stop();
			System.out.println("shooter has stopped");
		}
	}

	@Override
	public void update() {
		
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void done() {
		
	}

}
