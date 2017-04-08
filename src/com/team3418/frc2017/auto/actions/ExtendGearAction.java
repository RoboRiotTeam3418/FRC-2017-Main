package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.subsystems.MrGush;

public class ExtendGearAction implements Action{

	MrGush mMrGush;
	
	public ExtendGearAction() {
		mMrGush = MrGush.getInstance();
	}
	
	@Override
	public void start() {
		mMrGush.Extend();
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
