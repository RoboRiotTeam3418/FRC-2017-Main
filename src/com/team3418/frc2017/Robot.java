package com.team3418.frc2017;

import java.awt.image.VolatileImage;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.team3418.frc2017.auto.AutoExecuter;
import com.team3418.frc2017.auto.AutoModeBase;
import com.team3418.frc2017.auto.actions.Action;
import com.team3418.frc2017.auto.actions.DriveStraightAction;
import com.team3418.frc2017.auto.actions.DriveWithGearCamAction;
import com.team3418.frc2017.modes.DriveStraight;
import com.team3418.frc2017.modes.GearMiddle;
import com.team3418.frc2017.modes.LeftBoiler;
import com.team3418.frc2017.plugins.Pipeline;
import com.team3418.frc2017.plugins.Vision;
import com.team3418.frc2017.subsystems.Agitator;
import com.team3418.frc2017.subsystems.Climber;
import com.team3418.frc2017.subsystems.Drivetrain;
import com.team3418.frc2017.subsystems.Intake;
import com.team3418.frc2017.subsystems.Shooter;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
	//initalize main parts of the robot
	HardwareMap mHardwareMap;
	ControlBoard mControlBoard;
	
	//initialize subsystems
	Agitator mAgitator;
	Climber mClimber;
	Drivetrain mDrivetrain;
	Intake mIntake;
	Shooter mShooter;
	
	/*
	Vision mGearVision;
	Vision mShooterVision;
	*/
	/*
	UsbCamera mGearCamera;
	UsbCamera mShooterCamera;
	*/
	
	//Pipeline mPipeline;
	
	AutoExecuter mAutoExecuter = null;
	
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
		
		mHardwareMap = HardwareMap.getInstance();
		mControlBoard = ControlBoard.getInstance();
		
		mAgitator = Agitator.getInstance();
		mClimber = Climber.getInstance();
		mDrivetrain = Drivetrain.getInstance();
		mIntake = Intake.getInstance();
		mShooter = Shooter.getInstance();
		
		
		//mPipeline = new Pipeline();
		
		
		
		Thread t = new Thread(() -> {
			boolean allowcamera1 = false;
			
    		UsbCamera camera1 = new UsbCamera("camera1", 0);
            camera1.setResolution(320, 240);
            camera1.setFPS(30);
            camera1.setBrightness(0);
            camera1.setExposureManual(20);
            camera1.setWhiteBalanceManual(4000);
            
            UsbCamera camera2 = new UsbCamera("camera2", 1);
            camera2.setResolution(320, 240);
            camera2.setFPS(30);
            camera2.setBrightness(0);
            camera2.setExposureManual(20);
            camera2.setWhiteBalanceManual(4000);
            
            CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
            CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);

            CvSource outputStream1 = CameraServer.getInstance().putVideo("switcher2", 320, 240);
            
            Mat image = new Mat();
            ArrayList<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
            
            Pipeline mPipeline = new Pipeline();
            
            while(!Thread.interrupted()) {
            	
            	if (mControlBoard.getDriveCameraSwitcherButton()){
            		allowcamera1 = !allowcamera1;
            	}
            	
            	if (allowcamera1){
                    cvSink1.setEnabled(true);
                    cvSink2.setEnabled(false);
                    cvSink1.grabFrame(image);
            	} else {
                    cvSink2.setEnabled(true);
                    cvSink1.setEnabled(false);
                    cvSink2.grabFrame(image);
            	}
            	
            	System.out.println(allowcamera1);
            	mPipeline.process(image);
            	mContours = mPipeline.filterContoursOutput();
            	
            	for(int i = 0; i < mContours.size(); i++)
    			{
    				//Make a rectangle that fits the current contours
    				//System.out.println(Imgproc.boundingRect(mContours.get(i)).x);
    				Imgproc.rectangle(image, Imgproc.boundingRect(mContours.get(i)).br(), Imgproc.boundingRect(mContours.get(i)).tl(), new Scalar(255, 0, 255));
    				
    			}
                /*
                 * Skrrt Skrrt
                 * IT'S THE LOGIC ALERT!
                 */
                Imgproc.line(image, new Point(160, 0), new Point(160, 240) , new Scalar(255, 0, 255), 1);
                Imgproc.line(image, new Point(0, 120), new Point(340, 120) , new Scalar(255, 0, 255), 1);

                outputStream1.putFrame(image);
            }
        });
        t.start();
        
		
		/*
		mGearVision = new Vision("Gear_Vision", mGearCamera, mPipeline, 320, 240, 30);
		mGearVision.setCameraParameters(20, 4000, 0);
		
		mShooterVision = new Vision("ShooterVision", mShooterCamera, mPipeline, 320, 240, 30);
		mShooterVision.setCameraParameters(20, 4000, 0);
		
		mGearVision.startVision();
		mShooterVision.startVision();
		*/
		
		stopAllSubsystems();
	}
	
	@Override
	public void autonomousInit() {
		if (mAutoExecuter != null) {
            mAutoExecuter.stop();
        }
        mAutoExecuter = null;
        
        
        mAutoExecuter = new AutoExecuter();
        mAutoExecuter.setAutoRoutine(new DriveStraight());
        mAutoExecuter.start();
		
		stopAllSubsystems();
		updateAllSubsystems();
				
		x = 0;
		y = 0;
	}
	
	
	@Override
	public void autonomousPeriodic() {
		
		
	}

	@Override
	public void disabledInit(){
		
		if (mAutoExecuter != null) {
            mAutoExecuter.stop();
        }
        mAutoExecuter = null;
		
		stopAllSubsystems();
		updateAllSubsystems();
		//mGearVision.stopVision();
		//mShooterVision.stopVision();
	}
	
	@Override
	public void disabledPeriodic(){
		
	}
	
	@Override
	public void teleopInit(){
		
		if (mAutoExecuter != null) {
            mAutoExecuter.stop();
        }
        mAutoExecuter = null;
        
		stopAllSubsystems();
		mDrivetrain.lowGear();
		updateAllSubsystems();
	}
	
	@Override
	public void teleopPeriodic() {
		
		
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
		SmartDashboard.putNumber("Left_Drivetrain_Encoder_Distance", mHardwareMap.mLeftDrivetrainEncoder.getDistance());
		SmartDashboard.putNumber("Right_Drivetrain_Encoder_Distance", mHardwareMap.mRightDrivetrainEncoder.getDistance());
		
		updateAllSubsystems();
		
		if (mControlBoard.getDriverRightTrigger() > .2){
			mIntake.setRollerSpeed(mControlBoard.getDriverRightTrigger());
		}
		System.out.println(mControlBoard.getDriverRightTrigger());
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

