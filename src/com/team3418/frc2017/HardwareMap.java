package com.team3418.frc2017;

import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SPI.Port;
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
	
	
	
	
	public ADXRS450_Gyro mGyro;	
	
	HardwareMap() {
		
		try
		{
			mAgitatorTalon = new VictorSP(Constants.kAgitatorId);
			mClimberTalon = new VictorSP(Constants.kClimberId);
			mLeftShifterSolenoid = new Solenoid(Constants.kLeftShifterSolenoidId);
	    	mRightShifterSolenoid = new Solenoid(Constants.kRightShifterSolenoidId);
	    	mIntakeTalon = new VictorSP(Constants.kIntakeRollerId);
	    	mFeederTalon = new VictorSP(Constants.kFeederId);
	    	
			mCompressor = new Compressor(0);
			
			mGyro = new ADXRS450_Gyro();
			mGyro.reset();
			mGyro.calibrate();
		}
		catch(Exception e)
		{
			
		}
	}

}
