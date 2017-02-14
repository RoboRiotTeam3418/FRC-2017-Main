package com.team3418.frc2017.subsystems;

import com.team3418.frc2017.Constants;
import com.team3418.frc2017.HardwareMap;

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
	
    public Drivetrain(){
    	mLeftSolenoid = HardwareMap.getInstance().mLeftShifterSolenoid;
    	mRightSolenoid = HardwareMap.getInstance().mRightShifterSolenoid;
    	mDrive = new RobotDrive(Constants.kLeftFrontMotorId, Constants.kLeftRearMotorId, Constants.kRightFrontMotorId, Constants.kRightRearMotorId);
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
	
    public void setTankDriveSpeed(double left, double right){
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
    
    private void setLowGear() {//true
    	mLeftSolenoid.set(true);
    	mRightSolenoid.set(true);
    }
	
    private void setHighGear() {//false
    	mLeftSolenoid.set(false);
    	mRightSolenoid.set(false);
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
		SmartDashboard.putString("Drive_Gear", mDriveGear.toString());
	}
}
