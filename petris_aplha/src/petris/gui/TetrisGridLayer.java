package petris.gui;

import java.awt.Dimension;

import petris.ClassicPiece;
import petris.GraphicSquare;
import petris.GraphicSquare.SquareStyle;
import petris.TetrisGrid;

public class TetrisGridLayer extends Layer {
	private TetrisGrid grid;
	private SquareStyle style = SquareStyle.Border3d;
	
	public TetrisGridLayer(TetrisGrid gr)
	{
		super();
		grid = gr;
	}
	
	public void paint()
    { 
       

        Dimension size = grid.getSize();
        int boardTop = (int) size.getHeight() - grid.rows * grid.squareHeight();


        for (int i = 0; i < grid.rows; ++i) {
            for (int j = 0; j < grid.columns; ++j) {
                ClassicPiece pc = grid.pieceAt(j, grid.rows - i - 1);
                if (!(pc.isNoShape()))
                {
                	GraphicSquare sq = new GraphicSquare(0 + j * grid.squareWidth(),boardTop + i * grid.squareHeight(),grid.squareHeight(),grid.squareWidth(),
                			ClassicPiece.getColorFromShape(pc.getShape()),ClassicPiece.getColorFromShape(pc.getShape()),5, style);
                	sq.paint(graphics);
                }
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
