package petris.gui;


import java.awt.Color;
import java.awt.Font;

public class BottomMessageLayer extends Layer {
	private int parentWidth;
	private int parentHeight;
	private Color bgColor = new Color(255,255,255);
	private Color foreColor;
	private int renderDelay = 20;
	private int showTime;
	private int fadeTime;
	private int fadeInTicks;
	private int fadeOutTicks;
	private String message;
	private Font textFont;
	private boolean isStarted = false;
	private int currentAlpha;
	private int alphaMax;
	private int alphaStep;
	//private PriorityQueue<Msg> messages;

	public BottomMessageLayer(int pw, int ph, Font font, int maxalpha) {
		parentWidth = pw;
		parentHeight = ph;
		bgColor = new Color(50,50,50,0);
		textFont = font;
		showTime = 0;
		fadeInTicks = 0;
		fadeOutTicks = 0;
		message = "";
		currentAlpha = 0;
		alphaStep = 0;
		alphaMax = maxalpha;
		//messages = new PriorityQueue<Msg>();
	}
	
	public void fadeInStep()
	{
		currentAlpha += alphaStep;
		if(!(currentAlpha > alphaMax));
		{
			bgColor = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), currentAlpha);
			foreColor = new Color(foreColor.getRed(), foreColor.getGreen(), foreColor.getBlue(), currentAlpha);
		}
		
		framePaint();
		--fadeInTicks;
	}
	
	public void framePaint()
	{
		graphics.setColor(bgColor);
		graphics.fillRect(0, parentHeight - 20, parentWidth, 20);
		graphics.setColor(foreColor);
		graphics.setFont(textFont);
		graphics.drawString(message, 10, parentHeight - 5);
		//System.out.println(bgColor.getAlpha()); //bubble for debug ;)
	}
	
	public void fadeOutStep()
	{
		currentAlpha -= alphaStep;
		if(!(currentAlpha < 0));
		{
			bgColor = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), currentAlpha);
			foreColor = new Color(foreColor.getRed(), foreColor.getGreen(), foreColor.getBlue(), currentAlpha);
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
			currentAlpha = fadeTime = fadeOutTicks = fadeInTicks = showTime = 0;
			return;
		}
		else
		{
			if(showTime <= 0)
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
		message = "";
		currentAlpha = 0;
		alphaStep = 0;
	}
	
	public void show(String text, Color color, int messageTime, int fTime)
	{
		//messages.add(new Msg(text,color,messageTime,fTime));
		if (!isStarted)
			isStarted = false;
		reset();
		message = text;
		foreColor = color;
		showTime = messageTime / renderDelay;
		fadeOutTicks = fadeInTicks = fTime / renderDelay; 
		alphaStep = alphaMax / (fTime / renderDelay);
		isStarted = true;
	}
	/*
	private class Msg
	{
		public String text;
		public Color color;
		public int messageTime;
		public int fTime;
		public Msg(String t, Color c, int mt, int ft)
		{
			text = t;
			color = c;
			mt = messageTime;
			ft = fTime;
		}
	}
	*/

}
