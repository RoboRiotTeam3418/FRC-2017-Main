package com.team3418.frc2017;

public class Constants {
	
    //PID gains for flywheel
    public static double kFlywheelKp = 0.1;
    public static double kFlywheelKi = 0.00035;
    public static double kFlywheelKd = 2.3;
    public static double kFlywheelKf = 0.037;
    public static int kFlywheelIZone = (int) (1023.0 / kFlywheelKp);
    public static double kFlywheelRampRate = 0;
    
	//Flywheel constants
    public static int kFlywheelAllowableError = 0;
    public static double kFlywheelOnTargetTolerance = 100.0;
    
    //speed constants
    public static double kRollerIntakeSpeed = 1;
	public static double kRollerReverseSpeed = -1;
	
    public static double kAgitatorFeedSpeed = -.75;
    public static double kAgitatorReverseSpeed = .75;
    
	public static double kClimberReverseSpeed = -1;
	public static double kClimberHoldSpeed = .1;
	
	public static double kFeederSpeed = .75;
	
	public static double kTargetShooterRpm = 1450;
	
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
	
	//SOLENOIDS (0-64)
	public static int kLeftShifterSolenoidId = 0;
	public static int kRightShifterSolenoidId = 1;
}
