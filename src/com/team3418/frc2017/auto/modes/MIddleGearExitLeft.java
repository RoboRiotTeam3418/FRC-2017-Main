package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.DriveStraightActionTime;
import com.team3418.frc2017.auto.actions.TurnAction;
import com.team3418.frc2017.auto.actions.WaitAction;

public class MIddleGearExitLeft extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {		
		
		runAction(new DriveStraightActionTime(3, false));
		runAction(new WaitAction(3));
		runAction(new DriveStraightActionTime(1, true));
		runAction(new WaitAction(.5));
		runAction(new TurnAction(90));
		runAction(new WaitAction(.5));
		runAction(new DriveStraightActionTime(4, true));
		runAction(new TurnAction(90));
		runAction(new WaitAction(.5));
		runAction(new DriveStraightActionTime(2, true));
		
		
		runAction(new TurnAction(90));
		runAction(new TurnAction(-90));

		
	}
}
