package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.Constants;
import com.team3418.frc2017.HardwareMap;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem
{
	static Intake mInstance = new Intake();

    public static Intake getInstance() {
        return mInstance;
    }
    
	private VictorSP mIntakeVictor;
    
	public Intake() {
		mIntakeVictor = HardwareMap.getInstance().mIntakeTalon;
		System.out.println("Intake Initialized");
	}
    
    public enum IntakeRollerState {
    	INTAKE,
    	REVERSE,
    	STOP
    }
	
	private IntakeRollerState mIntakeRollerState;

	public IntakeRollerState getIntakeRollerState() {
		return mIntakeRollerState;
	}
	
	@Override
	public void updateSubsystem()
	{
		switch(mIntakeRollerState) {
		case INTAKE:
			setRollerSpeed(Constants.kRollerIntakeSpeed);
			break;
		case REVERSE:
			setRollerSpeed(Constants.kRollerReverseSpeed);
			break;
		case STOP:
			setRollerSpeed(0);
			break;
		default:
			mIntakeRollerState = IntakeRollerState.STOP;
			break;
		}
	}	
	
	public void intake(){
		mIntakeRollerState = IntakeRollerState.INTAKE;
	}
	
	public void reverse(){
		mIntakeRollerState = IntakeRollerState.REVERSE;
	}
	
	@Override
	public void stop(){
		mIntakeRollerState = IntakeRollerState.STOP;
	}
	

	private void setRollerSpeed(double speed) {
		mIntakeVictor.set(speed);
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Intake_Speed", mIntakeVictor.getSpeed());
		SmartDashboard.putString("Roller_State", mIntakeRollerState.toString());
	}
}