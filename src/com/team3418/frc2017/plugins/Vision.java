package com.team3418.frc2017.plugins;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.CameraServer;

public class Vision 
{
	//This will hold an instance of the mCamera being used for vision
	public UsbCamera mCamera;
	
	//This will hold an instance of the Pipeline being used for frame processing
	public Pipeline mPipeline;
	
	//Cache the width and height of the image
	public int mImgWidth, mImgHeight;
	
	//This will hold the instance of the actual vision thread
	VisionThread mVisionThread;
	
	public String mVisionThreadName;
	
	//Framerate
	int mUpdateFrequency;
	
	
	//_________________________________________________________________________________________________________________
	//Called when this class is created
	public Vision(String name, UsbCamera videoStream, Pipeline pipeline, int width, int height, int updateFramerate)
	{
		mVisionThreadName = name;
		
		//save an instance of the Camera
		mCamera = videoStream;
		
		//save an instance of the pipeline
		mPipeline = pipeline;
		
		//set the Camera's resolution and FPS
		mCamera.setResolution(width, height);		
		mCamera.setFPS(updateFramerate);
		
		//save the image size and framerate
		mImgWidth = width;
		mImgHeight = height;
		mUpdateFrequency = updateFramerate;
	}
	//_________________________________________________________________________________________________________________
	
	
	
	//_________________________________________________________________________________________________________________
	//This function starts the vision thread
	public void startVision()
	{		//If there is no instance of the vision thread...
		if(mVisionThread == null)
		{
			//Create a new instance of the vision thread
			mVisionThread = new VisionThread(mVisionThreadName, mCamera, mPipeline, mImgWidth, mImgHeight, mUpdateFrequency);
		}
		
	//Actually run the thread
		mVisionThread.start();
	}
	//_________________________________________________________________________________________________________________

	
	
	//_________________________________________________________________________________________________________________
	//This function stops the vision thread
	public void stopVision()
	{
		//If an instance of the vision thread exists...
		if(mVisionThread != null)
		{	
			//Interrupt the vision thread - this sets the Thread.interrupted() parameter to true
			mVisionThread.interrupt();
		}
	}
	//_________________________________________________________________________________________________________________
	
	//_________________________________________________________________________________________________________________
	public void setCameraParameters(int exposure, int whiteBalance, int brightness)
	{
		mCamera.setExposureManual(exposure);
		mCamera.setBrightness(brightness);
		mCamera.setWhiteBalanceManual(whiteBalance);
	}
	//_________________________________________________________________________________________________________________
	
	//_________________________________________________________________________________________________________________
	public double getTarget1RectangleWidth()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget1.boundingRect().width;
		
		return 0;
	}
	//_________________________________________________________________________________________________________________
	
	//_________________________________________________________________________________________________________________
	public double getTarget2RectangleWidth()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget2.boundingRect().width;
		
		return 0;
	}
	//_________________________________________________________________________________________________________________
	
	//_________________________________________________________________________________________________________________
	public double getTarget1RectangleArea()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget1.size.area();
			
		return 0;
	}
	//_________________________________________________________________________________________________________________
	
	//_________________________________________________________________________________________________________________
		public double getTarget2RectangleArea()
		{
			if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
				return mVisionThread.mTarget2.size.area();
				
			return 0;
		}
		//_________________________________________________________________________________________________________________
	
	
	//_________________________________________________________________________________________________________________
	public double getTarget1RectangleAspect()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget1.size.width / mVisionThread.mTarget1.size.height;
			
		return 0;
	}
	//_________________________________________________________________________________________________________________
	
	//_________________________________________________________________________________________________________________
	public double getTarget2RectangleAspect()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget2.size.width / mVisionThread.mTarget1.size.height;
				
		return 0;
	}
	//_________________________________________________________________________________________________________________
	
	//_________________________________________________________________________________________________________________
	public double getTarget1DistanceFromCamera()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget1Distance;
			
		return 0;
	}
	//_________________________________________________________________________________________________________________

	//_________________________________________________________________________________________________________________
	public double getTarget2DistanceFromCamera()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget2Distance;
				
		return 0;
	}
	//_________________________________________________________________________________________________________________
		
	//_________________________________________________________________________________________________________________
	public double getTarget1ScreenX()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget1.boundingRect().x;
			
		return 0;
	}
	//_________________________________________________________________________________________________________________

	//_________________________________________________________________________________________________________________
	public double getTarget2ScreenX()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget2.boundingRect().x;
			
		return 0;
	}
	//_________________________________________________________________________________________________________________
	
	//_________________________________________________________________________________________________________________
	public double getTarget1ScreenY()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget1.boundingRect().y;
			
		return 0;
	}
	//_________________________________________________________________________________________________________________
	
	//_________________________________________________________________________________________________________________
	public double getTarget2ScreenY()
	{
		if(mVisionThread != null && mVisionThread.mTargetsFound > 0)
			return mVisionThread.mTarget2.boundingRect().y;
			
		return 0;
	}
	//_________________________________________________________________________________________________________________
	
	
	public double getTargetOffsetDegres(){
		return  60 * ((mImgWidth/2) - ((getTarget1ScreenX() + getTarget2ScreenX())/2)) / mImgWidth;
	}
	
	
}

//This is the class that will be executed on a separate thread
class VisionThread extends Thread
{
	
	//_________________________________________________________________________________________________________________
	String mThreadName;
	
	//Target filtering variables
	final double mMinTargetSize = 0; //The minimum area a rectangle has to take up on-screen to be considered as a target
	
	//Variables for calculating target distance
	//To use distance calculation correctly, you must measure the width of a target (in pixels) from a known distance away from the Camera
	final double mCameraFOV = 60; //The horizontal FOV of the mCamera (in degrees)
	final double mFovPlaneDistance = 5; //The distance from the mCamera that the target's size was measured (in feet)
	final double mTargetWidth = 10.0 / 12.0; //The width of the target (this should be the width of the area actually seen by the Camera)
	final double mTargetWidthPixels = 61; //The width of the target on-screen measured at "fovPlaneDistance" away from the Camera (in pixels)
	final double mTargetWidthConversion = mTargetWidth / mTargetWidthPixels; //This creates a conversion to convert target width from pixels to feet
	
	//References to get and put video frames
	CvSink mVideoIn;
	CvSource mVideoOut;
	
	//References to hold images
	Mat mInputImage;
	Mat mOutputImage;
	
	//pipeline to process image
	Pipeline mPipeline;
	
	List<MatOfPoint> mContours;
	
	//The time (in milliseconds) that the vision thread should wait
	int mThreadWait;
	
	//These are variables returned by the vision system
	//Since this class is being executed on a separate thread, they have to be volatile to make sure no conflicts arise when another script tries to access them
	public volatile RotatedRect mTarget1;
	public volatile RotatedRect mTarget2;
	public volatile int mTargetsFound;
	public volatile double mTarget1Distance;
	public volatile double mTarget2Distance;
	//_________________________________________________________________________________________________________________
	
	
	
	//_________________________________________________________________________________________________________________
	//Called when an instance of this class is created
	public VisionThread (String name, VideoSource videoSource, Pipeline pipeline, int width, int height, double framerate)
	{
		mThreadName = name;
		
		//get instance of pipeline
		mPipeline = pipeline;
		
		//Setup videoIn to serve video frames
		mVideoIn = CameraServer.getInstance().getVideo(videoSource);
		
		//Setup a video server that the smartdashboard can view
		mVideoOut = CameraServer.getInstance().putVideo(mThreadName, width, height);
		
		//Initialize the image references
		mInputImage = new Mat();
		mOutputImage = new Mat();
		
		//Initialize our target variables
		mTarget1 = new RotatedRect();
		mTarget2 = new RotatedRect();
		mTargetsFound = 0;
		
		//Calculate how long the thread should wait given a frequency (and ensure it waits at least 1 ms)
		mThreadWait = Math.max((int)(Math.round(1.0 / framerate)) * 1000, 1);
		
		mContours = new ArrayList<MatOfPoint>();
	}
	//_________________________________________________________________________________________________________________

	
	//_________________________________________________________________________________________________________________
	//This function is run on a separate thread when the visionThread.start() method is called
	public void run()
	{
		//Catch an InterruptedExceptions
		try
		{	
			//Loop until this thread is interrupted
			while(!Thread.interrupted())
			{				
				mVideoIn.grabFrame(mInputImage);
				
				/*
				//Process the image
				processImage();
				
				//Process the target
				processTarget();
				
				//Draws ellipses over the two tracked targets for debugging
				visionDebug();
				*/
				
				//Have this thread wait for some time
				Thread.sleep(Math.max(mThreadWait / 2, 1));
			}
			
		}
		catch(InterruptedException e) {}
	}
	//_________________________________________________________________________________________________________________
	
	
	
	//_________________________________________________________________________________________________________________
	//This function takes whatever image is stored in "inputImage" and processes it to find contours
	void processImage()
	{
		
		mPipeline.process(mInputImage);
		
		//Clear the list of contours
		mContours.clear();
		
		//Find all image contours and store them in the contours list
		mContours = mPipeline.filterContoursOutput();
	}
	//_________________________________________________________________________________________________________________
	
	
	
	//_________________________________________________________________________________________________________________
	//This function processes the HSV mask and works out target variables
	void processTarget()
	{
		//Reset the number of found targets
		mTargetsFound = 0;
		
		//If we have contours...
		if (mContours.size() > 0)
		{
			//Variables to cache values during the sorting process
			int biggestAreaIndex = -1;
			int secondBiggestAreaIndex = -1;
			double largestArea = 0;
			double secondLargestArea = 0;
			RotatedRect currentRect = new RotatedRect();
					
			//Find the two largest rectangles and store them into their respective indices
			for(int i = 0; i < mContours.size(); i++)
			{
				//Make a rectangle that fits the current contours
				currentRect = Imgproc.minAreaRect(new MatOfPoint2f(mContours.get(i).toArray()));
				
				//Only process this rectangle if it is possibly a target
				if(isPossibleTarget(currentRect))
				{
					//Store the current rectangle's area
					double area = currentRect.size.area();
					
					//If this rectangle's area is greater than the current largest area...
					if(area > largestArea)
					{
						//Save the previous largest rectangle as the second largest
						secondBiggestAreaIndex = biggestAreaIndex;
						secondLargestArea = largestArea;
						
						//Save the current rectangle as the largest
						biggestAreaIndex = i;
						largestArea = area;
					}//If this area isn't the largest, but is largest than the current second largest...
					else if(area > secondLargestArea)
					{
						//Save this rectangle as the second largest
						secondLargestArea = area;
						secondBiggestAreaIndex = i;
					}
				}
			}
			
			//Store the largest rectangle
			if(biggestAreaIndex >= 0)
			{
				mTarget1 = Imgproc.minAreaRect(new MatOfPoint2f(mContours.get(biggestAreaIndex).toArray()));
				mTargetsFound++;
				
				mTarget1Distance = calculateDistance(mTarget1.boundingRect().width);
			}
			
			//Store the second largest rectangle
			if(secondBiggestAreaIndex >= 0)
			{
				mTarget2 = Imgproc.minAreaRect(new MatOfPoint2f(mContours.get(secondBiggestAreaIndex).toArray()));
				mTargetsFound++;
				
				mTarget2Distance = calculateDistance(mTarget2.boundingRect().width);

			}
		}
	}
	//_________________________________________________________________________________________________________________
	
	
	
	//_________________________________________________________________________________________________________________
	//This function draws ellipses over the tracked targets
	void visionDebug()
	{
		if(mTarget1 != null)
		{
			Imgproc.ellipse(mInputImage, mTarget1, new Scalar(255, 0, 255), 3);
			Imgproc.rectangle(mInputImage, mTarget1.boundingRect().tl(), mTarget1.boundingRect().br(), new Scalar(255, 0, 255), 255, 3, mTargetsFound);
		}
		
		if(mTarget2 != null)
		{
			Imgproc.ellipse(mInputImage, mTarget2, new Scalar(0, 0, 255), 3);
			Imgproc.rectangle(mInputImage, mTarget2.boundingRect().tl(), mTarget2.boundingRect().br(), new Scalar(255, 0, 255), 255, 3, mTargetsFound);
		}
		
		Imgproc.line(mInputImage, new Point(160, 0), new Point(160, 240) , new Scalar(255, 0, 255), 3);
		
		//Put the processed image into the server so that the smartdashboard can view it
		mVideoOut.putFrame(mOutputImage);
	}
	//_________________________________________________________________________________________________________________

	
	
	//_________________________________________________________________________________________________________________
	//This function returns whether a rectangle could potentially be a target
	boolean isPossibleTarget(RotatedRect rect)
	{
		return rect.size.area() > mMinTargetSize;
	}
	//_________________________________________________________________________________________________________________

	
	
	//_________________________________________________________________________________________________________________
	//This function calculates how far a target is from the Camera
	double calculateDistance(double targetSizeX)
	{
		//Convert the target's width from pixels to feet and halve that, since we're only going to be using half of the image
		double targetWidthFt = targetSizeX * mTargetWidthConversion * 0.5;
		
		//Calculate the angle that the right-most edge of the target is from the center of a line extending straight from the Camera
		double targetAngle = Math.atan((targetWidthFt * Math.tan(mCameraFOV * 0.5 * (Math.PI / 180.0))) / mFovPlaneDistance);
		
		//Scale a triangle that fits the calculated target angle and has an end length of half the width of the target
		return (mTargetWidth * 0.5) / Math.tan(targetAngle);
	}
	//_________________________________________________________________________________________________________________
	
	
	
}