package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.CameraAlign;
import com.team3418.frc2017.auto.actions.DriveStraightActionDistance;
import com.team3418.frc2017.auto.actions.DriveStraightActionTime;

public class MiddleGearStayMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new DriveStraightActionDistance(-60));
		runAction(new CameraAlign());
		runAction(new DriveStraightActionTime(1, false));
	}
}
