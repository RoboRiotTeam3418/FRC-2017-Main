package com.team3418.frc2017.auto;

public class AutoExecutor {
	int robotPosition = 0; //1 = LEFT, 2 = MIDDLE, 3 = RIGHT
	int gearPosition = 0; //1 = LEFT, 2 = MIDDLE, 3 = RIGHT
	int hopperPosition = 0; //1 = CLOSEST, 2 = MIDDLE, 3 = FARTHEST
	int shootPosition = 0; //0 = NO SHOOT, 1 SHOOT
	int fieldOrientationConstant = 1;  //-1 = SHOOTER ON LEFT, 1 = SHOOTER ON RIGHT
	
	public void autoExecutorInit() {
		
	}
	
	public void updateAutoPath() {
		//GET PATH FROM DRIVER DASHBOARD vv AND SET TO VARIABLES
		robotPosition = 0; //1 = LEFT, 2 = MIDDLE, 3 = RIGHT
		gearPosition = 0; //1 = LEFT, 2 = MIDDLE, 3 = RIGHT
		hopperPosition = 0; //0 = NO HOPPER, 1 = CLOSEST, 2 = MIDDLE, 3 = FARTHEST
		shootPosition = 0; //0 = NO SHOOT, 1 SHOOT
		fieldOrientationConstant = 1;  //-1 = LEFT, 1 = RIGHT
	}
	
	public void robotAutoPath(int step) {
		if (robotPosition == 0) {
			stopAutoPath();
			System.out.print("!Robot Position Not Specified ! - Stopping Autonomous");
			return; //(break from method)
		}
		//STEP 1 - Start to Gear
		if (step == 1) {
			if (gearPosition == 0) {
				stopAutoPath();
				System.out.print("!Gear Position Not Specified ! - Stopping Autonomous");
				return; //(break from method)
			}
			if (robotPosition == 1) {
				if (gearPosition == 1) {
					
				}
				if (gearPosition == 2) {
					
				}
				//no gearPosition == 3 possible
			}
			if (robotPosition == 2) {
				//no gearPosition == 1 possible
				if (gearPosition == 2) {
					
				}
				//no gearPosition == 3 possible
			}
			if (robotPosition == 3) {
				//no gearPosition == 1 possible
				if (gearPosition == 2) {
					
				}
				if (gearPosition == 3) {
					
				}
			}
		}
		
		//STEP 2 - Gear to Hopper
		if (step == 2) {
			if (hopperPosition == 0) {
				stopAutoPath();
				System.out.print("!Hopper Position Not Specified ! - Stopping Autonomous");
				return; //(break from method)
			}
			if (gearPosition == 1) {
				if (hopperPosition == 1) {
					
				}
				//no hopperPosition == 2 possible
				if (hopperPosition == 3) {
					
				}
			}
			if (gearPosition == 2) {
				if (hopperPosition == 1) {
					
				}
				//no hopperPosition == 2 possible
				//no hopperPosition == 3 possible
			}
			if (gearPosition == 3) {
				//no hopperPosition == 1 possible
				if (hopperPosition == 2) {
					
				}
				//no hopperPosition == 3 possible
			}
		}
		
		//STEP 3 - Hopper to Shoot
		if (step == 3) {
			if (shootPosition == 0) {
				stopAutoPath();
				System.out.print("!Shooter Position Not Specified ! - Stopping Autonomous");
				return; //(break from method)
			}
			//no hopperPosition == 1 possible to shoot
			if (hopperPosition == 2) {
				
			}
			//no hopperPosition == 3 possible to shoot
		}
		
		//STEP 4 - Shoot & Finish
		if (step == 4) {
			if (shootPosition == 1) {
				
			}
		}
	}
	
	public void stopAutoPath() {
		
	}
}