package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class DriveStraightAction implements Action, PIDOutput {
	
	private double mWantedDistance;
	private double mRightPIDControllerOutput;

	private Drivetrain mDrivetrain = Drivetrain.getInstance();	
	
	private PIDController mRightDrivetrainPIDController = new PIDController(0.01, 0.0, 0.1, , );
	
    public DriveStraightAction(double distance) {
        mWantedDistance = distance;
        mRightDrivetrainPIDController.enable();
    }
    
    public boolean isOnTarget() {
    	if (mRightDrivetrainPIDController.onTarget()) {
    		return true;
    	} else {
    		return false;
    	}
    }

	@Override
	public void start() {
		
		mRightDrivetrainPIDController.setAbsoluteTolerance(20);
		mRightDrivetrainPIDController.setSetpoint(mWantedDistance);
		mRightDrivetrainPIDController.setOutputRange(-1, 1);
		
		mDrivetrain.highGear();
	}

	@Override
	public void update() {
		
		mDrivetrain.setTankDriveSpeed(mRightPIDControllerOutput, mRightPIDControllerOutput);
	}

	@Override
	public boolean isFinished() {
		boolean output = false;
		if (isOnTarget()) {
			output = true;
		}
		return output;
	}	

	@Override
	public void done() {
		mDrivetrain.setTankDriveSpeed(0, 0);
		mRightDrivetrainPIDController.disable();
		System.out.println("finished with drive straight action");
	}

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		
	}
    
}
