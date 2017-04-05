package com.team3418.frc2017.plugins;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class MinionVision {
	
	static MinionVision mInstance = new MinionVision();
	
	public static MinionVision getInstance() {
		return mInstance;
	}
	
	VisionThread visionThread;
	
	public MinionVision() {
		
	}
	
	public void startVision()
	{		
		if(visionThread == null)
		{
			System.out.println("visionthread = null (creating new thread)");
			visionThread = new VisionThread(20);
		}
		if (visionThread != null){
			System.out.println("starting vision");
			visionThread.start();
		}
	}
	
	public void stopVision()
	{
		if(visionThread != null)
		{	
			System.out.println("stopping vision");
			visionThread.interrupt();
			visionThread = null;
		}
	}
	
	
	public double getCombinedTargetX() {
		return visionThread.combinedTargetCenter;
	}
	
	public double getCombinedTargetWidth() {
		return visionThread.combinedTarget.width;
	}
	
	
	
	public double getRectangleWidth()
	{
		if(visionThread != null && visionThread.targetsFound > 0)
			return visionThread.target1.boundingRect().width;
		
		return 0;
	}
	
	public double getRectangleArea()
	{
		if(visionThread != null && visionThread.targetsFound > 0)
			return visionThread.target1.size.area();
			
		return 0;
	}
	
	public double getRectangleAspect()
	{
		if(visionThread != null && visionThread.targetsFound > 0)
			return visionThread.target1.size.width / visionThread.target1.size.height;
			
		return 0;
	}
	
	/*
	public double getTargetDistanceFromCamera()
	{
		if(visionThread != null && visionThread.targetsFound > 0)
			return visionThread.targetDistance;
			
		return 0;
	}
	*/
	
	public double getTargetScreenX()
	{
		if(visionThread != null && visionThread.targetsFound > 0)
			return visionThread.target1.boundingRect().x;
			
		return 0;
	}
	
	public double getTargetScreenY()
	{
		if(visionThread != null && visionThread.targetsFound > 0)
			return visionThread.target1.boundingRect().y;
			
		return 0;
	}
	
}

class VisionThread extends Thread {
	
	//Target filtering variables
	final double minTargetSize = 5; //The minimum area a rectangle has to take up on-screen to be considered as a target
	
	/*
	//Variables for calculating target distance
	//To use distance calculation correctly, you must measure the width of a target (in pixels) from a known distance away from the camera
	final double cameraFOV = 60; //The horizontal FOV of the camera (in degrees)
	final double fovPlaneDistance = 5; //The distance from the camera that the target's size was measured (in feet)
	final double targetWidth = 10.0; //The width of the target (this should be the width of the area actually seen by the camera)
	final double targetWidthPixels = 35; //The width of the target on-screen measured at "fovPlaneDistance" away from the camera (in pixels)
	final double targetWidthConversion = targetWidth / targetWidthPixels; //This creates a conversion to convert target width from pixels to feet
	*/
	UsbCamera camera;
	CvSink cvSink; //video in source
	CvSource cvSource; //video output
	
	Mat image;
	
	List<MatOfPoint> contours;
	
	Pipeline mPipeline;
	
	//The time (in milliseconds) that the vision thread should wait
	int threadWait;
	
	int runningcount;
	
	
	//These are variables returned by the vision system
	//Since this class is being executed on a separate thread, they have to be volatile to make sure no conflicts arise when another script tries to access them
	public volatile RotatedRect target1;
	public volatile RotatedRect target2;
	public volatile Rect combinedTarget;
	public volatile double combinedTargetCenter;
	
	public volatile int targetsFound;
	public volatile double targetDistance;
	
	//Called when an instance of this class is created
	public VisionThread (double framerate)
	{	
		System.out.println("creating visionthread");
		//camera setup
		camera = new UsbCamera("camera1", 0);
        camera.setResolution(320, 240);
        camera.setFPS(30);
        camera.setBrightness(0);
        camera.setExposureManual(20);
        camera.setWhiteBalanceManual(4000);
        
        cvSink = CameraServer.getInstance().getVideo(camera);
        cvSink.setEnabled(true);
        cvSource = CameraServer.getInstance().putVideo("GearCam", 320, 240);
        
    	image = new Mat();
        contours = new ArrayList<MatOfPoint>();
        mPipeline = new Pipeline();
		
		target1 = new RotatedRect();
		target2 = new RotatedRect();
		combinedTarget = new Rect();
		combinedTargetCenter = 0;
		targetsFound = 0;
		
		threadWait = Math.max((int)(Math.round(1.0 / framerate)) * 1000, 1);
		
		runningcount = 0;
	}

	
	public void run()
	{
		try
		{				 
			while(!Thread.interrupted()) {
				
				processImage();
				
				processTarget();				

				visionDebug();				

				Thread.sleep(Math.max(threadWait / 2, 1));
			}
		}
		catch(InterruptedException e) {}
	}
	
	void processImage() {
		
		cvSink.grabFrame(image);

		mPipeline.process(image);
    					
    	contours = mPipeline.filterContoursOutput();
	}
	
	void processTarget()
	{
		targetsFound = 0;
		
		if (contours.size() > 0)
		{
			int biggestAreaIndex = -1;
			int secondBiggestAreaIndex = -1;
			double largestArea = 0;
			double secondLargestArea = 0;
			RotatedRect currentRect = new RotatedRect();
					
			for(int i = 0; i < contours.size(); i++)
			{
				currentRect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
				
				if(isPossibleTarget(currentRect))
				{
					double area = currentRect.size.area();
					
					if(area > largestArea)
					{
						secondBiggestAreaIndex = biggestAreaIndex;
						secondLargestArea = largestArea;
						
						biggestAreaIndex = i;
						largestArea = area;
					}
					else if(area > secondLargestArea)
					{
						secondLargestArea = area;
						secondBiggestAreaIndex = i;
					}
				}
			}
			
			if(biggestAreaIndex >= 0)
			{
				target1 = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(biggestAreaIndex).toArray()));
				targetsFound++;
				
				//targetDistance = calculateDistance(combinedTarget.width);
			}
			
			if(secondBiggestAreaIndex >= 0)
			{
				target2 = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(secondBiggestAreaIndex).toArray()));
				targetsFound++;
			}
		}
	}
	
		void visionDebug()
		{
			Point tg1tl = target1.boundingRect().tl();
			Point tg1br = target1.boundingRect().br();
			Point tg2tl = target2.boundingRect().tl();
			Point tg2br = target2.boundingRect().br();
			
			if(target1 != null)
			{
				Imgproc.rectangle(image, tg1tl, tg1br, new Scalar(255, 0, 255), 1);
			}
			
			if(target2 != null)
			{
				Imgproc.rectangle(image, tg2tl, tg2br, new Scalar(255, 0, 255), 1);
			}
			
			if(target1 != null && target2 != null) {
				
				//if target 1 is on the right
				if(target1.center.x > target2.center.x){
					Imgproc.rectangle(image, tg1br, tg2tl, new Scalar(100, 100, 255), 1);
					combinedTarget = new Rect(tg1br, tg2tl);
				}
				//if target 1 is on the left
				else {
					Imgproc.rectangle(image, tg1tl, tg2br, new Scalar(100, 100, 255), 1);
					combinedTarget = new Rect(tg1tl, tg2br);
				}
			}
			
			
			combinedTargetCenter = (combinedTarget.tl().x + combinedTarget.br().x)*0.5;
			Imgproc.circle(image, new Point(combinedTargetCenter, 120), 0, new Scalar(43, 249, 32), 5);
			Imgproc.circle(image, new Point(160, 120), 0, new Scalar(240, 0, 0), 5);
			Imgproc.line(image, new Point(combinedTargetCenter, 120), new Point(160, 120), new Scalar(43, 249, 32), 2);
			
			
			cvSource.putFrame(image);
		}
	
	boolean isPossibleTarget(RotatedRect rect)
	{
		return rect.size.area() > minTargetSize;
	}
	
	/*
	double calculateDistance(double targetSizeX)
	{
		double targetWidthFt = targetSizeX * targetWidthConversion * 0.5;
		
		double targetAngle = Math.atan((targetWidthFt * Math.tan(cameraFOV * 0.5 * (Math.PI / 180.0))) / fovPlaneDistance);
		
		return (targetWidth * 0.5) / Math.tan(targetAngle);
	}
	*/
}
