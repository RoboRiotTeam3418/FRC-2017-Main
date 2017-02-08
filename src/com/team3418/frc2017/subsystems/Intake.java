package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.Constants;
import edu.wpi.first.wpilibj.VictorSP;

public class Intake extends Subsystem
{
	static Intake mInstance = new Intake();

    public static Intake getInstance() {
        return mInstance;
    }
    
	private VictorSP  mIntakeRoller;
    
	Intake() {
		mIntakeRoller = new VictorSP(Constants.kIntakeRollerId);
		System.out.println("Intake Initialized");
	}
    
    public enum IntakeRollerState {
    	ROLLER_IN,
    	ROLLER_OUT,
    	ROLLER_STOP
    }
	
	private IntakeRollerState mIntakeRollerState;

	public IntakeRollerState getIntakeRollerState() {
		return mIntakeRollerState;
	}
	
	@Override
	public void updateSubsystem()
	{
		switch(mIntakeRollerState) {
		case ROLLER_IN:
			setRollerSpeed(Constants.kRollerIntakeSpeed);
			break;
		case ROLLER_OUT:
			setRollerSpeed(Constants.kRollerReverseSpeed);
			break;
		case ROLLER_STOP:
			setRollerSpeed(0);
			break;
		default:
			mIntakeRollerState = IntakeRollerState.ROLLER_STOP;
			break;
		}
	}	
	
	public void rollerIn(){
		mIntakeRollerState = IntakeRollerState.ROLLER_IN;
	}
	
	public void rollerOut(){
		mIntakeRollerState = IntakeRollerState.ROLLER_OUT;
	}
	
	public void stopRoller(){
		mIntakeRollerState = IntakeRollerState.ROLLER_STOP;
	}
	

	private void setRollerSpeed(double speed) {
		mIntakeRoller.set(speed);
	}

	@Override
	public void outputToSmartDashboard() {
		mIntakeRoller.getSpeed();
	}
}