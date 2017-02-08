package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.Constants;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends Subsystem {

	static Climber mInstance = new Climber();

    public static Climber getInstance() {
        return mInstance;
    }
    
    private VictorSP mClimberTalon;
    
    
    public Climber() {
    	mClimberTalon = new VictorSP(Constants.kClimberId);
    }
    
	@Override
	public void updateSubsystem() {
		outputToSmartDashboard();
	}
	
	public void setSpeed(double speed){
		mClimberTalon.set(speed);
		System.out.println(speed);
	}
	
	public void stop(){
		setSpeed(0);
	}
	//
	
	

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Climber_Power_Percent", mClimberTalon.getSpeed());
	}

}
