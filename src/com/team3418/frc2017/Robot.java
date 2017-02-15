package com.team3418.frc2017;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.team3418.frc2017.subsystems.Agitator;
import com.team3418.frc2017.subsystems.Climber;
import com.team3418.frc2017.subsystems.Climber.ClimberState;
import com.team3418.frc2017.subsystems.Drivetrain;
import com.team3418.frc2017.subsystems.Intake;
import com.team3418.frc2017.subsystems.Shooter;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	//initalize main parts of the robot
	HardwareMap mHardwareMap;
	ControlBoard mControlBoard;
	//Accelerometer mAccelerometer;
	//AnalogGyro mAnalogGyro;
	//I2C mI2c;
		
	//initialize subsystems
	Agitator mAgitator;
	Climber mClimber;
	Drivetrain mDrivetrain;
	Intake mIntake;
	Shooter mShooter;
	
	//Vision mVision;
	
	Pipeline mPipeline;
	
	public UsbCamera camera1;
	
	SendableChooser<String> chooser;
	
	int x, y;
	
	public void updateAllSubsystems(){
		mAgitator.updateSubsystem();
		mClimber.updateSubsystem();
		mDrivetrain.updateSubsystem();
		mIntake.updateSubsystem();
		mShooter.updateSubsystem();
	}
	
	public void stopAllSubsystems(){
		mAgitator.stop();
		mClimber.stop();
		mDrivetrain.stop();
		mDrivetrain.highGear();
		mIntake.stop();
		mShooter.stopFeeder();
		mShooter.stop();
	}
	
	@Override
	public void robotInit() {
		
		chooser = new SendableChooser<>();
		chooser.addDefault("Default Auto", "0");
		chooser.addObject("My Auto", "1");
		SmartDashboard.putData("Auto choices", chooser);
		
		mHardwareMap = HardwareMap.getInstance();
		mControlBoard = ControlBoard.getInstance();
		//mAccelerometer = new ADXL345_I2C(Port.kOnboard,Range.k8G);
		//mAnalogGyro = new AnalogGyro(0);
		//mI2c = new I2C(Port.kOnboard,84);
		
		mAgitator = Agitator.getInstance();
		mClimber = Climber.getInstance();
		mDrivetrain = Drivetrain.getInstance();
		mIntake = Intake.getInstance();
		mShooter = Shooter.getInstance();
		
		
		
		mPipeline = new Pipeline();
		camera1 = new UsbCamera("Usb Camera 0", 0);
		
		camera1.setResolution(320, 240);
        camera1.setFPS(30);
        camera1.setExposureManual(20);
        camera1.setBrightness(0);
        camera1.setWhiteBalanceManual(4000);
        

        
        
			Thread mThread = new Thread(() -> {
	            CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
	            CvSource outputStream = CameraServer.getInstance().putVideo("Switcher", 320, 240);
	            Mat image = new Mat();
	            RotatedRect target;
	            
	            while(!Thread.interrupted()) {
	                cvSink1.grabFrame(image);
	                mPipeline.process(image);
	        		for(int i = 0; i < mPipeline.findContoursOutput().size(); i++){
	        			//System.out.println(mPipeline.findContoursOutput().get(i).toArray());
	        			Imgproc.ellipse(image, Imgproc.minAreaRect(new MatOfPoint2f(mPipeline.findContoursOutput.get(i).toArray())), new Scalar(255, 0, 255), 3);
	        			target = Imgproc.minAreaRect(new MatOfPoint2f(mPipeline.findContoursOutput.get(i).toArray()));
	        			System.out.println(("target (" + i + ") X position = " + target.boundingRect().x));
	        		}
	                outputStream.putFrame(image);
	                //System.out.println(mPipeline.filterContoursOutput());
	            }
	        });
	        mThread.start();
		
		
		
		/*
		camera1 = CameraServer.getInstance().startAutomaticCapture(0);
		
		mVision = new Vision("Vision_Test", camera1, 320, 240, 30);
		mVision.setCameraParameters(20, 4000, 0);
		mVision.setMaskParameters(95, 255, 244, 63, 122, 18);
		mVision.startVision();
		*/
		
		stopAllSubsystems();
	}
	
	@Override
	public void autonomousInit() {
		stopAllSubsystems();
		updateAllSubsystems();
		x = 0;
		y = 0;
	}
	
	@Override
	public void autonomousPeriodic() {
		//All of this is still being tested, do not play with it unless you know what you're doing
		//Uncomment below to test the drive foreward function
		/*
		int x, y;
		System.out.println("Oh geeze here we go!");
		for(x = 0; x != 1000; x++){driveForwardAuto(); System.out.println(x);}
		System.out.println("I'M FINISHED DRIVING!");
		for(y = 0; y != 500; y++){turnRightAuto(); System.out.println(y);}
		System.out.println("I'M FINISHED TURNING!");
		mDrivetrain.setTankDriveSpeed(0, 0);
		*/

		if (x < 100){
			driveForwardAuto();
			System.out.println(x);
			x++;
		} else if (y < 100){
			turnRightAuto();
			System.out.println(y);
			y++;
		}
		
		
		
	}
		
	public void driveForwardAuto(){
		mDrivetrain.lowGear();
		mDrivetrain.setArcadeDriveSpeed(.75, 0);
	}
	
	public void turnRightAuto(){
		mDrivetrain.lowGear();
		mDrivetrain.setArcadeDriveSpeed(0, .75);
	}
	
	@Override
	public void disabledInit(){
		stopAllSubsystems();
		updateAllSubsystems();
		//mVision.stopVision();
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
		
		/*
		SmartDashboard.putNumber("Rectangle Area", mVision.getRectangleArea());
		SmartDashboard.putNumber("Rectangle Width", mVision.getRectangleWidth());
		SmartDashboard.putNumber("Rectangle Aspect", mVision.getRectangleAspect());
		SmartDashboard.putNumber("Rectangle Distance", mVision.getTargetDistanceFromCamera());
		SmartDashboard.putNumber("Rectangle X", mVision.getTargetScreenX());
		SmartDashboard.putNumber("Rectangle Y", mVision.getTargetScreenY());
		*/
		
		//intake
		if(mControlBoard.getSecondaryIntakeButton()) {
			mIntake.intake();
		} else {
			mIntake.stop();
		}
		
		//---------------------------------------------------------------
		
		//climber
		/*
		if (mControlBoard.getDriverClimberAxis()) {
			mClimber.forward();
		} else if (mControlBoard.getDriverClimberHoldButton()) {
			mClimber.hold();
		} else {
			mClimber.stop();
		}
		*/
		
		
		if (mControlBoard.getClimberForwardButton()) {
			mClimber.forward();
		} else if (mControlBoard.getClimberHoldButton()) {
			mClimber.hold();
		} else {
			mClimber.stop();
		}
		
		

		//-----------------------------------------------------------------
		
		//shooter
		if (mControlBoard.getSecondaryShootButton()) {
			mShooter.shoot();
		} else {
			mShooter.stop();
		}
		//---------------------------------------------------------------

		//  Drive train 
		if(mControlBoard.getDriverHighGearButton()) {
			mDrivetrain.highGear();
		}
		if(mControlBoard.getDriverLowGearButton()) {
			mDrivetrain.lowGear();
		}
		
		mDrivetrain.setTankDriveSpeed(mControlBoard.getDriverLeftY(), mControlBoard.getDriverRightY());
		
		if (mControlBoard.getDriverPov() > -1){
			switch(mControlBoard.getDriverPov()){
			//--------------------------------------------------
			case 0: //forwards
				mDrivetrain.setArcadeDriveSpeed(.5, 0);
				break;
			case 180:// back
				mDrivetrain.setArcadeDriveSpeed(-.5, 0);
				break;
			//--------------------------------------------------
			case 45: // forwards / right
				mDrivetrain.setArcadeDriveSpeed(.5, .25);
				break;
			case 315: // forwards / left
				mDrivetrain.setArcadeDriveSpeed(.5, -.25);
				break;
			//--------------------------------------------------
			case 90:// right
				mDrivetrain.setArcadeDriveSpeed(0, .5);
				break;
			case 270: //left
				mDrivetrain.setArcadeDriveSpeed(0, -.5);
				break;
			//--------------------------------------------------
			case 135:// back / right
				mDrivetrain.setArcadeDriveSpeed(-.5, -.25);
				break;
			case 225:// back / left
				mDrivetrain.setArcadeDriveSpeed(-.5, .25);
				break;
			//--------------------------------------------------
			}
		}
		//---------------------------------------------------
		
		
		
		
		
		//System.out.println(Math.random());
		
		
		updateAllSubsystems();
	}
	
	@Override
	public void testInit( ){
		stopAllSubsystems();
		updateAllSubsystems();
	}
	
	@Override
	public void testPeriodic() {
		
	}
}

