package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.Constants;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MrGush extends Subsystem {
	
	static MrGush mInstance = new MrGush();

    public static MrGush getInstance() {
        return mInstance;
    }
    
	public MrGush() {
		MrGushySolenoid = new Solenoid(Constants.kMrGushySolenoid);
		System.out.println("Mr Gushy has been initialized :D");
	}

	private Solenoid MrGushySolenoid;
	
	public enum MrGushState {
      	EXTENDED, RETRACTED
    }
  	
  	private MrGushState MrGushState;
  	
  	public MrGushState getClimberState() {
  		return MrGushState;
  	}
	

	
	@Override
	public void updateSubsystem() {
		
		switch(MrGushState){
		case EXTENDED:
			setExtend();
			break;
		case RETRACTED:
			setRetract();
			break;
		default:
			setRetract();
			break;
		}
		
		outputToSmartDashboard();
	}
	
	public void Extend() {
		MrGushState = MrGushState.EXTENDED;
	}
	
	public void Retract() {
		MrGushState = MrGushState.RETRACTED;
	}
	
	private void setExtend() {
		MrGushySolenoid.set(true);
	}
	
	private void setRetract() {
		MrGushySolenoid.set(false);
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putString("MrGushyState", MrGushState.toString());
	}

	@Override
	public void stop() {
		Retract();
	}
}
