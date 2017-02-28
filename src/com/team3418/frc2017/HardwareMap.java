package com.team3418.frc2017;

import com.team3418.frc2017.plugins.ITG3200;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
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
	//public AnalogGyro mAnalogGyro;
	
	//public ADXL345_I2C mAccelerometer;
	
	//public Encoder mLeftDrivetrainEncoder;
	//public Encoder mRightDrivetrainEncoder;
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
			
	    	//mAccelerometer = new ADXL345_I2C(Port.kOnboard,Range.k8G);
	    	//mAnalogGyro = new AnalogGyro(0);
	    	//mAnalogGyro.reset();
			//mGyro = new ITG3200();
			
			mCompressor = new Compressor(0);
			//I2C.Port.kOnboard, false
			
			
			mGyroInterrupt = new DigitalInput(0);
			mGyro = new ITG3200(I2C.Port.kOnboard, mGyroInterrupt);
		}
		catch(Exception e)
		{
			
		}
	}

}
