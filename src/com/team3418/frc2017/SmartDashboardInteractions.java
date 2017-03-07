package com.team3418.frc2017;

import org.json.simple.JSONArray;
import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.AutoModeEndedException;
import com.team3418.frc2017.auto.modes.DriveStraightMode;
import com.team3418.frc2017.auto.modes.StandStillMode;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardInteractions {
	
    private static final String AUTO_OPTIONS = "auto_options";
    private static final String SELECTED_AUTO_MODE = "selected_auto_mode";
    private static final AutonOption DEFAULT_MODE = AutonOption.STAND_STILL;
    
    @SuppressWarnings("unchecked")
	public void initWithDefaults() {
        JSONArray autoOptionsArray = new JSONArray();
        for (AutonOption autonOption : AutonOption.values()) {
            autoOptionsArray.add(autonOption.name);
        }
        SmartDashboard.putString(AUTO_OPTIONS, autoOptionsArray.toString());
        SmartDashboard.putString(SELECTED_AUTO_MODE, DEFAULT_MODE.name);
    }

    public AutoModeBase getSelectedAutonMode() {
        String autoModeString = SmartDashboard.getString(SELECTED_AUTO_MODE, DEFAULT_MODE.name);
        AutonOption selectedOption = DEFAULT_MODE;
        for (AutonOption autonOption : AutonOption.values()) {
            if (autonOption.name.equals(autoModeString)) {
                selectedOption = autonOption;
                break;
            }
        }
        return createAutoMode(selectedOption);
    }
    
    /**
     * I don't trust {@link SendableChooser} to manage {@link AutoModeBase}
     * objects directly, so use this enum to protects us from WPILIb.
     */
    enum AutonOption {
        STAY_HIGH_ONE_BALL_DRIVE_BACK("No Drop Drive Back"), //
        TEST_DRIVE("test drive"),//
        STAND_STILL("stand still and do nothing");//
        
        public final String name;

        AutonOption(String name) {
            this.name = name;
        }
    }

    /*
    enum AutonLane {
        LANE_1(160, "1"), LANE_2(205, "2"), LANE_3(160, "3"), LANE_4(155, "4"), LANE_5(220, "5");

        public final double distanceToDrive;
        public final String numberString;

        AutonLane(double distanceToDrive, String numberString) {
            this.distanceToDrive = distanceToDrive;
            this.numberString = numberString;
        }
    }
    */
    
    private AutoModeBase createAutoMode(AutonOption autonOption) {
        switch (autonOption) {
        case STAY_HIGH_ONE_BALL_DRIVE_BACK:
            return new DriveStraightMode();
        case TEST_DRIVE:
            return new AutoModeBase() {
                @Override
                protected void routine() throws AutoModeEndedException {
                    throw new RuntimeException("Expected exception!!!");
                }
            };
            
        case STAND_STILL: // fallthrough
        default:
            System.out.println("ERROR: unexpected auto mode: " + autonOption);
            return new StandStillMode();
        }
    }
}