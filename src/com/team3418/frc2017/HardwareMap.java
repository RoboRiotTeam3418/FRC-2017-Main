package com.team3418.frc2017;

import com.team3418.frc2017.plugins.ITG3200;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public class HardwareMap {
	
	private static HardwareMap mInstance = new HardwareMap();
	
	public static HardwareMap getInstance(){
		return mInstance;
	}
	
	public Compressor mCompressor;
	public VictorSP mAgitatorTalon;
	public VictorSP mIntakeTalon;
	public VictorSP mFeederTalon;
	public VictorSP mClimberTalon;
	public Solenoid mLeftShifterSolenoid;
	public Solenoid mRightShifterSolenoid;
	
	public ITG3200 mGyro;
	DigitalInput mGyroInterrupt;
	
	ADXL345_I2C mAccelerometer;
	
	
	HardwareMap() {
		
		try
		{
			mAgitatorTalon = new VictorSP(Constants.kAgitatorId);
			mClimberTalon = new VictorSP(Constants.kClimberId);
			mLeftShifterSolenoid = new Solenoid(Constants.kLeftShifterSolenoidId);
	    	mRightShifterSolenoid = new Solenoid(Constants.kRightShifterSolenoidId);
	    	mIntakeTalon = new VictorSP(Constants.kIntakeRollerId);
	    	mFeederTalon = new VictorSP(Constants.kFeederId);
			
	    	mAccelerometer = new ADXL345_I2C(Port.kOnboard,Range.k8G);
			
			mCompressor = new Compressor(0);
			
			mGyroInterrupt = new DigitalInput(0);
			mGyro = new ITG3200(I2C.Port.kOnboard, mGyroInterrupt);
		}
		catch(Exception e)
		{
			
		}
	}

}
