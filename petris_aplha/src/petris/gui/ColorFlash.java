package petris.gui;

import java.awt.Color;

public class ColorFlash extends DynamicColor {

	private double cosAngle = 0;
	private double detail = 30.0;
	private double exponent = 20;
	private double speed = 0.5;
	
	private boolean loop = false;
	private boolean ascending = true;
	private int alphaStep = 5;
	private int maxAlpha = 100;
	private int minAlpha = 0;
	
	private boolean sinWave = false;
	
	public ColorFlash(Color c, int t)
	{
		super(c,t);
	}
	
	@Override
	void tick() {
		if( sinWave)
		{
			setAlpha((int)(Math.pow(Math.sin((cosAngle/detail)*Math.PI),exponent )*(maxAlpha - minAlpha) + minAlpha));
			cosAngle = (cosAngle + speed) % detail ;
		}
		else
		{
			if (ascending)
			{
				if (getAlpha() + alphaStep <= maxAlpha) setAlpha(getAlpha() + alphaStep);
				else ascending = false;
			}
			else
			{
				if (getAlpha() - alphaStep >= minAlpha) setAlpha(getAlpha() - alphaStep);
				else 
				{
					if (!loop)this.stop();
					ascending = true;
				}
			}
		}
	}
	
	public void flash()
	{
		this.start();
	}
	
	public void flash(Color c)
	{
		this.setColor(c);
		this.setAlpha(0);
		this.start();
	}

}
