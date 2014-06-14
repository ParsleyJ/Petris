package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import petris.ClassicPiece;
import petris.gui.GraphicSquare;
import petris.gui.GraphicSquare.SquareStyle;
import petris.gui.PanelLayer.DockSide;
import petris.TetrisGrid;

public class PieceLayer extends Layer {
	private ClassicPiece curPiece;
	private TetrisGrid grid;
	private int ox = 0;
	private int oy = 0;
	private int border = 5;
	private int alpha = 255;
	private SquareStyle style = SquareStyle.Border3d;
	
	public enum DockSide{Left, Right}	
	private int oox;
	private int ooy;
	private Dimension originalParentSize;
	private DockSide side;
	
	public void setOriginalPosition(int offsetX, int offsetY, Dimension siz, DockSide side)
	{
		oox = offsetX;
		ooy = offsetY;
		originalParentSize = siz;
		this.side = side;
	}
	
	public void updatePosition(Dimension newParentSize)
	{
		if (originalParentSize == null) return;
		switch(side)
		{
		case Left:
		{
			
		}
		case Right:
		{
			ox = oox - (originalParentSize.width - newParentSize.width);
		}
		}
	}
	
	public PieceLayer(TetrisGrid gr, ClassicPiece p)
	{
		super();
		curPiece = p;
		grid = gr;
		
	}
	
	public PieceLayer(TetrisGrid gr, ClassicPiece p, int offsetX, int offsetY, int b)
	{
		super();
		curPiece = p;
		grid = gr;
		ox = offsetX;
		oy = offsetY;
		border = b;
	}
	
	
	public PieceLayer(TetrisGrid gr, ClassicPiece p, int offsetX, int offsetY, int b, int a)
	{
		super();
		curPiece = p;
		grid = gr;
		ox = offsetX;
		oy = offsetY;
		border = b;
		alpha = a;
	}
	
	public void setPiece(ClassicPiece p)
	{
		curPiece = p;
	}
	
	public void setOffsets(int x, int y)
	{
		ox = x;
		oy = y;
	}
	
	public void paint()
    { 
       
        
        Dimension size = grid.getSize();
        int boardTop = (int) size.getHeight() - grid.rows * grid.squareHeight();


        
        if (!(curPiece.isNoShape())) {
            for (int i = 0; i < 4; ++i) {
                int x = curPiece.x() + curPiece.x(i);
                int y = curPiece.y() - curPiece.y(i);
                Color c = ClassicPiece.getColorFromShape(curPiece.getShape());
                Color c2 = new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha);
                GraphicSquare sq = new GraphicSquare(ox + x * grid.squareWidth(),boardTop + oy + (grid.rows - y - 1) * grid.squareHeight(),
                		grid.squareHeight(),grid.squareWidth(), c2, c2, border, style);
                sq.paint(graphics);
                
                
            }
        }
        
    }
	
	public void setSquareStyle(SquareStyle st) {
		style = st;
		
	}

	public SquareStyle getSquareStyle() {
		return style;
	}
}
