package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.CameraAlign;
import com.team3418.frc2017.auto.actions.CameraAlignOld;
import com.team3418.frc2017.auto.actions.DriveStraightActionDistance;
import com.team3418.frc2017.auto.actions.TurnActionAngle;

public class TestMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		/*
		runAction(new DriveStraightActionDistance(-40));
		runAction(new DriveStraightActionDistance(40));
		runAction(new DriveStraightActionDistance(-40));
		runAction(new DriveStraightActionDistance(40));
		 */
		
		runAction(new CameraAlignOld());
		runAction(new TurnActionAngle(10));
		runAction(new CameraAlign());
	}

}
