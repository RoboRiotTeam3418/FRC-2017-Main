package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.HardwareMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroSubsystem extends Subsystem {
	
	static GyroSubsystem mInstance = new GyroSubsystem();
	
	public static GyroSubsystem getInstance() {
		return mInstance;
	}
	
	HardwareMap mHardwareMap;
	ADXRS450_Gyro mGyro;
	
	double mGyroAngle;
	int mRotationCounter = 0;
	
	public GyroSubsystem() {
		mHardwareMap = HardwareMap.getInstance();
		mGyro = mHardwareMap.mGyro;
	}
	
	public double getAngle() {
		return mGyroAngle;
	}
	
	
	@Override
	public void updateSubsystem() {
		
		if (mGyro.getAngle() > 360 * mRotationCounter) {
			mGyroAngle = 0;
			mRotationCounter++;
		} else if (mGyro.getAngle() < 0) {
			mGyroAngle = 360;
		} else {
			mGyroAngle = mGyro.getAngle();
		}
		
		outputToSmartDashboard();
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Gyro_Subsystem_Angle", mGyroAngle);
	}

	@Override
	public void stop() {
		
	}

}
