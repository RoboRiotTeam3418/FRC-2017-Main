package com.team3418.frc2017;

public class Constants {
	public static double kConstantVariableExample = 10.0;
	
	
	
	//Flywheel constants
    public static double kFlywheelOnTargetTolerance = 100.0;
    public static double kFlywheelRpmSetpoint = 4200.0;
    
    //PID gains for flywheel velocity
    public static double kFlywheelKp = 0.1;
    public static double kFlywheelKi = 0.00035;
    public static double kFlywheelKd = 2.3;
    public static double kFlywheelKf = 0.037;
    public static int kFlywheelIZone = (int) (1023.0 / kFlywheelKp);
    public static double kFlywheelRampRate = 0;
    public static int kFlywheelAllowableError = 0;
    
    //PID gains for feeder 
    public static double kFeederKp = 0.1;
    public static double kFeederKi = 0.0;
    public static double kFeederKd = 0.0;
    public static double kFeederKf = 0.0;
    public static int kFeederIZone = (int) (1023.0 / kFlywheelKp);
    public static double kFeederRampRate = 0;
    public static int kFeederAllowableError = 0;
	
	
	//Do not change anything below this line
	
	//PWM
	public static int kLeftFrontMotorId = 0;
	public static int kLeftRearMotorId = 1;
	public static int kRightFrontMotorId = 2;
	public static int kRightRearMotorId = 3;
	public static int kIntakeRollerId = 4;
	public static int kClimberId = 5;
	public static int kFeederId = 6;
	
	//CAN
	public static int kLeftShooterMotorId = 0;
	public static int kRightShooterMotorId = 1;
	public static int kIntakeSpinnerId = 2;
	
	//SOLENOIDS
	public static int kLeftShifterSolenoidId = 0;
	public static int kRightShifterSolenoidId = 1;
	
	public static double kRollerReverseSpeed = .75;
    public static double kRollerIntakeSpeed = -.75;
}
