package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.CameraAlign;

public class TestMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		/*
		runAction(new DriveStraightActionDistance(-40));
		runAction(new DriveStraightActionDistance(40));
		runAction(new DriveStraightActionDistance(-40));
		runAction(new DriveStraightActionDistance(40));
		 */
		/*
		runAction(new CameraAlign());
		//runAction(new TurnActionAngle(10));
		runAction(new CameraAlign());
		*/
		
		/*
		runAction(new TurnActionAngle(90));
		runAction(new WaitAction(2));
		runAction(new TurnActionAngle(90));
		runAction(new WaitAction(2));
		runAction(new TurnActionAngle(360));
		*/
		/*
		runAction(new TurnActionTime(1, -1, .8));
		runAction(new WaitAction(2));
		runAction(new TurnActionTime(1, 1, .8));
		*/
		
		runAction(new CameraAlign());
		
		
	}
}
