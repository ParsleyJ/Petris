package petris;

import java.awt.Color;
import java.util.Random;

import petris.gui.GraphicSquare;

public class ClassicPiece extends Item {
	
	private int[][] coords;
	public int nsquares = 4; //TODO: getter and setter
	private GraphicSquare[] squares;
	private TetrominoesId shape;
	
	private static Random random = new Random();
	
	public enum TetrominoesId { NOSHAPE, ZSHAPE, SSHAPE, LINE, 
        TSHAPE, SQUARE, LSHAPE, MIRRLSHAPE};
	
	private static int[][][] tetrominoesCoords = new int[][][] {
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
            { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
            { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
            { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
            { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
            { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } },
            { { 1, 0 },   { 1, -1 },  { -1, 0 },   { -1, -1 } }
        };
	
	private static Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), 
            new Color(102, 204, 102), new Color(102, 102, 204), 
            new Color(204, 204, 102), new Color(204, 102, 204), 
            new Color(102, 204, 204), new Color(218, 170, 0),
            new Color(200, 200, 200)
        };
	
	public static Color getColorFromShape(TetrominoesId t)
	{
		return colors[t.ordinal()];
	}
	
	public ClassicPiece()
	{
		coords = new int[nsquares][2];
		squares = new GraphicSquare[nsquares];
		for (int i = 0; i < nsquares; ++i)
			squares[i] = new GraphicSquare();
		for (int i = 0; i < nsquares; ++i) 
		{
			coords[i][0] = 0;
			coords[i][1] = 0;
		}
		shape = TetrominoesId.NOSHAPE;
	}
	
	public ClassicPiece(TetrominoesId sh)//classic tetrominoes shape based ctor
	{
		coords = new int[nsquares][2];
		squares = new GraphicSquare[nsquares];
		for (int i = 0; i < nsquares; ++i)
			squares[i] = new GraphicSquare();
		for (int i = 0; i < nsquares; ++i) 
		{
			coords[i][0] = tetrominoesCoords[sh.ordinal()][i][0];
			coords[i][1] = tetrominoesCoords[sh.ordinal()][i][1];
			squares[i].setBGColor(colors[sh.ordinal()]);
			squares[i].setDeepness(2);
		}
		shape = sh;
	}
	
	public ClassicPiece(ClassicPiece old)//copy ctor
    {
		super(old);
		nsquares = old.nsquares;
    	coords = new int[old.nsquares][2];
    	squares = new GraphicSquare[old.nsquares];
    	for (int i = 0; i < old.nsquares ; i++) {
    		coords[i][0] = old.coords[i][0];
    		coords[i][1] = old.coords[i][1];
    		squares[i] = new GraphicSquare(old.squares[i]);
        }
    	shape = old.shape;
    }
	
	public void setX(int index, int x) { coords[index][0] = x; }
    public void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }//cambiare questi nomi
    public int y(int index) { return coords[index][1]; }
    
    public TetrominoesId getShape() {return shape;}//cambiare tipo di ritorno
	
	public void setShape(TetrominoesId sh)//classic tetrominoes shape based
	{
	    for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = tetrominoesCoords[sh.ordinal()][i][j];
            }
        }
        shape = sh;
    }
	
	public void setRandomShape()
    {
    	
        int x = Math.abs(random.nextInt()) % 7 + 1;
        TetrominoesId[] values = TetrominoesId.values(); 
        setShape(values[x]);
        fillWithColor(values[x]);
        
    }
	
	public void fillWithColor(Color col)
	{
		for (int i = 0; i < nsquares; ++i) 
		{
			squares[i].setBGColor(col);
		}
	}
	
	
	public void fillWithColor(TetrominoesId id)
	{
		for (int i = 0; i < nsquares; ++i) 
		{
			squares[i].setBGColor(colors[id.ordinal()]);
		}
	}
	
	public boolean isNoShape()
	{
		if (shape == TetrominoesId.NOSHAPE) return true;
		return false;
	}
	public void setNoShape()
	{
		setShape(TetrominoesId.NOSHAPE);
		fillWithColor(TetrominoesId.NOSHAPE);
	}
	
	public int minX()
    {
      int m = coords[0][0];
      for (int i=0; i < nsquares; i++) {
          m = Math.min(m, coords[i][0]);
      }
      return m;
    }
	
	public int minY() 
    {
      int m = coords[0][1];
      for (int i=0; i < nsquares; i++) {
          m = Math.min(m, coords[i][1]);
      }
      return m;
    }
    
    public int maxY() 
    {
      int m = coords[0][1];
      for (int i=0; i < nsquares; i++) {
          m = Math.max(m, coords[i][1]);
      }
      return m;
    }
    
    public void rotateLeft() 
    {
        if (shape == TetrominoesId.SQUARE)
            return;

        //Piece result = new Piece(this);

        for (int i = 0; i < nsquares; ++i) {
            //this.setX(i, y(i));
            //this.setY(i, -x(i));
        	int oldx = coords[i][0];
        	coords[i][0] = coords[i][1];
        	coords[i][1] = -oldx;
        }
        //return result;
    }

    public void rotateRight()
    {
        if (shape == TetrominoesId.SQUARE)
            return;

        //Piece result = new Piece(this);
        
        for (int i = 0; i < nsquares; ++i) {
            //result.setX(i, -y(i));
            //result.setY(i, x(i));
        	int oldx = coords[i][0];
        	coords[i][0] = -coords[i][1];
        	coords[i][1] = oldx;
        }
        //return result;
    }
    
    public int getNumberOfSquares() {return nsquares;}
    
    public GraphicSquare getSquare(int index)
    {
    	return squares[index];
    }

	public void mirror() 
	{
		for (int i = 0; i < nsquares; ++i)
			coords[i][0] = -coords[i][0];
		
	}
	
}
