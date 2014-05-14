package petris.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public abstract class DynamicColor implements ActionListener {

	private int red;
	private int green;
	private int blue;
	private int alpha;
	
	protected void checkValuesAndThrow()
	{
		if (red < 0 || red > 255) throw new RuntimeException("Red out of range");
		if (green < 0 || green > 255) throw new RuntimeException("Green out of range");
		if (blue < 0 || blue > 255) throw new RuntimeException("Blue out of range");
		if (alpha < 0 || alpha > 255) throw new RuntimeException("Alpha out of range");
	}
	
	protected boolean checkValues()
	{
		if (red < 0 || red > 255) return false;
		if (green < 0 || green > 255) return false;
		if (blue < 0 || blue > 255) return false;
		if (alpha < 0 || alpha > 255) return false;
		return true;
	}
	
	private Timer timer;
	
	public DynamicColor()
	{
		this(Color.black);
	}
	
	public DynamicColor(Color c)
	{
		red = c.getRed();
		green = c.getGreen();
		blue = c.getBlue();
		alpha = c.getAlpha();
		timer = null;
	}
	
	public DynamicColor(Color c, int t)
	{
		red = c.getRed();
		green = c.getGreen();
		blue = c.getBlue();
		alpha = c.getAlpha();
		timer = new Timer(t,this);
	}
	
	public DynamicColor(int r, int g, int b, int a) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
		checkValuesAndThrow();
		timer = null;
		
	}
	
	public DynamicColor(int r, int g, int b, int a, int t) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
		checkValuesAndThrow();
		timer = new Timer(t,this);
		
	}
	
	public void setTimerDelay(int t)
	{
		if (timer == null) timer = new Timer(t,this);
		timer.setDelay(t);
	}
	
	public void start()
	{
		timer.start();
	}

	public void stop()
	{
		timer.stop();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		tick();
		
	}
	
	abstract void tick();
	
	public int getRed()
	{
		return red;
	}
	
	public int getBlue()
	{
		return blue;
	}
	
	public int getGreen()
	{
		return green;
	}
	
	public int getAlpha()
	{
		return alpha;
	}
	
	public void setRed(int r)
	{
		red = r;
	}
	public void setGreen(int g)
	{
		green = g;
	}
	public void setBlue(int b)
	{
		blue = b;
	}
	public void setAlpha(int a)
	{
		alpha = a;
	}
	public Color getStaticColor()
	{
		return new Color(red,green,blue,alpha);
	}
	public void setColor(Color c)
	{
		red = c.getRed();
		green = c.getGreen();
		blue = c.getBlue();
		alpha = c.getAlpha();
	}
	
	

}
