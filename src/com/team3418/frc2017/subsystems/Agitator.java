package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.Constants;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Agitator extends Subsystem {
	
	static Agitator mInstance = new Agitator();
	
	public static Agitator getInstance(){
		return mInstance;
	}
	
	private VictorSP  mAgitator;
	
	public Agitator() {
		mAgitator = new VictorSP(Constants.kAgitatorId);
		System.out.println("Agitator Initialized");
	}
	
	public enum AgitatorState {
    	AGITATOR_CLOCKWISE,
    	AGITATOR_COUNTERCLOCKWISE,
    	AGITATOR_STOP
    }
	
	private AgitatorState mAgitatorState;
	
	public AgitatorState getAgitatorState() {
		return mAgitatorState;
	}

	@Override
	public void updateSubsystem() {
		switch(mAgitatorState) {
		case AGITATOR_CLOCKWISE:
			setMotorSpeed(Constants.kAgitatorSpeed);
			break;
		case AGITATOR_COUNTERCLOCKWISE:
			setMotorSpeed(Constants.kAgitatorReverseSpeed);
			break;
		case AGITATOR_STOP:
			setMotorSpeed(0);
			break;
		default:
			mAgitatorState = AgitatorState.AGITATOR_STOP;
			break;
		}
	}
	
	public void clockwiseAgitator(){
		mAgitatorState = AgitatorState.AGITATOR_CLOCKWISE;
	}
	
	public void counterclockwiseAgitator(){
		mAgitatorState = AgitatorState.AGITATOR_COUNTERCLOCKWISE;
	}
	
	public void stopAgitator(){
		mAgitatorState = AgitatorState.AGITATOR_STOP;
	}
	

	private void setMotorSpeed(double speed) {
		mAgitator.set(speed);
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Agitator_Speed", mAgitator.getSpeed());
	}
}