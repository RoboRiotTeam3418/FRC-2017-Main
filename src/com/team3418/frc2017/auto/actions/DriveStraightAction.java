package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class DriveStraightAction implements Action {
	
	private double mDistanceSetPoint;
	private double mGyroSetpoint;
	private double mEncoderPIDRate;
	private double mGyroPIDRate;

	private Drivetrain mDrivetrain;
	private Encoder mEncoder;
	private ADXRS450_Gyro mGyro;
	
	private PIDController mEncoderPIDController;
	private PIDController mGyroPIDController;
	
	
	private PIDOutput mEncoderPIDOutput;
	private PIDOutput mGyroPIDOutput;
	
	
	
	
    public DriveStraightAction(double distance) {
    	mDrivetrain = Drivetrain.getInstance();
    	mEncoder = Drivetrain.getInstance().mRightEncoder;
    	mGyro = HardwareMap.getInstance().mGyro;
        
    	mEncoderPIDOutput = new PIDOutput() {
    		@Override
    		public void pidWrite(double output) {
    			mEncoderPIDRate = output;
    		}
    	};
    	
        mGyroPIDOutput = new PIDOutput() {
    		@Override
    		public void pidWrite(double output) {
    			mGyroPIDRate = output;
    		}
    	};
        
        mGyroPIDController = new PIDController(0.1, 0, 0, mGyro, mGyroPIDOutput);
        mGyroPIDController.setInputRange(-180, 180);
        mGyroPIDController.setOutputRange(-.2, .2);
        mGyroPIDController.setAbsoluteTolerance(1);
        
        mEncoderPIDController = new PIDController(1, 0.0, 1, mEncoder, mEncoderPIDOutput);
        mEncoderPIDController.setOutputRange(-1, 1);
        mEncoderPIDController.setAbsoluteTolerance(1);
    	
    	
    	mDistanceSetPoint = distance;
        mGyroSetpoint = mGyro.getAngle();
        
        
        
    	
        
    }
    
    @Override
	public void start() {
		mDrivetrain.highGear();
		mEncoderPIDController.setSetpoint(mDistanceSetPoint);
		mEncoderPIDController.enable();
		
		mGyroPIDController.setSetpoint(mGyroSetpoint);
		mGyroPIDController.enable();
		
	}
    
    @Override
	public void update() {
    	System.out.println("Encoder PID rate = " + mEncoderPIDRate + " Gyro PID Rate = " + mGyroPIDRate);
		mDrivetrain.setTankDriveSpeed(mEncoderPIDRate + mGyroPIDRate, mEncoderPIDRate + -mGyroPIDRate);
	}
    
    @Override
	public boolean isFinished() {
		if (mEncoderPIDController.onTarget() && mGyroPIDController.onTarget()) {
			return true;
		}
		return false;
	}
    
	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
		mDrivetrain.resetEncoders();
		mEncoderPIDController.disable();
		mGyroPIDController.disable();
		System.out.println("finished with drive straight action");
	}
    
}
