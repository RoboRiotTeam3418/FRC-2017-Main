package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.DriveStraightActionDistance;
import com.team3418.frc2017.auto.actions.DriveStraightActionTime;
import com.team3418.frc2017.auto.actions.TurnAction;
import com.team3418.frc2017.auto.actions.WaitAction;

public class LeftGearExitLeft extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		
		//runAction(new DriveStraightActionTime(3.5, false));
		runAction(new DriveStraightActionDistance(-90));
		
		
		
		
		runAction(new TurnAction(50));
		runAction(new DriveStraightActionTime(1.8, false));
		
		
		runAction(new WaitAction(3));
		runAction(new DriveStraightActionTime(3, true));
		runAction(new WaitAction(.5));
		runAction(new TurnAction(120));
		runAction(new WaitAction(1));
		runAction(new DriveStraightActionDistance(-10));
		
	}
	
	
	
	
}
