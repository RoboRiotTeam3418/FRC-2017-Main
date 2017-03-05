package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.DriveStraightActionNotWorking;
import com.team3418.frc2017.auto.actions.DriveStraightActionTime;
import com.team3418.frc2017.auto.actions.TurnAction;
import com.team3418.frc2017.auto.actions.DriveStraightActionDistance;
import com.team3418.frc2017.auto.actions.WaitAction;

public class DriveStraightMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		
		//runAction(new TurnAction(90));
		
		
		runAction(new DriveStraightActionTime(3, false));
		runAction(new WaitAction(3));
		runAction(new DriveStraightActionTime(1, true));
		
		
		/*
		runAction(new DriveStraightActionDistance(-54));
		runAction(new WaitAction(5));
		runAction(new DriveStraightActionDistance(54));
		*/
	}
}
