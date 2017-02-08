package com.team3418.frc2017;

import edu.wpi.first.wpilibj.Joystick;

public class ControlBoard {
	private static ControlBoard mInstance = new ControlBoard();

    public static ControlBoard getInstance() {
        return mInstance;
    }
    
    //Create Joystick Object
    private final Joystick mDriverStick;
    
    //Initialize Joystick Object
    ControlBoard() {
    	mDriverStick = new Joystick(0);
    }
    
    //DRIVER CONTROLS
    
}
