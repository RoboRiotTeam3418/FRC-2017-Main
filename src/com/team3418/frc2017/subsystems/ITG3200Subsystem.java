package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.plugins.Gyro;
import com.team3418.frc2017.plugins.ITG3200;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ITG3200Subsystem extends Subsystem {

	public static ITG3200Subsystem mInstance = new ITG3200Subsystem();
	
	public static ITG3200Subsystem getInstance() {
		return mInstance;
	}
	
	private ITG3200 mGyro;
	private Gyro mGyroX;
	
	public ITG3200Subsystem() {
		mGyro = HardwareMap.getInstance().mGyro;
		// Track angle for duration
		mGyroX = mGyro.getXGyro();
	}
	
	public double getAngle() {
		return mGyroX.getAngle();
	}

	public Gyro createGyroY() {
		// Allocate a new independent gyro tracker on y-axis
		return mGyro.getYGyro();
	}
	
	public void resetGyroX() {
		mGyroX.reset();
	}
	
	public void calibrateGyro() {
		mGyro.calibrate(1000);
	}
	
	
	@Override
	void updateSubsystem() {
		
	}

	@Override
	void outputToSmartDashboard() {
		SmartDashboard.putNumber("Gyro_X_Value", getAngle());
	}

	@Override
	void stop() {
		
	}

}
