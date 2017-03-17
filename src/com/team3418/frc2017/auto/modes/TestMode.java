package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.CameraAlign;
import com.team3418.frc2017.auto.actions.DriveStraightActionDistance;

public class TestMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		/*
		runAction(new DriveStraightActionDistance(-40));
		runAction(new DriveStraightActionDistance(40));
		runAction(new DriveStraightActionDistance(-40));
		runAction(new DriveStraightActionDistance(40));
		 */
		
		runAction(new CameraAlign());
	}

}
