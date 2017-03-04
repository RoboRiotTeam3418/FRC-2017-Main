package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class TurnAction implements Action {

	private double mGyroSetpoint;
	private double mGyroPIDRate;

	private Drivetrain mDrivetrain;
	private ADXRS450_Gyro mGyro;
	private PIDController mGyroPIDController;
	private PIDOutput mGyroPIDOutput;
	
	
	
	
    public TurnAction(double angle) {
    	mDrivetrain = Drivetrain.getInstance();
    	mGyro = HardwareMap.getInstance().mGyro;
    	
        mGyroPIDOutput = new PIDOutput() {
    		@Override
    		public void pidWrite(double output) {
    			mGyroPIDRate = output;
    		}
    	};
        
        mGyroPIDController = new PIDController(0.01, 0, 0, 100000000, mGyro, mGyroPIDOutput);
        mGyroPIDController.setInputRange(-180, 180);
        mGyroPIDController.setOutputRange(-.25, .25);
        mGyroPIDController.setAbsoluteTolerance(1.0);
        mGyroPIDController.setContinuous(true);
        
        mGyroSetpoint = mGyro.getAngle();
        
        mGyroSetpoint = mGyro.getAngle() + angle;
    	if (angle > 180) angle -= 360;
    	if (angle < -180) angle += 360;
    	mGyroPIDController.setSetpoint((mGyro.getAngle() + angle + 180) % 360 - 180);
        
    	
        
    }
    
    @Override
	public void start() {
		mDrivetrain.highGear();
		
		mGyroPIDController.setSetpoint(mGyroSetpoint);
		mGyroPIDController.enable();
		
	}
    
    @Override
	public void update() {
    	
    	if (mGyroPIDRate < .2 && mGyroPIDRate > 0) { mGyroPIDRate = .25; }
    	if (mGyroPIDRate > -.2 && mGyroPIDRate <= 0) { mGyroPIDRate = -.25; }
    	
    	System.out.println(" Gyro PID Rate = " + mGyroPIDRate);
		mDrivetrain.setTankDriveSpeed(mGyroPIDRate, -mGyroPIDRate);
	}
    
    @Override
	public boolean isFinished() {
		if (mGyroPIDController.onTarget()) {
			return true;
		}
		return false;
	}
    
	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
		mGyroPIDController.disable();
		System.out.println("finished with turn action");
	}
}
