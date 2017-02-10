package com.team3418.frc2017.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.team3418.frc2017.Constants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {

	
	static Shooter mInstance = new Shooter();

    public static Shooter getInstance() {
        return mInstance;
    }
    
    CANTalon mLeftShooterTalon;
	CANTalon mRightShooterTalon;
	CANTalon mFeederTalon;
    
    public Shooter() {
    	//initialize shooter hardware settings
		System.out.println("Shooter Initialized");
		
		//Feeder Talon motor controller
		mFeederTalon = new CANTalon(Constants.kFeederId);
		System.out.println("cantalon Id set");
		mFeederTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		mFeederTalon.changeControlMode(TalonControlMode.PercentVbus);
		mFeederTalon.set(0);
		System.out.println("contol mode set");
		mFeederTalon.setPID(Constants.kFeederKp, Constants.kFeederKi, Constants.kFeederKd, Constants.kFeederKf, 
				Constants.kFeederIZone, Constants.kFeederRampRate, Constants.kFeederAllowableError);
		mFeederTalon.setProfile(0);
		System.out.println("pid set");
		
		mFeederTalon.reverseSensor(false);
		mFeederTalon.reverseOutput(true);
		
		mFeederTalon.setVoltageRampRate(0);
		mFeederTalon.enableBrakeMode(false);
		mFeederTalon.clearStickyFaults();
		
		mFeederTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		mFeederTalon.configPeakOutputVoltage(+0.0f, -12.0f);
		//
		
		System.out.println("feederdoneinit");
		
		//Left Talon Motor Controller
		mLeftShooterTalon = new CANTalon(Constants.kLeftShooterMotorId);			
		mLeftShooterTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		mLeftShooterTalon.changeControlMode(TalonControlMode.PercentVbus);
		mLeftShooterTalon.set(0);
		mLeftShooterTalon.setPID(Constants.kFlywheelKp, Constants.kFlywheelKi, Constants.kFlywheelKd, Constants.kFlywheelKf,
                Constants.kFlywheelIZone, Constants.kFlywheelRampRate, 0);
		mLeftShooterTalon.setProfile(0);
		mLeftShooterTalon.reverseSensor(false);
		mLeftShooterTalon.reverseOutput(true);
		
		mLeftShooterTalon.setVoltageRampRate(0);
		mLeftShooterTalon.enableBrakeMode(false);
		mLeftShooterTalon.clearStickyFaults();
		
		mLeftShooterTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		mLeftShooterTalon.configPeakOutputVoltage(+0.0f, -12.0f);
		mLeftShooterTalon.setAllowableClosedLoopErr(Constants.kFlywheelAllowableError);		
		//
		System.out.println("leftshooterdoneinit");
		
		//Right Talon Motor Controller
		mRightShooterTalon = new CANTalon(Constants.kRightShooterMotorId);
		mRightShooterTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		mRightShooterTalon.changeControlMode(TalonControlMode.PercentVbus);
		mRightShooterTalon.set(0);
		mRightShooterTalon.setPID(Constants.kFlywheelKp, Constants.kFlywheelKi, Constants.kFlywheelKd, Constants.kFlywheelKf,
                Constants.kFlywheelIZone, Constants.kFlywheelRampRate, 0);
		mRightShooterTalon.setProfile(0);
		mRightShooterTalon.reverseSensor(true);
		mRightShooterTalon.reverseOutput(true);
		
		mRightShooterTalon.setVoltageRampRate(0);
		mRightShooterTalon.enableBrakeMode(false);
		mRightShooterTalon.clearStickyFaults();
		
		mRightShooterTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		mRightShooterTalon.configPeakOutputVoltage(+0.0f, -12.0f);
		mRightShooterTalon.setAllowableClosedLoopErr(Constants.kFlywheelAllowableError);		
		
		mTargetShooterRpm = 0;
		mTargetFeederRpm = 0;
		//
		System.out.println("rightshooterdone init");
		System.out.println("shooter done initializing");
		}
    
    public enum ShooterReadyState {
    	READY, NOT_READY
    }
    
    public enum FeederState {
    	FEEDING, STOPPED
    }
    
    public enum ShooterState {
    	SHOOTING, STOPPED
    }

    
    //
    private double mTargetFeederRpm;
    private double mTargetShooterRpm;
    
    
    
    
    public void setTargetShooterRpm(double rpm){
    	mTargetShooterRpm = rpm;
    }
    
    public double getTargetShooterRpm(){
    	return mTargetShooterRpm;
    }
    
    public void setTargetFeederRpm(double rpm){
    	mTargetFeederRpm = rpm;
    }
    
    public double getTargetFeederRpm(){
    	return mTargetFeederRpm;
    }
    
    //
    
    //
    private ShooterReadyState mShooterReadyState;
    private ShooterState mShooterState;
    private FeederState mFeederState;
    
    public ShooterState getShooterState(){
    	return mShooterState;
    }
    
    public FeederState getFeederState(){
    	return mFeederState;
    }
    
    public ShooterReadyState getShooterReadyState(){
    	return mShooterReadyState;
    }
    //
    
    
    
	@Override
	public void updateSubsystem() {
		
		switch(mFeederState){
		case STOPPED:
			setFeederOpenLoop(0);
			break;
		case FEEDING:
			setFeederRpm(mTargetFeederRpm);
			break;
		default:
			stopFeeder();
			break;
		}
		
		switch(mShooterState){
		case STOPPED:
			setLeftShooterOpenLoop(0);
			setRightShooterOpenLoop(0);
			break;
		case SHOOTING:
			setShooterRpm(mTargetShooterRpm);
		default:
			stopShooter();
			break;
		}
		
		//printShooterInfo();
		outputToSmartDashboard();
		
		if (bothIsOnTarget()){
			mShooterReadyState = ShooterReadyState.READY;
		} else {
			mShooterReadyState = ShooterReadyState.NOT_READY;
		}
	}
	
	public void shoot(){
		mShooterState = ShooterState.SHOOTING;
	}
	
	public void stopShooter(){
		mShooterState = ShooterState.STOPPED;
	}
	
	public void feed(){
		mFeederState = FeederState.FEEDING;
	}
	
	public void stopFeeder(){
		mFeederState = FeederState.STOPPED;
	}
	
	
	
	/*print shooter info to console
	private void printShooterInfo(){
		System.out.println(" out: "+getMotorOutput()+
				   " spd: "+mMasterShooterTalon.getSpeed()+
		           " err: "+mMasterShooterTalon.getClosedLoopError()+
		           " trgrpm: "+getTargetRpm()+
		           " trgspd: "+getTargetSpeed());
	}
	*/
	
	
	//get shooter speed info methods
	private double getLeftRpm(){
		return mLeftShooterTalon.getSpeed();
	}
	
	private double getRightRpm(){
		return mRightShooterTalon.getSpeed();
	}
	
	private double getLeftSetpoint(){
		return mLeftShooterTalon.getSetpoint();
	}
	
	private double getRightSetpoint(){
		return mRightShooterTalon.getSetpoint();
	}
	//
	
	//set shooter speed methods
	public void setShooterRpm(double rpm){
		mLeftShooterTalon.changeControlMode(TalonControlMode.Speed);
		mLeftShooterTalon.set(rpm);
		mRightShooterTalon.changeControlMode(TalonControlMode.Speed);
		mRightShooterTalon.set(rpm);
	}
	
	public void setLeftShooterOpenLoop(double speed){
		mLeftShooterTalon.changeControlMode(TalonControlMode.PercentVbus);
		mLeftShooterTalon.set(speed);
	}
	
	public void setRightShooterOpenLoop(double speed){
		mRightShooterTalon.changeControlMode(TalonControlMode.PercentVbus);
		mRightShooterTalon.set(speed);
	}
	
	public void setFeederRpm(double rpm){
		mFeederTalon.changeControlMode(TalonControlMode.Speed);
		mFeederTalon.set(rpm);
	}
	
	public void setFeederOpenLoop(double speed){
		mFeederTalon.changeControlMode(TalonControlMode.PercentVbus);
		mFeederTalon.set(speed);
	}
	
	//
	
	//set shooter ready state
	private boolean leftIsOnTarget(){
		return (mLeftShooterTalon.getControlMode() == CANTalon.TalonControlMode.Speed
                && Math.abs(getLeftRpm() - getLeftSetpoint()) < Constants.kFlywheelOnTargetTolerance);
	}
	
	private boolean RightIsOnTarget(){
		return (mRightShooterTalon.getControlMode() == CANTalon.TalonControlMode.Speed
                && Math.abs(getRightRpm() - getRightSetpoint()) < Constants.kFlywheelOnTargetTolerance);
	}
	
	private boolean bothIsOnTarget(){
		return (leftIsOnTarget() && RightIsOnTarget());
	}
	//
	
	

	@Override
	public void outputToSmartDashboard() {
        SmartDashboard.putBoolean("Flywheel_On_Target", bothIsOnTarget());
        
		SmartDashboard.putNumber("Left_Flywheel_rpm", getLeftRpm());
		SmartDashboard.putNumber("Right_Flywheel_rpm", getRightRpm());
        SmartDashboard.putNumber("Feeder_Rpm", getTargetFeederRpm());
		
		SmartDashboard.putNumber("Target_Shooter_rpm", getTargetShooterRpm());
		SmartDashboard.putNumber("Target_Feeder_Rpm", getTargetFeederRpm());
		
        SmartDashboard.putNumber("Left_Flywheel_Setpoint", getLeftSetpoint());
        SmartDashboard.putNumber("Right_Flywheel_Setpoint", getRightSetpoint());
        
        SmartDashboard.putNumber("Left_Flywheel_Closed_Loop_error", mLeftShooterTalon.getClosedLoopError());
        SmartDashboard.putNumber("Left_Flywheel_Closed_Loop_error_Number", mLeftShooterTalon.getClosedLoopError());
        SmartDashboard.putNumber("Right_Flywheel_Closed_Loop_error", mRightShooterTalon.getClosedLoopError());
        SmartDashboard.putNumber("Right_Flywheel_Closed_Loop_error_Number", mRightShooterTalon.getClosedLoopError());
        SmartDashboard.putNumber("Feeder_Closed_Looop_error", mFeederTalon.getClosedLoopError());
        
        SmartDashboard.putNumber("Left_Flywheel_Output_Current", mLeftShooterTalon.getOutputCurrent());
        SmartDashboard.putNumber("Right_Flywheel_Output_Current", mRightShooterTalon.getOutputCurrent());
        
        SmartDashboard.putNumber("Left_Encoder_Velocity", mLeftShooterTalon.getEncVelocity());
        SmartDashboard.putNumber("Right_Encoder_Velocity", mRightShooterTalon.getEncVelocity());
        SmartDashboard.putNumber("Feeder_Encoder_Velovity", mFeederTalon.getEncVelocity());
        
        SmartDashboard.putNumber("Left_Closed_Loop_Ramp_Rate", mLeftShooterTalon.getCloseLoopRampRate());
        SmartDashboard.putNumber("Right_Closed_Loop_Ramp_Rate", mRightShooterTalon.getCloseLoopRampRate());
	}
    
    
    
    
}