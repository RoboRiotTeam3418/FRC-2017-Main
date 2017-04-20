package com.team3418.frc2017;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.modes.LeftGearStayMode;
import com.team3418.frc2017.auto.modes.MiddleGearStayMode;
import com.team3418.frc2017.auto.modes.RightGearStayMode;
import com.team3418.frc2017.auto.modes.ShootMode;
import com.team3418.frc2017.auto.modes.StandStillMode;
import com.team3418.frc2017.auto.modes.TestMode;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardInteractions {
	
	
	//chooser object to send to smartdashboard
	private SendableChooser<AutonOption> mAutoChooser;
	
	//default autonomous mode in case one is not selected
    private static final AutonOption DEFAULT_MODE = AutonOption.STAND_STILL;
    
    
    
    //happens when the class is first created
	public void initWithDefaults() {
		mAutoChooser = new SendableChooser<AutonOption>();
		
        mAutoChooser.addObject(AutonOption.MIDDLE_GEAR_STAY.name, AutonOption.MIDDLE_GEAR_STAY);
        mAutoChooser.addObject(AutonOption.LEFT_GEAR_STAY.name, AutonOption.LEFT_GEAR_STAY);
        mAutoChooser.addObject(AutonOption.RIGHT_GEAR_STAY.name, AutonOption.RIGHT_GEAR_STAY);
        mAutoChooser.addObject(AutonOption.SHOOT.name, AutonOption.SHOOT);
        mAutoChooser.addObject(AutonOption.STAND_STILL.name, AutonOption.STAND_STILL);
        mAutoChooser.addObject(AutonOption.TEST.name, AutonOption.TEST);
        mAutoChooser.addDefault("Stand_Still", AutonOption.STAND_STILL);
        
        SmartDashboard.putData("Auto Mode Chooser", mAutoChooser);
    }
	
	//compares selected auto mode to AutonOptions and returns the created mode
    public AutoModeBase getSelectedAutonMode() {
        AutonOption selectedOption = DEFAULT_MODE;
        for (AutonOption autonOption : AutonOption.values()) {
            if (autonOption == mAutoChooser.getSelected()) {
                selectedOption = autonOption;
                break;
            }
        }
        return createAutoMode(selectedOption);
    }
    
    
    //enum to hold all possible auto modes
   private enum AutonOption {
        MIDDLE_GEAR_STAY("middle gear stay", new MiddleGearStayMode()), //
        LEFT_GEAR_STAY("left gear stay", new LeftGearStayMode()), //
        RIGHT_GEAR_STAY("right gear stay", new RightGearStayMode()), //
        SHOOT("shoot", new ShootMode()), //
        STAND_STILL("stand still", new StandStillMode()),//
    	TEST("test (do not use at comp)", new TestMode()); //
        
        public final String name;
        public final AutoModeBase autoMode;

        AutonOption(String name, AutoModeBase autoMode) {
            this.name = name;
            this.autoMode = autoMode;
        }
    }
    
    //method to create auto mode based on chosen mode
    private AutoModeBase createAutoMode(AutonOption autonOption) {
        switch (autonOption) {
        case MIDDLE_GEAR_STAY:
            return autonOption.autoMode;
        case LEFT_GEAR_STAY:
            return autonOption.autoMode;
        case RIGHT_GEAR_STAY:
            return autonOption.autoMode;
        case SHOOT:
        	return autonOption.autoMode;
        case TEST:
            return autonOption.autoMode;
        case STAND_STILL:
        default:
            System.out.println("ERROR: unexpected auto mode: " + autonOption);
            return new StandStillMode();
        }
    }
}
