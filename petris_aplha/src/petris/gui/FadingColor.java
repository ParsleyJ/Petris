package petris.gui;

import java.awt.Color;

public class FadingColor extends DynamicColor {

	private int renderDelay = 20;
	private int showTime;
	private int fadeInTicks;
	private int fadeOutTicks;
	private boolean isStarted = false;
	private int currentAlpha;
	private int alphaMax = 255;
	private int alphaStep;
	private boolean permanent;
	
	public FadingColor(Color c)
	{
		super(c);
		super.setTimerDelay(renderDelay);
	}
	
	public FadingColor(Color c, int maxAlpha)
	{
		super(c);
		super.setTimerDelay(renderDelay);
		alphaMax = maxAlpha;
		
	}
	
	@Override
	void tick() {
		if(!isStarted) return;
		if(fadeOutTicks <= 0)
		{
			isStarted = false;
			currentAlpha = fadeOutTicks = fadeInTicks = showTime = 0;
			setAlpha(currentAlpha);
			return;
		}
		else
		{
			if(showTime <= 0 && !permanent)
			{
				fadeOutStep();
			}
			else
			{
				if(fadeInTicks <= 0)
				{
					
					--showTime;
				}
				else
				{
					fadeInStep();
				}
			}
		}

	}
	
	public void fadeInStep()
	{
		currentAlpha += alphaStep;
		if(!(currentAlpha > alphaMax));
		{
			setAlpha(currentAlpha);
		}
		
		--fadeInTicks;
	}
	
	public void fadeOutStep()
	{
		currentAlpha -= alphaStep;
		if(!(currentAlpha < 0));
		{
			setAlpha(currentAlpha);
		}
		
		--fadeOutTicks;
	}
	
	public void reset()
	{
		showTime = 0;
		fadeInTicks = 0;
		fadeOutTicks = 0;
		currentAlpha = 0;
		alphaStep = 0;
	}
	
	public void fadeOut()
	{
		permanent = false;
		showTime = 0;
		fadeInTicks = 0;
		currentAlpha = alphaMax;
		
	}
	
	public void fadeIn(int maxAlphaTime, int fadeTime)
	{
		reset();
		showTime = maxAlphaTime / renderDelay;
		permanent = !(maxAlphaTime >= 0); //if messageTime is negative, the color will be visible until fadeOut() is invoked.
		fadeOutTicks = fadeInTicks = fadeTime / renderDelay; 
		alphaStep = alphaMax / (fadeTime / renderDelay);
		isStarted = true;
		start();
	}
	
	/* TODO: implement this:
	public void fadeTo(int toAlpha, int fadeTime)
	{
		
	}
	*/

}
