package com.team3418.frc2017;

import com.team3418.frc2017.subsystems.Agitator;
import com.team3418.frc2017.subsystems.Climber;
import com.team3418.frc2017.subsystems.Drivetrain;
import com.team3418.frc2017.subsystems.Intake;
import com.team3418.frc2017.subsystems.Shooter;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	//initialize subsystems
	Agitator mAgitator;
	Climber mClimber;
	Drivetrain mDrivetrain;
	Intake mIntake;
	Shooter mShooter;
	
	//other parts of the robot
	ControlBoard mControlBoard;
	
	
	
	
	public void updateAllSubsystems(){
		
	}
	
	public void stopAllSubsystems(){
		
	}
	
	@Override
	public void robotInit() {
		mAgitator = new Agitator();
		mClimber = new Climber();
		mDrivetrain = new Drivetrain();
		mIntake = new Intake();
		mShooter = new Shooter();
	}
	
	@Override
	public void autonomousInit() {
		
	}
	
	@Override
	public void autonomousPeriodic() {
		
	}
	
	@Override
	public void disabledInit(){
		
	}
	
	@Override
	public void disabledPeriodic(){
		
	}
	
	@Override
	public void teleopInit(){
		
	}
	
	@Override
	public void teleopPeriodic() {
		
	}
	
	@Override
	public void testInit(){
		
	}
	
	@Override
	public void testPeriodic() {
		
	}
}

