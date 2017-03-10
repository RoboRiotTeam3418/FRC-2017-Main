package com.team3418.frc2017;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.modes.LeftGearExitLeftMode;
import com.team3418.frc2017.auto.modes.LeftGearExitRightMode;
import com.team3418.frc2017.auto.modes.LeftGearStayMode;
import com.team3418.frc2017.auto.modes.MiddleGearExitLeftMode;
import com.team3418.frc2017.auto.modes.MiddleGearExitRightMode;
import com.team3418.frc2017.auto.modes.MiddleGearStayMode;
import com.team3418.frc2017.auto.modes.RightGearExitRightMode;
import com.team3418.frc2017.auto.modes.RightGearStayMode;
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
        for (AutonOption autonOption : AutonOption.values()) {
            mAutoChooser.addObject(autonOption.name, autonOption);
        }
        SmartDashboard.putData("Auto Options", mAutoChooser);
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
    enum AutonOption {
        MIDDLE_GEAR_EXIT_LEFT("1. middle gear exit left", new MiddleGearExitLeftMode()), //
        MIDDLE_GEAR_EXIT_RIGHT("2. middle gear exit right", new MiddleGearExitRightMode()), //
        MIDDLE_GEAR_STAY("3. middle gear stay", new MiddleGearStayMode()), //
        LEFT_GEAR_EXIT_LEFT("4. left gear exit left", new LeftGearExitLeftMode()), //
        LEFT_GEAR_EXIT_RIGHT("5. left gear exit right", new LeftGearExitRightMode()), //
        LEFT_GEAR_STAY("6. left gear stay", new LeftGearStayMode()), //
        RIGHT_GEAR_EXIT_LEFT("7. right gear exit left", new RightGearExitRightMode()), //
        RIGHT_GEAR_EXIT_RIGHT("8. right gear exit right", new RightGearExitRightMode()), //
        RIGHT_GEAR_STAY("9. right gear stay", new RightGearStayMode()), //
        STAND_STILL("10. stand still", new StandStillMode()),//
    	TEST("11. test (do not use at comp)", new TestMode()); //
        
    	
    	
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
        case MIDDLE_GEAR_EXIT_LEFT:
            return autonOption.autoMode;
        case MIDDLE_GEAR_EXIT_RIGHT:
            return autonOption.autoMode;
        case MIDDLE_GEAR_STAY:
            return autonOption.autoMode;
        case LEFT_GEAR_EXIT_LEFT:
            return autonOption.autoMode;
        case LEFT_GEAR_EXIT_RIGHT:
            return autonOption.autoMode;
        case LEFT_GEAR_STAY:
            return autonOption.autoMode;
        case RIGHT_GEAR_EXIT_LEFT:
            return autonOption.autoMode;
        case RIGHT_GEAR_EXIT_RIGHT:
            return autonOption.autoMode;
        case RIGHT_GEAR_STAY:
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
