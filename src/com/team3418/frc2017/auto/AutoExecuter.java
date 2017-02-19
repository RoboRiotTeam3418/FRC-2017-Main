package com.team3418.frc2017.auto;

import com.team3418.frc2017.auto.actions.Action;
import com.team3418.frc2017.auto.actions.DriveStraightAction;

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
	SendableChooser<String> option11;
	SendableChooser<String> option12;
	SendableChooser<String> option13;
	SendableChooser<String> option14;
	SendableChooser<String> option15;
	SendableChooser<String> option16;
	SendableChooser<String> option17;
	SendableChooser<String> option18;
	SendableChooser<String> option19;
	SendableChooser<String> option20;
	
	
	public void Z1(){
		
	}
	
	public void Z2(){
		
	}
	
	public void Z3(){
		
	}
	
	public void Z4(){
		
	}
	
	public void Z5(){
		
	}
	
	public void Z6(){
		
	}
	
	public void Z7(){
		
	}
	
	public void Z8(){
		
	}
	
	public void Z9(){
		
	}
	
	public void Z10(){
		
	}
	
	public void Z11(){
		
	}
	
	public void Z12(){
		
	}
	
	public void Z13(){
		
	}
	
	public void H1(){
		
	}
	
	public void H2(){
		
	}
	
	public void H3(){
		
	}
	
	public void H4(){
		
	}
	
	public void H5(){
		
	}
	
	public void H6(){
		
	}
	
	public void G1(){
		
	}
	
	public void G2(){
		
	}
	
	public void G3(){
		
	}
	
	public void B1(){
		
	}
	
	public void B2(){
		
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
