package petris;

import java.awt.Dimension;

public class TetrisGrid extends Grid {
	
	private Game currentGame;
	
	public TetrisGrid(Game curGame)
	{
		super();
		
		currentGame = curGame;
		
	}
	
	public TetrisGrid(Game curGame, Dimension d, int w, int h)
	{
		super(d.width,d.height,w,h);
		currentGame = curGame;
		
	}
	
   
	
	public ClassicPiece pieceAt(int x, int y) { return board[(y * BoardWidth) + x]; }
	
	private void removeFullLines()
    {
        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (pieceAt(j, i).isNoShape()) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                currentGame.removedLineAt(i);
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                         board[(k * BoardWidth) + j] = pieceAt(j, k + 1);
                }
            }
        }

        if (numFullLines > 0) {
            currentGame.linesSuccessfullyRemoved(numFullLines);
            //repaint();
        }
        else currentGame.noLinesRemoved();
        
    }
	
	
	
	public void dropPiece()
    {
        for (int i = 0; i < currentGame.curPiece.getNumberOfSquares(); ++i) {
        	int x = currentGame.curPiece.x() + currentGame.curPiece.x(i);
            int y = currentGame.curPiece.y() - currentGame.curPiece.y(i);
            board[(y * BoardWidth) + x] = new ClassicPiece(currentGame.curPiece.getShape());
        }
        removeFullLines();
    }
}
