package com.team3418.frc2017.plugins;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class MinionVisionTest {

	
	
	
	public MinionVisionTest() {
				
		Thread t = new Thread(() -> {
			
    		UsbCamera camera = new UsbCamera("camera1", 0);
            camera.setResolution(320, 240);
            camera.setFPS(30);
            camera.setBrightness(0);
            camera.setExposureManual(20);
            camera.setWhiteBalanceManual(4000);
            
            CvSink cvSink = CameraServer.getInstance().getVideo(camera);
            
            CvSource outputStream = CameraServer.getInstance().putVideo("switcher", 320, 240);
            
            Mat image = new Mat();
            /*
            ArrayList<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
            
            Pipeline mPipeline = new Pipeline();
            */
            while(!Thread.interrupted()) {
            	
                cvSink.grabFrame(image);
            	/*	
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
                */
                outputStream.putFrame(image);
            }
        });
        t.start();
	}
}