package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightActionNotWorking implements Action {
	
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
	
    public DriveStraightActionNotWorking(double distance) {
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
        
        mGyroPIDController = new PIDController(0.3, 0, 0, mGyro, mGyroPIDOutput);
        mGyroPIDController.setInputRange(-15, 15);
        mGyroPIDController.setOutputRange(-.075, .075);
        mGyroPIDController.setAbsoluteTolerance(0.5);
        
        mEncoderPIDController = new PIDController(.75, 0.0001, 3, mEncoder, mEncoderPIDOutput);
        mEncoderPIDController.setOutputRange(-.85, .85);
        mEncoderPIDController.setAbsoluteTolerance(.75);
        
    	mDistanceSetPoint = distance;
        mGyroSetpoint = mGyro.getAngle();
    }
    
    @Override
	public void start() {
		mDrivetrain.lowGear();
		mEncoderPIDController.setSetpoint(mDistanceSetPoint);
		mEncoderPIDController.enable();
		
		mGyroPIDController.setSetpoint(mGyroSetpoint);
		mGyroPIDController.enable();
	}
    
    @Override
	public void update() {
    	SmartDashboard.putNumber("Auto_Encoder_PIDOutput", mEncoderPIDController.getError());
    	SmartDashboard.putNumber("Gyro_PIDOutput", mGyroPIDController.getError());
    	System.out.println(mGyroSetpoint - mGyro.getAngle());
    	
    	mGyroPIDRate = mGyroPIDController.getError() * .05;
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
