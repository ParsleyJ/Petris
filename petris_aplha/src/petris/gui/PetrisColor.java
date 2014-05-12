package petris.gui;

import java.awt.Color;

public class PetrisColor {

	private boolean isDynamic;
	private DynamicColor dynamicC;
	private Color staticC;
	
	public PetrisColor()
	{
		isDynamic = false;
		dynamicC = null;
		staticC = new Color(0,0,0);
	}
	
	public PetrisColor(Color c)
	{
		isDynamic = false;
		dynamicC = null;
		staticC = c;
	}
	
	public PetrisColor(DynamicColor c)
	{
		isDynamic = true;
		dynamicC = c;
		staticC = null;
	}
	
	public boolean isDynamic() {
		return isDynamic;
	}

	public void setColor(DynamicColor dynam) {
		dynamicC = dynam;
		isDynamic = true;
	}
	
	public void setColor(Color stat) {
		staticC = stat;
		isDynamic = false;
	}
	
	public Color getColor()
	{
		if (isDynamic)
			return dynamicC.getStaticColor();
		else
			return staticC;
	}
	
	public void setAlpha(int alpha)
	{
		if (alpha < 0 || alpha > 255) throw new RuntimeException("Alpha out of range");
		if (isDynamic) dynamicC.setAlpha(alpha);
		else staticC = new Color(staticC.getRed(),staticC.getGreen(),staticC.getBlue(),alpha);
	}
	
}
