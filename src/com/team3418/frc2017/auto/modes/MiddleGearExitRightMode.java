package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.CameraAlign;
import com.team3418.frc2017.auto.actions.DriveStraightActionDistance;
import com.team3418.frc2017.auto.actions.DriveStraightActionTime;
import com.team3418.frc2017.auto.actions.TurnActionAngle;
import com.team3418.frc2017.auto.actions.TurnActionTime;
import com.team3418.frc2017.auto.actions.WaitAction;

public class MiddleGearExitRightMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new DriveStraightActionDistance(-60));
		runAction(new CameraAlign());
		runAction(new DriveStraightActionTime(1, false));
		runAction(new WaitAction(5));
		
		
		runAction(new DriveStraightActionTime(1, true, 1));
		runAction(new TurnActionAngle(80));
		runAction(new WaitAction(.25));
		runAction(new DriveStraightActionDistance(-70, .8));
		runAction(new TurnActionAngle(-40));
		runAction(new WaitAction(.25));
		runAction(new DriveStraightActionDistance(-40, .8));
		runAction(new TurnActionAngle(-40));
		runAction(new WaitAction(.25));
		runAction(new DriveStraightActionDistance(-50, .8));
	}
}
