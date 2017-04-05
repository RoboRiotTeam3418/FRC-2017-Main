package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.Constants;
import com.team3418.frc2017.HardwareMap;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends Subsystem {

	static Climber mInstance = new Climber();

    public static Climber getInstance() {
        return mInstance;
    }
    
    private VictorSP mClimberVictor;
        
    public Climber() {
    	mClimberVictor = HardwareMap.getInstance().mClimberTalon;
    }
    
  	public enum ClimberState {
      	FORWARD, REVERSE, STOP, HOLD
    }
  	
  	private ClimberState mClimberState;
  	
  	public ClimberState getClimberState() {
  		return mClimberState;
  	}
    
	@Override
	public void updateSubsystem() {
		
		switch(mClimberState){
		case FORWARD:
			setSpeed(1);
			break;
		case REVERSE:
			setSpeed(Constants.kClimberReverseSpeed);
			break;
		case HOLD:
			setSpeed(Constants.kClimberHoldSpeed);
			break;
		case STOP:
			setSpeed(0);
			break;
		default:
			
			break;
		}
		
		outputToSmartDashboard();
		
	}
	
	public void forward(){
		mClimberState = ClimberState.FORWARD;
	}
	
	public void reverse() {
		mClimberState = ClimberState.REVERSE;
	}
	
	@Override
	public void stop(){
		mClimberState = ClimberState.STOP;
	}
	
	public void hold(){
		mClimberState = ClimberState.HOLD;
	}
	
	public void setSpeed(double speed) {
		mClimberVictor.set(speed);
	}
	
	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Climber_Speed", mClimberVictor.getSpeed());
		SmartDashboard.putString("Climber_State", mClimberState.toString());
	}

}
