package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.CameraAlign;
import com.team3418.frc2017.auto.actions.DriveStraightActionTime;
import com.team3418.frc2017.auto.actions.ExtendGearAction;
import com.team3418.frc2017.auto.actions.RetractGearAction;
import com.team3418.frc2017.auto.actions.TurnActionPID;
import com.team3418.frc2017.auto.actions.WaitAction;


public class TestMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		/*
		runAction(new CameraAlign());
		runAction(new DriveStraightActionTime(2, false));
		runAction(new ExtendGearAction());
		runAction(new WaitAction(1));
		runAction(new DriveStraightActionTime(2, true));
		runAction(new RetractGearAction());
		*/
		
		runAction(new TurnActionPID(90));
		
	}
}
