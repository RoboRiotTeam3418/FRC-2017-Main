package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.Constants;
import com.team3418.frc2017.HardwareMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends Subsystem {
	
	static Drivetrain mInstance = new Drivetrain();

    public static Drivetrain getInstance() {
        return mInstance;
    }
    
    
    
    Solenoid mLeftSolenoid;
    Solenoid mRightSolenoid;
    RobotDrive mDrive;
    
    public Encoder mLeftEncoder;
    public Encoder mRightEncoder;
	
    public Drivetrain(){
    	
		final double ticksPerRev = 1024;
		final double pi = 3.14;
		final double radius = 2;
		final double calculated = (2*pi)/ticksPerRev*radius;
    	
    	mLeftSolenoid = HardwareMap.getInstance().mLeftShifterSolenoid;
    	mRightSolenoid = HardwareMap.getInstance().mRightShifterSolenoid;
    	
    	mDrive = new RobotDrive(Constants.kLeftFrontMotorId,
    							Constants.kLeftRearMotorId,
    							Constants.kRightFrontMotorId,
    							Constants.kRightRearMotorId);
    	
    	mLeftEncoder = new Encoder(1, 2);
		mLeftEncoder.setDistancePerPulse(calculated);
		mLeftEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
		mLeftEncoder.setReverseDirection(false);
		mRightEncoder = new Encoder(3, 4);
		mRightEncoder.setDistancePerPulse(calculated);
		mRightEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
		mRightEncoder.setReverseDirection(true);
		}
    
    private DriveGear mDriveGear;
    
    private double mLeftSpeed;
    private double mRightSpeed;
    private double mMoveSpeed;
    private double mRotateSpeed;
    
    public enum DriveGear {
    	LOW, HIGH
    }
    
    public DriveGear getDriveGear(){
    	return mDriveGear;
    }
    
    public void resetEncoders(){
    	mLeftEncoder.reset();
    	mRightEncoder.reset();
    }
    
    public double getDistance(){
    	return (mLeftEncoder.getDistance() + mRightEncoder.getDistance()) / 2.0;
    }
	
    public void setTankDriveSpeed(double left, double right){
    	//System.out.println("Left speed = " + left + " right speed = " + right);
    	mLeftSpeed = left;
    	mRightSpeed = right;
    	mDrive.tankDrive(mLeftSpeed, mRightSpeed);
    }
    
    public void setArcadeDriveSpeed(double move, double rotate){
    	mRotateSpeed = rotate;
    	mMoveSpeed = move;
    	mDrive.arcadeDrive(move, rotate);
    }
    
    @Override
    public void stop(){
    	mDrive.stopMotor();
    }
    
    private void setLowGear() {
    	mLeftSolenoid.set(false);
    	mRightSolenoid.set(false);
    }
	
    private void setHighGear() {
    	mLeftSolenoid.set(true);
    	mRightSolenoid.set(true);
    }
	
	@Override
	public void updateSubsystem() {
		
		switch(mDriveGear){
		case HIGH:
			setHighGear();
			break;
		case LOW:
			setLowGear();
			break;
		default:
			setHighGear();
			break;
		}
		
		outputToSmartDashboard();
		
	}
	
	public void highGear(){
		mDriveGear = DriveGear.HIGH;
	}
	
	public void lowGear(){
		mDriveGear = DriveGear.LOW;
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("DriveTrain_LeftMotorSpeeds", mLeftSpeed);
		SmartDashboard.putNumber("DriveTrain_RightMotorSpeeds", mRightSpeed);
		SmartDashboard.putNumber("DriveTrain_MoveValue", mMoveSpeed);
		SmartDashboard.putNumber("DriveTrain_RotateValue", mRotateSpeed);
		SmartDashboard.putNumber("Right_Drivetrain_Encoder_Distance", mRightEncoder.getDistance());
		SmartDashboard.putString("Drive_Gear", mDriveGear.toString());
	}
}
