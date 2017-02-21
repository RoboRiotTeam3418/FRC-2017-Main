package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class DriveStraightAction implements Action, PIDOutput {
	
	private double mSetPoint;
	private double mPIDRate;

	private Drivetrain mDrivetrain = Drivetrain.getInstance();	
	private Encoder mEncoder = mDrivetrain.mRightEncoder;
	
	private PIDController mPIDController;
	
    public DriveStraightAction(double distance) {
        mSetPoint = distance;
        
        mPIDController = new PIDController(0.2, 0.0, 0.0, mEncoder, this);
        mPIDController.setOutputRange(-1, 1);
        mPIDController.setAbsoluteTolerance(10);
        
    }
    
    @Override
	public void start() {
		mDrivetrain.highGear();
		mPIDController.setSetpoint(mSetPoint);
		mPIDController.enable();
		
	}
    
    @Override
	public void update() {
    	System.out.println(mPIDController.getError());
		mDrivetrain.setTankDriveSpeed(mPIDRate, mPIDRate);
	}
    
    @Override
	public boolean isFinished() {
		if (mPIDController.onTarget()) {
			return true;
		}
		return false;
	}
    
	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
		mDrivetrain.resetEncoders();
		mPIDController.disable();
		System.out.println("finished with drive straight action");
	}
	
	@Override
	public void pidWrite(double output) {
		mPIDRate = output;
	}
    
}
