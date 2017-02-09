package com.team3418.frc2017;

import edu.wpi.first.wpilibj.Joystick;

public class ControlBoard {
	
	private static ControlBoard mInstance = new ControlBoard();

    public static ControlBoard getInstance() {
        return mInstance;
    }
    
    //Create Joystick Object
    private final Joystick mDriverStick;
    private final Joystick mSecondaryDriverStick;
    
    //Initialize Joystick Object
    ControlBoard() {
    	mDriverStick = new Joystick(0);
    	mSecondaryDriverStick = new Joystick(1);
    }
    
    //DRIVER CONTROLS
    
    public double getDriverLeftX(){
    	return mDriverStick.getRawAxis(0);
    }
    
    public double getDriverLeftY(){
    	return mDriverStick.getRawAxis(1);
    }
    
    public double getDriverRightX(){
    	return mDriverStick.getRawAxis(4);
    }
    
    public double getDriverRightY(){
    	return mDriverStick.getRawAxis(5);
    }
    
    public boolean getDriverHighGearButton(){
    	return mDriverStick.getRawButton(5);
    }
    
    public boolean getDriverLowGearButton(){
    	return mDriverStick.getRawButton(6);
    }
    
    public double getClimberAxis(){
    	return mDriverStick.getRawAxis(2) + -mDriverStick.getRawAxis(3);
    }
    
    public boolean getSecondaryIntakeButton(){
    	return mSecondaryDriverStick.getRawButton(1);
    }
    
    public boolean getSecondaryShootButton(){
    	return mSecondaryDriverStick.getRawButton(4);
    }
}
