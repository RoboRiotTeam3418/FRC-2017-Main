package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.plugins.PID;
import com.team3418.frc2017.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Encoder;

public class DriveStraightAction implements Action {
	
	private double mWantedDistance;
	private double mLeftDrivetrainEncoderDistance;
	private double mRightDrivetrainEncoderDistance;
	private double mLeftPIDControllerOutput;
	private double mRightPIDCongtrollerOutput;

	private Drivetrain mDrivetrain = Drivetrain.getInstance();
	private Encoder mLeftDrivetrainEncoder = HardwareMap.getInstance().mLeftDrivetrainEncoder;
	private Encoder mRightDrivetrainEncoder = HardwareMap.getInstance().mRightDrivetrainEncoder;
	private PID mLeftDrivetrainPIDController = new PID(0.05, 0.0, 0.0, 1, 100);
	private PID mRightDrivetrainPIDController = new PID(0.05, 0.0, 0.0, 1, 100);
	
    public DriveStraightAction(double distance) {
        mWantedDistance = distance;
    }

	@Override
	public void start() {
		mLeftDrivetrainEncoder.reset();
		mRightDrivetrainEncoder.reset();
		mLeftDrivetrainPIDController.setParameters(mLeftDrivetrainEncoderDistance, mWantedDistance);
		mRightDrivetrainPIDController.setParameters(mRightDrivetrainEncoderDistance, mWantedDistance);
		mDrivetrain.highGear();
	}

	@Override
	public void update() {
		mLeftDrivetrainEncoderDistance = mLeftDrivetrainEncoder.getDistance();
		mRightDrivetrainEncoderDistance = mRightDrivetrainEncoder.getDistance();
		mLeftPIDControllerOutput = mLeftDrivetrainPIDController.calculateOutput();
		mRightPIDCongtrollerOutput = mRightDrivetrainPIDController.calculateOutput();
		
		
		System.out.println(mLeftDrivetrainEncoderDistance + " " + mLeftPIDControllerOutput + " " + mWantedDistance);
		
		mDrivetrain.setTankDriveSpeed(mLeftPIDControllerOutput, mRightPIDCongtrollerOutput);
	}

	@Override
	public boolean isFinished() {
		boolean output = false;
		if (mLeftDrivetrainEncoderDistance == mWantedDistance && mRightDrivetrainEncoderDistance == mWantedDistance) {
			output = true;
		}
		return output;
	}	

	@Override
	public void done() {
		
		
	}
    
}
