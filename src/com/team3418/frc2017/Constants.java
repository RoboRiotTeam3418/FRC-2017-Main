package com.team3418.frc2017;

public class Constants {
	
	//-------------------------------//
	//-subsystem speed motor speeds-//
	//-----------------------------//
	
    //PID
    public static double kFlywheelKp = 0.1;
    public static double kFlywheelKi = 0.00035;
    public static double kFlywheelKd = 2.3;
    public static double kFlywheelKf = 0.037;
    public static int kFlywheelIZone = (int) (1023.0 / kFlywheelKp);
    public static double kFlywheelRampRate = 0;
    
	//misc. (not used)
    public static int kFlywheelAllowableError = 0;
    public static double kFlywheelOnTargetTolerance = 100.0;
    
	//-------------------------------//
	//-subsystem motor speeds-//
	//-----------------------------//
    
    //Intake Roller
    public static double kRollerIntakeSpeed = .75;
	public static double kRollerReverseSpeed = -1;
	//instake speed is negative for comp bot and positive for practice bot
	
	//Agitator
    public static double kAgitatorFeedSpeed = -.50;
    public static double kAgitatorReverseSpeed = .75;
    
    //Feeder Wheel
	public static double kFeederSpeed = -.55;
	//feeder speed is negative for comp bot and positive for practice bot
    
	//Climber
	public static double kClimberReverseSpeed = -.5;
	public static double kClimberHoldSpeed = .40;
	
	//shooter
	public static double kTargetShooterRpm = 1785;
	
	//-------------//
	//-Autonomous-//
	//-----------//
	
	
	
	
	
	//--------------------------//
	//-static port assignments-//
	//------------------------//
	
	//Do not change anything below this line
	
	//PWM (0-9)
	public static int kLeftFrontMotorId = 0;
	public static int kLeftRearMotorId = 1;
	public static int kRightFrontMotorId = 2;
	public static int kRightRearMotorId = 3;
	
	public static int kIntakeRollerId = 4;
	public static int kClimberId = 5;
	public static int kFeederId = 6;
	public static int kAgitatorId = 7;
	
	//CAN (0-64)
	public static int kLeftShooterMotorId = 0;
	public static int kRightShooterMotorId = 1;
	
	//DIO (0-9)
	public static int kLeftEncoderChannelA = 1;
	public static int kLeftEncoderChannelB = 2;
	public static int kRightEncoderChannelA = 3;
	public static int kRightEncoderChannelB = 4;
	
	//SOLENOIDS (0-64)
	public static int kLeftShifterSolenoidId = 0;
	public static int kRightShifterSolenoidId = 1;
	public static int kMrGushySolenoid = 2;
}
