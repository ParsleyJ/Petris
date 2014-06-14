package petris.gui;

import java.awt.Color;
import java.awt.Dimension;

import petris.TetrisGrid;

public class LineAnimationLayer extends Layer {

	private int parentWidth;
	private int parentHeight;
	private int boxHeight;
	private Color bgColor = new Color(255,255,255);
	private int renderDelay = 20;//TODO: parametrizzare
	private int showTime;
	private int fadeInTicks;
	private int fadeOutTicks;
	private boolean isStarted = false;
	private int currentAlpha;
	private int alphaMax;
	private int alphaStep;
	private TetrisGrid tetrisGrid;
	private int currentLine;
	//private PriorityQueue<Msg> messages;
	private boolean permanent;

	public LineAnimationLayer(int pw, int ph, TetrisGrid grid, int maxalpha) {
		parentWidth = pw;
		parentHeight = ph;
		bgColor = new Color(50,50,50,0);
		tetrisGrid = grid;
		showTime = 0;
		fadeInTicks = 0;
		fadeOutTicks = 0;
		currentAlpha = 0;
		alphaStep = 0;
		alphaMax = maxalpha;
		boxHeight = tetrisGrid.squareHeight();
		currentLine = -1;
		//messages = new PriorityQueue<Msg>();
	}
	
	
	public void setSize(Dimension size)
	{
		parentWidth= size.width;
		parentHeight= size.height;
	}
	
	public void fadeInStep()
	{
		currentAlpha += alphaStep;
		if(!(currentAlpha > alphaMax));
		{
			bgColor = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), currentAlpha);
		}
		
		framePaint();
		--fadeInTicks;
	}
	
	public void framePaint()
	{
		graphics.setColor(bgColor);
		int sqy = parentHeight - (currentLine + 1) * tetrisGrid.squareHeight();
		graphics.fillRect(0, sqy, parentWidth - 1, boxHeight);
		
		//System.out.println(bgColor.getAlpha() + " | " + currentLine + " | " + sqy); //bubble for debug ;)
	}
	
	public void fadeOutStep()
	{
		currentAlpha -= alphaStep;
		if(!(currentAlpha < 0));
		{
			bgColor = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), Math.max(currentAlpha, 0));
			
		}
		framePaint();
		--fadeOutTicks;
	}
	
	public void paint()
	{
		if(!isStarted) return;
		if(fadeOutTicks <= 0)
		{
			isStarted = false;
			currentAlpha = fadeOutTicks = fadeInTicks = showTime = 0;
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
					framePaint();
					--showTime;
				}
				else
				{
					fadeInStep();
				}
			}
		}
	}
	
	public void reset()
	{
		showTime = 0;
		fadeInTicks = 0;
		fadeOutTicks = 0;
		currentAlpha = 0;
		alphaStep = 0;
	}
	
	public void show(int gridX, Color color, int messageTime, int fTime)
	{
		
		if (!isStarted)
			isStarted = false;
		reset();
		bgColor = color;
		showTime = messageTime / renderDelay;
		permanent = !(messageTime >= 0);
		fadeOutTicks = fadeInTicks = fTime / renderDelay; 
		alphaStep = alphaMax / (fTime / renderDelay);
		currentLine = gridX;
		isStarted = true;
		
	}
	
	public void fadeOut()
	{
		permanent = false;
		showTime = 0;
		fadeInTicks = 0;
		
	}

}


