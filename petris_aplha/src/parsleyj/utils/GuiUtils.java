package parsleyj.utils;

import java.awt.Dimension;
import java.awt.Point;

public class GuiUtils {
	public static Point getCenteredChildRectCoords(Point parentCoords, Dimension parentSize, Dimension childSize)
	{
		Point cCoords = new Point(0,0);
		cCoords.x = parentCoords.x + (parentSize.width/2 - childSize.width/2);
		cCoords.y = parentCoords.y + (parentSize.height/2 + childSize.height/2);
		return cCoords;
	}
	public static Point getCenteredChildRectCoords2(Point parentCoords, Dimension parentSize, Dimension childSize)
	{
		Point cCoords = new Point(0,0);
		cCoords.x = parentCoords.x + (parentSize.width/2 - childSize.width/2);
		cCoords.y = parentCoords.y + (parentSize.height/2 - childSize.height/2);
		return cCoords;
	}
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;
	    
	    if (bound_width > original_width)
	    {
	    	if(bound_height > original_height)
	    	{
	    		double ratio = 1.0 * original_width / original_height;
	    		
	    		new_height = (int) ((double)bound_width/ratio);
	    		new_width = (int) ((double)bound_height*ratio);
	    		
	    		
	    		if(new_width<=bound_width)
	    			return new Dimension(new_width, bound_height);
	    		else
	    			return new Dimension(bound_width, new_height);
	    	}
	    }

	   /* // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }*/
	    
	    
	    

	    return new Dimension(new_width, new_height);
	}
}


