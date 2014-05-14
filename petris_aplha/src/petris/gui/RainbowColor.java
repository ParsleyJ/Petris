package petris.gui;

import java.awt.Color;

public class RainbowColor extends DynamicColor {

	private boolean rTG = false;
	private boolean phase1 = true;
	private boolean phase2 = false;
	private boolean phase3 = false;
	int step = 16;
	
	public enum Phase{RED,GREEN,BLUE};
	
	public RainbowColor()
	{
		super(0,0,255,255,20);
		start();
	}
	
	public RainbowColor(int speed)
	{
		super(0,0,255,255,20);
		
		step = Math.max(Math.min(speed, 255),0);
		start();
	}
	
	public RainbowColor(int speed, Phase phase)
	{
		super(0,0,255,255,20);
		setPhase(phase);
		step = Math.max(Math.min(speed, 255),0);
		start();
	}
	
	public RainbowColor(boolean redToGreen, int speed)
	{
		super(0,0,255,255,20);
		rTG = redToGreen;
		step = speed;
		start();
	}
	
	
	@Override
	void tick() {
		if(phase1)
		{
			if(getRed() + step >= 255)
			{
				phase1 =false;
				phase2 =true;
				phase3 =false;
				setRed(255);
				setBlue(0);
			}
			else 
			{
				setRed(getRed() + step);
				setBlue(getBlue() -step);
			}
		}
		
		if(phase2)
		{
			if(getGreen() + step >= 255)
			{
				phase1 =false;
				phase2 =false;
				phase3 =true;
				setGreen(255);
				setRed(0);
			}
			else 
			{
				setGreen(getGreen() + step);
				setRed(getRed() -step);
			}
		}
		
		if(phase3)
		{
			if(getBlue() + step >= 255)
			{
				phase1 =true;
				phase2 =false;
				phase3 =false;
				setBlue(255);
				setGreen(0);
			}
			else 
			{
				setBlue(getBlue() + step);
				setGreen(getGreen() -step);
			}
		}

	}
	
	public void setPhase(Phase phase)
	{
		switch(phase)
		{
		case RED:
		{
			setColor(new Color(0,0,255,255));
			phase1=true;
			phase2=false;
			phase3=false;
			break;
		}
		case GREEN:
		{
			setColor(new Color(255,0,0,255));
			phase1=false;
			phase2=true;
			phase3=false;
			break;
		}
		case BLUE:
		{
			setColor(new Color(0,255,0,255));
			phase1=false;
			phase2=false;
			phase3=true;
			break;
		}
		}
	}
	

}
