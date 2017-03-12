package com.team3418.frc2017.auto.modes;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.actions.CameraAlignAndDrive;
import com.team3418.frc2017.auto.actions.DriveStraightActionDistance;
import com.team3418.frc2017.auto.actions.DriveStraightActionTime;
import com.team3418.frc2017.auto.actions.TurnActionAngle;
import com.team3418.frc2017.auto.actions.WaitAction;

public class TestMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		
		
		
		//runAction(new DriveStraightActionDistance(-90));
		//runAction(new TurnActionAngle(50));
		runAction(new CameraAlignAndDrive(2.5));

		runAction(new WaitAction(3));
		runAction(new DriveStraightActionTime(1.5, true));
				
		
	}

}
