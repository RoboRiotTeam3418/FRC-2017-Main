package com.team3418.frc2017.auto.actions;

import com.team3418.frc2017.HardwareMap;
import com.team3418.frc2017.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class DriveStraightAction implements Action {
	
	private double mWantedDistance;
	private double mLeftDrivetrainEncoderDistance;
	private double mRightDrivetrainEncoderDistance;
	private double mLeftPIDControllerOutput;
	private double mRightPIDControllerOutput;

	private Drivetrain mDrivetrain = Drivetrain.getInstance();
	private Encoder mLeftDrivetrainEncoder = HardwareMap.getInstance().mLeftDrivetrainEncoder;
	private Encoder mRightDrivetrainEncoder = HardwareMap.getInstance().mRightDrivetrainEncoder;
	
	private PIDOutput mLeftPIDoutput = new PIDOutput() {
		
		@Override
		public void pidWrite(double output) {
			mLeftPIDControllerOutput = output;
		}
	};
	
	private PIDOutput mRightPIDOutput = new PIDOutput() {
		
		@Override
		public void pidWrite(double output) {
			mRightPIDControllerOutput = output;		
		}
	};
	
	private PIDController mLeftDrivetrainPIDController = new PIDController(0.25, 0.0, 0.1, mLeftDrivetrainEncoder, mLeftPIDoutput);
	private PIDController mRightDrivetrainPIDController = new PIDController(0.25, 0.0, 0.1, mRightDrivetrainEncoder, mRightPIDOutput);
	
    public DriveStraightAction(double distance) {
        mWantedDistance = distance*1000;
        mLeftDrivetrainPIDController.enable();
        mRightDrivetrainPIDController.enable();
    }
    
    public boolean isOnTarget() {
    	if (mLeftDrivetrainPIDController.onTarget() && mRightDrivetrainPIDController.onTarget()) {
    		return true;
    	} else {
    		return false;
    	}
    }

	@Override
	public void start() {
		mLeftDrivetrainEncoder.reset();
		mLeftDrivetrainPIDController.setAbsoluteTolerance(50);
		mLeftDrivetrainPIDController.setSetpoint(mWantedDistance);
		mLeftDrivetrainPIDController.setOutputRange(-.8, .8);
		
		mRightDrivetrainEncoder.reset();
		mRightDrivetrainPIDController.setAbsoluteTolerance(50);
		mRightDrivetrainPIDController.setSetpoint(mWantedDistance);
		mRightDrivetrainPIDController.setOutputRange(-.8, 8);
		
		mDrivetrain.highGear();
	}

	@Override
	public void update() {
		mLeftDrivetrainEncoderDistance = mLeftDrivetrainEncoder.getDistance();
		mRightDrivetrainEncoderDistance = mRightDrivetrainEncoder.getDistance();
		
		System.out.println("encoder differnece = " + (mLeftDrivetrainEncoderDistance - mRightDrivetrainEncoderDistance));
		System.out.println(mLeftPIDControllerOutput);
		mDrivetrain.setTankDriveSpeed(mLeftPIDControllerOutput, mRightPIDControllerOutput);
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
		mLeftDrivetrainPIDController.disable();
		mRightDrivetrainPIDController.disable();
		System.out.println("finished with drive straight action");
		
	}
    
}
