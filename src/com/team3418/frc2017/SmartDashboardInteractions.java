package com.team3418.frc2017;

import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.modes.MiddleGearExitLeftMode;
import com.team3418.frc2017.auto.modes.StandStillMode;
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
        MIDDLE_GEAR("place middle gear", new MiddleGearExitLeftMode()), //
        STAND_STILL("stand still and do nothing", new StandStillMode());//
        
    	
    	
    	
    	
    	
    	
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
        case MIDDLE_GEAR:
            return AutonOption.MIDDLE_GEAR.autoMode;
            
            
            
            
            
            
            
        case STAND_STILL:
        default:
            System.out.println("ERROR: unexpected auto mode: " + autonOption);
            return new StandStillMode();
        }
    }
}
