package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.CameraAlign;
import com.team3418.frc2017.auto.actions.DriveStraightActionDistance;
import com.team3418.frc2017.auto.actions.DriveStraightActionTime;
import com.team3418.frc2017.auto.actions.ExtendGearAction;
import com.team3418.frc2017.auto.actions.RetractGearAction;
import com.team3418.frc2017.auto.actions.TurnActionAngle;
import com.team3418.frc2017.auto.actions.WaitAction;

public class RightGearStayMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new DriveStraightActionDistance(-105));
		runAction(new TurnActionAngle(-50));
		runAction(new CameraAlign());
		runAction(new DriveStraightActionTime(2, false, .6));
		runAction(new ExtendGearAction());
		runAction(new WaitAction(.5));
		runAction(new DriveStraightActionTime(1, true));
		runAction(new RetractGearAction());
	}
}