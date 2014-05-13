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
}


