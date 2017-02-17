package com.team3418.frc2017;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

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
	
	
	public Encoder mLeftDrivetrainEncoder;
	public Encoder mRightDrivetrainEncoder;
	
	public ADXRS450_Gyro mGyro;
	//public BuiltInAccelerometer mAccelerometer;
	
	HardwareMap() {
		
		try
		{
			mAgitatorTalon = new VictorSP(Constants.kAgitatorId);
			mClimberTalon = new VictorSP(Constants.kClimberId);
			mLeftShifterSolenoid = new Solenoid(Constants.kLeftShifterSolenoidId);
	    	mRightShifterSolenoid = new Solenoid(Constants.kRightShifterSolenoidId);
	    	mIntakeTalon = new VictorSP(Constants.kIntakeRollerId);
	    	mFeederTalon = new VictorSP(Constants.kFeederId);
			
			
	    	mLeftDrivetrainEncoder = new Encoder(1, 2);
	    	mRightDrivetrainEncoder = new Encoder(3, 4);
			//mGyro = new ADXRS450_Gyro();
			//mGyro.reset();
			
			//mAccelerometer = new BuiltInAccelerometer();
			
			
			mCompressor = new Compressor(0);
		}
		catch(Exception e)
		{
			
		}
	}

}
