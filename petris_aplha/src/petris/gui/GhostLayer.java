package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import petris.GhostGrid;
import petris.ClassicPiece;
import petris.gui.GraphicSquare;
import petris.gui.GraphicSquare.SquareStyle;

public class GhostLayer extends Layer implements ActionListener {
	private GhostGrid grid;
	
	private boolean ascending;
	private int currentAlpha;
	private int alphaStep = 3;
	private int alphaMax = 200;
	private int alphaMin = 50;
	private Timer timer;

	private SquareStyle style  = SquareStyle.Border3d;
	
	public GhostLayer(GhostGrid gr)
	{
		super();
		grid = gr;
		ascending = true;
		currentAlpha = 150;
		timer = new Timer(20,this);
		timer.start();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (ascending)
		{
			if (currentAlpha + alphaStep > alphaMax) 
			{
				currentAlpha = alphaMax;
				ascending = false;
			}
			else currentAlpha += alphaStep;
		}
		else
		{
			if (currentAlpha - alphaStep < alphaMin) 
			{
				currentAlpha = alphaMin;
				ascending = true;
			}
			else currentAlpha -= alphaStep;
		}
		
	}
	
	
	public void paint()
    { 
        
        Dimension size = grid.getSize();
        int boardTop = (int) size.getHeight() - grid.rows * grid.squareHeight();
        
        for (int i = 0; i < grid.rows; ++i) 
        {
            for (int j = 0; j < grid.columns; ++j) 
            {
                ClassicPiece piece = grid.pieceAt(j, grid.rows - i - 1);
                if (!piece.isNoShape())
                {
                	Color bd = ClassicPiece.getColorFromShape(piece.getShape());
                	Color bg = bd.darker();
                	GraphicSquare sq = new GraphicSquare(0 + j * grid.squareWidth(), boardTop + i * grid.squareHeight(), 
                			grid.squareHeight(), grid.squareWidth(), setAlpha(bg,currentAlpha), bd, 1, style);
                	sq.paint(graphics);

                }
            }
                
        }
    }
	
	public Color setAlpha(Color c, int alpha)
	{
		alpha = Math.max(0, alpha);
		alpha = Math.min(255, alpha);
		return new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha);
	}


	public void setSquareStyle(SquareStyle st) {
		style = st;
		
	}

	public SquareStyle getSquareStyle() {
		return style;
	}

	
    
}


