package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.Constants;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem
{
	static Intake mInstance = new Intake();

    public static Intake getInstance() {
        return mInstance;
    }
    
	private Talon mIntake;
    
	public Intake() {
		mIntake = new Talon(Constants.kIntakeRollerId);
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
	
	public void intakeIn(){
		mIntakeRollerState = IntakeRollerState.ROLLER_IN;
	}
	
	public void intakeOut(){
		mIntakeRollerState = IntakeRollerState.ROLLER_OUT;
	}
	
	public void stopIntakeRoller(){
		mIntakeRollerState = IntakeRollerState.ROLLER_STOP;
	}
	

	private void setRollerSpeed(double speed) {
		mIntake.set(speed);
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Intake_Speed", mIntake.getSpeed());
	}
}