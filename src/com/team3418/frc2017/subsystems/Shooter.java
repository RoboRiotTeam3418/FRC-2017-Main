package com.team3418.frc2017.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.team3418.frc2017.Constants;
import com.team3418.frc2017.HardwareMap;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {

	
	static Shooter mInstance = new Shooter();
	
    public static Shooter getInstance() {
        return mInstance;
    }
    
    CANTalon mLeftShooterTalon;
	CANTalon mRightShooterTalon;
	VictorSP mFeederVictor;
	
	Agitator mAgitator;
    
    public Shooter() {
    	
    	mAgitator = Agitator.getInstance();
    	
    	//initialize shooter hardware settings
		System.out.println("Shooter Initialized");
		
		//Feeder Talon motor controller
		mFeederVictor= HardwareMap.getInstance().mFeederTalon;
		mFeederVictor.setInverted(true);
		
		//Left Talon Motor Controller
		mLeftShooterTalon = new CANTalon(Constants.kLeftShooterMotorId);		
		mLeftShooterTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		mLeftShooterTalon.changeControlMode(TalonControlMode.PercentVbus);
		mLeftShooterTalon.set(0);
		mLeftShooterTalon.setPID(Constants.kFlywheelKp, Constants.kFlywheelKi, Constants.kFlywheelKd, Constants.kFlywheelKf,
                Constants.kFlywheelIZone, Constants.kFlywheelRampRate, 0);
		mLeftShooterTalon.setProfile(0);
		mLeftShooterTalon.reverseSensor(false);
		mLeftShooterTalon.reverseOutput(false);
		
		mLeftShooterTalon.setVoltageRampRate(0);
		mLeftShooterTalon.enableBrakeMode(false);
		mLeftShooterTalon.clearStickyFaults();
		
		mLeftShooterTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		mLeftShooterTalon.configPeakOutputVoltage(+12.0f, -0.0f);
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
		mRightShooterTalon.configPeakOutputVoltage(+.0f, -12.0f);
		mRightShooterTalon.setAllowableClosedLoopErr(Constants.kFlywheelAllowableError);		
		
		mTargetShooterRpm = Constants.kTargetShooterRpm;
		mFeederSpeed = Constants.kFeederSpeed;
		//
		System.out.println("rightshooterdone init");
		System.out.println("shooter done initializing");
	}
    
    public enum ShooterReadyState {
    	READY, NOT_READY
    }
    
    public enum FeederState {
    	FEED, STOP
    }
    
    public enum ShooterState {
    	SHOOT, STOP
    }
    
    
    private double mFeederSpeed;
    private double mTargetShooterRpm;
    
    public void setTargetShooterRpm(double rpm){
    	mTargetShooterRpm = rpm;
    }
    
    public double getTargetShooterRpm(){
    	return mTargetShooterRpm;
    }
    
    public void setTargetFeederSpeed(double speed){
    	mFeederSpeed = speed;
    }
    
    public double getTargetFeederSpeed(){
    	return mFeederSpeed;
    }
    
    
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
    
	@Override
	public void updateSubsystem() {
		
		switch(mShooterState){
		case SHOOT:
			setShooterRpm(mTargetShooterRpm);
			break;
		case STOP:
			setShooterOpenLoop(0);
			break;
		default:
			stop();
			break;
		}
		
		switch(mFeederState){
		case FEED:
			setFeederOpenLoop(Constants.kFeederSpeed);
			break;
		case STOP:
			setFeederOpenLoop(0);
			break;
		default:
			stopFeeder();
			break;
		}
		
		if (bothIsOnTarget()){
			mShooterReadyState = ShooterReadyState.READY;
			feed();
			mAgitator.feed();
		} else {
			mShooterReadyState = ShooterReadyState.NOT_READY;
			stopFeeder();
			mAgitator.stop();
		}
		
		outputToSmartDashboard();
		
	}
	
	public void shoot(){
		mShooterState = ShooterState.SHOOT;
	}
	
	@Override
	public void stop(){
		mShooterState = ShooterState.STOP;
	}
	
	public void feed(){
		mFeederState = FeederState.FEED;
	}
	
	public void stopFeeder(){
		mFeederState = FeederState.STOP;
	}
	
	//get shooter speed info
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
	
	//set shooter speed
	public void setShooterRpm(double rpm){
		mLeftShooterTalon.changeControlMode(TalonControlMode.Speed);
		mLeftShooterTalon.set(rpm);
		mRightShooterTalon.changeControlMode(TalonControlMode.Speed);
		mRightShooterTalon.set(rpm);
	}
	
	public void setShooterOpenLoop(double speed){
		mLeftShooterTalon.changeControlMode(TalonControlMode.PercentVbus);
		mLeftShooterTalon.set(speed);
		mRightShooterTalon.changeControlMode(TalonControlMode.PercentVbus);
		mRightShooterTalon.set(speed);
	}
	
	public void setFeederOpenLoop(double speed){
		mFeederVictor.set(speed);
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
	
    public boolean isShooterReady(){
    	return bothIsOnTarget();
    }
	//
	
	@Override
	public void outputToSmartDashboard() {
        SmartDashboard.putBoolean("Flywheel_On_Target", bothIsOnTarget());
        SmartDashboard.putBoolean("Flywheel_On_Target_Left", leftIsOnTarget());
        SmartDashboard.putBoolean("Flywheel_On_Target_Right", RightIsOnTarget());
        
		SmartDashboard.putNumber("Left_Flywheel_rpm", getLeftRpm());
		SmartDashboard.putNumber("Right_Flywheel_rpm", getRightRpm());
        SmartDashboard.putNumber("Feeder_Speed", mFeederVictor.getSpeed());
		
		SmartDashboard.putNumber("Target_Shooter_rpm", getTargetShooterRpm());
		SmartDashboard.putNumber("Target_Feeder_Speed", getTargetFeederSpeed());
        
        SmartDashboard.putNumber("Left_Flywheel_Closed_Loop_error", mLeftShooterTalon.getClosedLoopError());
        SmartDashboard.putNumber("Left_Flywheel_Closed_Loop_error_Number", mLeftShooterTalon.getClosedLoopError());
        SmartDashboard.putNumber("Right_Flywheel_Closed_Loop_error", mRightShooterTalon.getClosedLoopError());
        SmartDashboard.putNumber("Right_Flywheel_Closed_Loop_error_Number", mRightShooterTalon.getClosedLoopError());
        
        SmartDashboard.putString("Shooter_State", mShooterState.toString());
        SmartDashboard.putString("ShooterReady_State", mShooterReadyState.toString());
        SmartDashboard.putString("Feeder_State", mFeederState.toString());
	}
}