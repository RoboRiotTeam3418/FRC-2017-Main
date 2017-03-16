package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.CameraAlign;

public class TestMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		
		
		//
		//runAction(new TurnActionAngle(10));
		//runAction(new DriveStraightActionDistance(-90));
		//runAction(new TurnActionAngle(50));
		//runAction(new CameraAlignAndDrive(2.5));
		/*
		runAction(new WaitAction(3));
		runAction(new DriveStraightActionTime(1.5, true));
		*/
		runAction(new CameraAlign());
		
	}

}
