package petris;

import java.awt.Dimension;

public class Grid {
	public int columns = 10;//TODO: parametrizzare!!
    public int rows = 22;//TODO: parametrizzare!!
    protected Dimension size;
    ClassicPiece[] board;
    
    
    
    
    public Grid()
    {
    	board = new ClassicPiece[columns * rows];
    	for(int i = 0; i < columns * rows; ++i)
    		board[i] = new ClassicPiece();
    	size = new Dimension(300, 660);
    	
    	//clear();    	
    }
    
    public Grid(int w, int h, int bw, int bh)
    {
    	this();
    	size = new Dimension(w, h);
    	columns = bw;
    	rows = bh;
    }
    
    
    public void setSize(Dimension arg)
    {
    	size = arg;
    }
    
    public Dimension getSize() {return size;}
    
    public int squareWidth() { return (int) size.getWidth() / columns; }
    
    public int squareHeight() { return (int) size.getHeight() / rows; }
    
    public void clear()
    {
        for (int i = 0; i < rows * columns; ++i)
            board[i].setNoShape();
    }
    
    public int[] getMaxHeights()
    {
    	int[] result = new int[columns];
    	int max = 0;
    	for (int i = 0; i < columns; ++i)
    	{
    		max = 0;
    		for (int j = 0; j < rows; ++j)
    		{
    			if (!board[j * columns + i].isNoShape())
    				max = Math.max(max, j + 1);
 
    		}
    		result[i] = max;
    	}
    	
    	return result;
    }
    
    public int getTopLine()
    {
    	int max = -1;
    	int[] h = getMaxHeights();
    	for (int i : h)
    	{
    		max = Math.max(max, i);
    	}
    	return max;
    }
    
    
    
}
