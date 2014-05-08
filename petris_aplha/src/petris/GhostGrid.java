package petris;

public class GhostGrid extends Grid {
	TetrisGrid tetrisGrid;
	ClassicPiece curGhost;
	
	public GhostGrid(TetrisGrid tg)
	{
		tetrisGrid = tg;
		setSize(tg.getSize());//
		curGhost = new ClassicPiece();
	}
	
	public ClassicPiece pieceAt(int x, int y) { return board[(y * BoardWidth) + x]; }
	
	
	private boolean tryMoveGhost(ClassicPiece newPiece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (!tetrisGrid.pieceAt(x, y).isNoShape())
                return false;
        }

        curGhost = newPiece;
        curGhost.setX(newX);
        curGhost.setY(newY);
       
        //render.repaint();
        return true;
    }

	public void setGhost(ClassicPiece curPiece) {
		int newY = curPiece.y();
		curGhost = new ClassicPiece(curPiece);
		while(newY > 0)
		{
			if(!tryMoveGhost(curGhost, curGhost.x(),newY - 1))
				break;
			--newY;
			
		}
		for (int i = 0; i < curGhost.nsquares; ++i){
			int x = curGhost.x() + curGhost.x(i);
			int y = curGhost.y() - curGhost.y(i);
			board[(y * BoardWidth) + x] = curGhost;
		}
		
	}
}
