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
    public static double kRollerIntakeSpeed = .8;
	public static double kRollerReverseSpeed = -1;
	
	//Agitator
    public static double kAgitatorFeedSpeed = -.75;
    public static double kAgitatorReverseSpeed = .75;
    
    //Feeder Wheel
	public static double kFeederSpeed = .75;
    
	//Climber
	public static double kClimberReverseSpeed = -.5;
	public static double kClimberHoldSpeed = .35;
	
	//shooter
	public static double kTargetShooterRpm = 1550;
	
	//-------------//
	//-Autonomous-//
	//-----------//
	
	//Drive Straight (distance) (timed)
	public static double kGyroMinSpeed = .03;
	public static double kGyroMaxSpeed = .5;
	public static double kGyroDeadzone = .25;
	
	//Drive Straight (distance)
	public static double kEncoderMinSpeed = .28;
	public static double kEncoderMaxSpeed = .75;
	public static double kEncoderDeadzone = 1;
	
	//Drive Straight (timed)
	public static double kTimedDriveSpeed = .65;
	
	//Turn (angle)
	public static double kTurnMinSpeed = .4;
	public static double kTurnMaxSpeed = .65;
	public static double kTurnDeadzone = .25;
	
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
}
