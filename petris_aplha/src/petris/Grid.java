package petris;

import java.awt.Dimension;

public class Grid {
	public int BoardWidth = 10;//TODO: parametrizzare!!
    public int BoardHeight = 22;//TODO: parametrizzare!!
    protected Dimension size;
    ClassicPiece[] board;
    
    
    
    
    public Grid()
    {
    	board = new ClassicPiece[BoardWidth * BoardHeight];
    	for(int i = 0; i < BoardWidth * BoardHeight; ++i)
    		board[i] = new ClassicPiece();
    	size = new Dimension(300, 660);
    	
    	//clear();    	
    }
    
    public Grid(int w, int h, int bw, int bh)
    {
    	this();
    	size = new Dimension(w, h);
    	BoardWidth = bw;
    	BoardHeight = bh;
    }
    
    
    public void setSize(Dimension arg)
    {
    	size = arg;
    }
    
    public Dimension getSize() {return size;}
    
    public int squareWidth() { return (int) size.getWidth() / BoardWidth; }
    
    public int squareHeight() { return (int) size.getHeight() / BoardHeight; }
    
    public void clear()
    {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i)
            board[i].setNoShape();
    }
    
    public int[] maxHeights()
    {
    	int[] result = new int[BoardWidth];
    	int max = 0;
    	for (int i = 0; i < BoardWidth; ++i)
    	{
    		max = 0;
    		for (int j = 0; j < BoardHeight; ++j)
    		{
    			if (!board[j * BoardWidth + i].isNoShape())
    				max = Math.max(max, j + 1);
 
    		}
    		result[i] = max;
    	}
    	
    	return result;
    }
    
    
}
