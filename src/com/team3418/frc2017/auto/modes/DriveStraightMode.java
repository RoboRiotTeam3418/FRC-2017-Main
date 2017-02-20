package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.DriveStraightAction;

public class DriveStraightMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new DriveStraightAction(5.0));
	}
}
