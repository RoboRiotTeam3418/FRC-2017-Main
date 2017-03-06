package com.team3418.frc2017.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutoExecuter {
	//sendable choosers for each input box (0 - 14)
	SendableChooser<String> option1;
	SendableChooser<String> option2;
	SendableChooser<String> option3;
	SendableChooser<String> option4;
	SendableChooser<String> option5;
	SendableChooser<String> option6;
	SendableChooser<String> option7;
	SendableChooser<String> option8;
	SendableChooser<String> option9;
	SendableChooser<String> option10;
	
	public void option1(){
		
	}
	
	public void option2(){
		
	}
	
	public void option3(){
		
	}
	
	public void option4(){
		
	}
	
	public void option5(){
		
	}
	
	public void option6(){
		
	}
	
	public void option7(){
		
	}
	
	public void option8(){
		
	}
	
	public void option9(){
		
	}
	
	private AutoModeBase mAutoMode;
    private Thread m_thread = null;

    public void setAutoRoutine(AutoModeBase newAutoMode) {
        mAutoMode = newAutoMode;
    }
    
    public void start() {
        if (m_thread == null) {
            m_thread = new Thread() {
            	
            	
                public void run() {
                    if (mAutoMode != null) {
                        mAutoMode.run();
                    }
                }
            };
            m_thread.start();
        }
    }
    
    public void stop() {
        if (mAutoMode != null) {
            mAutoMode.stop();
        }
        m_thread = null;
    }
}
