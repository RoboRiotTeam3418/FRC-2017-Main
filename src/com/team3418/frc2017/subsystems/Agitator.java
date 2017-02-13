package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.Constants;
import com.team3418.frc2017.HardwareMap;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Agitator extends Subsystem {
	
	//variable to store subsystem instance
	static Agitator mInstance = new Agitator();
	//
	
	//method to get subsystem instance
	public static Agitator getInstance(){
		return mInstance;
	}
	//
	
	//variable to store hardware instance
	private VictorSP mAgitatorVictor;
	
	//Constructor gets hardware instance and initializes settings
	public Agitator() {
		mAgitatorVictor = HardwareMap.getInstance().mAgitatorTalon;
		System.out.println("Agitator Initialized");
	}
	//
	
	//possible subsystem states
	public enum AgitatorState {
    	FEED,
    	REVERSE,
    	STOP
    }
	//
	
	//variable to hold subsystem state
	private AgitatorState mAgitatorState;
	//
	
	//method to get current subsystem state
	public AgitatorState getAgitatorState() {
		return mAgitatorState;
	}
	//

	//method to update subsystem state and run other periodic subsystem tasks
	@Override
	public void updateSubsystem() {
		
		switch(mAgitatorState) {
		case FEED:
			setMotorSpeed(Constants.kAgitatorFeedSpeed);
			break;
		case REVERSE:
			setMotorSpeed(Constants.kAgitatorReverseSpeed);
			break;
		case STOP:
			setMotorSpeed(0);
			break;
		default:
			stop();
			break;
		}
		
		outputToSmartDashboard();
	}
	//
	
	//methods to set subsystem state
	public void feed(){
		mAgitatorState = AgitatorState.FEED;
	}
	
	public void reverse(){
		mAgitatorState = AgitatorState.REVERSE;
	}
	
	@Override
	public void stop(){
		mAgitatorState = AgitatorState.STOP;
	}
	//

	//methods to control hardware
	private void setMotorSpeed(double speed) {
		mAgitatorVictor.set(speed);
	}
	//
	
	
	//method to output helpful information to the smartdashboard
	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Agitator_Speed", mAgitatorVictor.getSpeed());
		SmartDashboard.putString("Agitator_State", mAgitatorState.toString());
	}
}