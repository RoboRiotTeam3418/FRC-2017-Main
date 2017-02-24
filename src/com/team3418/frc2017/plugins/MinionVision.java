package com.team3418.frc2017.plugins;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.team3418.frc2017.ControlBoard;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class MinionVision {

	
	
	ControlBoard mControlBoard = ControlBoard.getInstance();
	
	
	
	public MinionVision() {
		
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
            	
            	mPipeline.process(image);
            	mContours = mPipeline.filterContoursOutput();
            	
            	for(int i = 0; i < mContours.size(); i++)
    			{
    				//Make a rectangle that fits the current contours
    				//System.out.println(Imgproc.boundingRect(mContours.get(i)).x);
    				Imgproc.rectangle(image, Imgproc.boundingRect(mContours.get(i)).br(), Imgproc.boundingRect(mContours.get(i)).tl(), new Scalar(255, 0, 255));
    				
    			}
                Imgproc.line(image, new Point(160, 0), new Point(160, 240) , new Scalar(255, 0, 255), 1);
                Imgproc.line(image, new Point(0, 120), new Point(340, 120) , new Scalar(255, 0, 255), 1);

                outputStream1.putFrame(image);
            }
        });
        t.start();
	}
}
