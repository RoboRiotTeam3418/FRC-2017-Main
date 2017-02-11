package com.team3418.frc2017;

import com.team3418.frc2017.subsystems.Agitator;
import com.team3418.frc2017.subsystems.Climber;
import com.team3418.frc2017.subsystems.Drivetrain;
import com.team3418.frc2017.subsystems.Drivetrain.DriveGear;
import com.team3418.frc2017.subsystems.Intake;
import com.team3418.frc2017.subsystems.Shooter;
import com.team3418.frc2017.subsystems.Drivetrain.DriveGear;
import com.team3418.frc2017.subsystems.Shooter.ShooterReadyState;

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
		mAgitator.updateSubsystem();
		mClimber.updateSubsystem();
		mDrivetrain.updateSubsystem();
		mIntake.updateSubsystem();
		mShooter.updateSubsystem();
	}
	
	public void stopAllSubsystems(){
		mAgitator.stopAgitator();
		mClimber.stop();
		mDrivetrain.setTankDriveSpeed(0, 0);
		mIntake.stopIntakeRoller();
		mShooter.stopFeeder();
		mShooter.stopFeeder();
	}
	
	
	public void robotInit() {
		mAgitator = new Agitator();
		mClimber = new Climber();
		mDrivetrain = new Drivetrain();
		mIntake = new Intake();
		mShooter = new Shooter();
	}
	
	@Override
	public void autonomousInit() {
		stopAllSubsystems();
		updateAllSubsystems();
	}
	
	@Override
	public void autonomousPeriodic() {
		//All of this is still being tested, do not play with it unless you know what you're doing
		//Uncomment below to test the drive foreward function
		//driveForwardAuto();
		//austin is a dinugs
		
		
		
	}
		
		public void driveForwardAuto(){
			System.out.println("Oh geeze here we go!");
			mDrivetrain.LowGear();
			for(int x = 0; x != 1000; x++){
			mDrivetrain.setTankDriveSpeed(.1,.1);
			}
			System.out.println("I'M FINISHED!");
			System.out.println("I'M FINISHED!");
		}
		
		
		
		
	
	@Override
	public void disabledInit(){
		stopAllSubsystems();
		updateAllSubsystems();
	}
	
	@Override
	public void disabledPeriodic(){
		
	}
	
	@Override
	public void teleopInit(){
		stopAllSubsystems();
		updateAllSubsystems();
	}
	
	@Override
	public void teleopPeriodic() {
		
		
		//  Drive train 
		if(mControlBoard.getDriverHighGearButton() == true || mControlBoard.getSecondaryHighGearButton())
		{
			mDrivetrain.setDriveGear(DriveGear.HIGH);
		}
		if(mControlBoard.getDriverLowGearButton() == true || mControlBoard.getSecondaryLowGearButton())
		{
			mDrivetrain.setDriveGear(DriveGear.LOW);
		}
		
		mDrivetrain.setTankDriveSpeed(mControlBoard.getDriverLeftY(), mControlBoard.getDriverRightY());
		//---------------------------------------------------
		//intake
		
		if(mControlBoard.getSecondaryIntakeButton()==true )
		{
			mIntake.intakeIn();
		}
		else{mIntake.stopIntakeRoller();}
		//---------------------------------------------------------------
		
		//climber
		mClimber.setSpeed(mControlBoard.getClimberAxis());
		//-----------------------------------------------------------------
		
	if(mShooter.getShooterReadyState() == ShooterReadyState.READY)
	{ mAgitator.clockwiseAgitator();
	
	}else{mAgitator.stopAgitator();}
	
		
		
		
		
		
		
		
		
	}
	
	@Override
	public void testInit(){
		stopAllSubsystems();
		updateAllSubsystems();
	}
	
	@Override
	public void testPeriodic() {
		
	}
}

