package petris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import petris.ClassicPiece.TetrominoesId;
import petris.GraphicSquare.SquareStyle;
import petris.gui.BackgroundLayer;
import petris.gui.BottomMessageLayer;
import petris.gui.ColorFlash;
import petris.gui.FlashingGravityColor;
import petris.gui.LineAnimationLayer;
import petris.gui.MessageLayer;
import petris.gui.PetrisColor;
import petris.gui.PieceLayer;
import petris.gui.GhostLayer;
import petris.gui.GuiLayer;
import petris.gui.LabelLayer;
import petris.gui.Render;
import petris.gui.RenderInterface;
import petris.gui.TetrisGridLayer;

public class Game implements ActionListener{
	
	public ClassicPiece curPiece;
	public ClassicPiece nextPiece;
	
	public Dimension gameSize;
	
	RenderInterface render;
	
	TetrisGrid tetriGrid;
	TetrisGridLayer tetriLayer;
	PieceLayer fallLayer;
	
	GhostGrid ghostGrid;
	GhostLayer ghostLayer;
	
	LineAnimationLayer removeLineAnimation;
	
	TetrisGrid nextGrid;
	PieceLayer nextLayer;
	
	LabelLayer pointsLabel;
	LabelLayer multiplierLabel;
	LabelLayer speedLabel;
	LabelLayer linesLabel;
	
	MessageLayer messageBox;
	MessageLayer secondMessage;
	MessageLayer thirdMessage;
	MessageLayer fourthMessage;
	BottomMessageLayer smallMessage;
	
	Timer timer;
	
    boolean isFallingFinished = false;
    boolean isStarted = false;
    boolean isPaused = false;
    
    Render guiRender;
    
    boolean wasPreviousSuccesful = false;
    
    int numLinesRemoved = 0;
    int speed = 1;
    public int points = 0;
    public int multiplier = 1;
    
    JFrame parent;//?
	
	
	Random randomGenerator = new Random();//used for animations
    
    private int powerAmmo;
    boolean infiniteAmmo;
    
    LabelLayer ammoLabel;
	private FlashingGravityColor gravityColor;
	private ColorFlash flashColor;
	
	SquareStyle curStyle = SquareStyle.Border3d;
	String curPower = "PROCRASTINATE";
	
	LabelLayer powerNameLabel;
	private int powerMaxAmmo = -1;
	
    
    public Game(Dimension d, RenderInterface r, Render g)
	{
    	gameSize = d;
    	
		curPiece = new ClassicPiece();
		
		tetriGrid = new TetrisGrid(this);
		tetriGrid.setSize(gameSize);
		tetriLayer = new TetrisGridLayer(tetriGrid);
		
		ghostGrid = new GhostGrid(tetriGrid);
		
		render = r;
		
		gravityColor = new FlashingGravityColor(0,20);
		
		BackgroundLayer bgLayer = new BackgroundLayer(new PetrisColor(gravityColor),gameSize);
		
		flashColor = new ColorFlash(new Color(0,0,0,0),20);
		
		BackgroundLayer bgLayer2 = new BackgroundLayer(new PetrisColor(flashColor),gameSize);
		render.addLayer(bgLayer);
		render.addLayer(bgLayer2);
		
		render.addLayer(tetriLayer);
		
		ghostLayer = new GhostLayer(ghostGrid);
		render.addLayer(ghostLayer);
		
		nextPiece = new ClassicPiece();
	    nextPiece.setRandomShape();
	    nextPiece.setX(tetriGrid.columns / 2 - 3);
        nextPiece.setY(tetriGrid.rows - 5 );
		
		fallLayer = new PieceLayer(tetriGrid,curPiece);
		render.addLayer(fallLayer);
		
		removeLineAnimation = new LineAnimationLayer((int)gameSize.getWidth(), (int)gameSize.getHeight(), tetriGrid, 200);
		render.addLayer(removeLineAnimation);
		
		pointsLabel = new LabelLayer("",new Font("Arial",Font.BOLD,18),15, 40, new Color(50,255,0), 100);
		multiplierLabel = new LabelLayer("",new Font("Arial",Font.BOLD,12),15, 76, new Color(0,255,255), 80);
		linesLabel = new LabelLayer("0",new Font("Arial",Font.BOLD,14),15, 112, Color.yellow, 100);
		speedLabel = new LabelLayer("",new Font("Arial",Font.BOLD,14),15, 148, new Color(255,0,0), 100);
		ammoLabel = new LabelLayer("0",new Font("Arial",Font.BOLD,14), 15, 186, Color.orange, 100);
		
		powerAmmo = 20;
		infiniteAmmo = false;
		
		if (g != null)//this never happens, at the moment
		{
			guiRender  = g;
			nextGrid = new TetrisGrid(this,new Dimension(150,334),10,22);
			nextLayer = new PieceLayer(nextGrid,nextPiece);
			guiRender.addLayer(nextLayer);
		}
		else //always this configuration
		{
			render.addLayer(new GuiLayer(new Dimension(80,200), 5, 5));
			render.addLayer(new GuiLayer(new Dimension(80,120), (int)gameSize.getWidth()-85, 5));
			render.addLayer(new LabelLayer("NEXT",new Font("Arial",Font.BOLD,16),(int)gameSize.getWidth()-70, 25, new Color(255,255,255), 80));
			render.addLayer(new LabelLayer("SCORE",new Font("Arial",Font.BOLD,12),15, 20, new Color(255,255,255), 80));
			render.addLayer(new LabelLayer("MULTIPLIER",new Font("Arial",Font.BOLD,10),15, 60, new Color(255,255,255), 80));
			render.addLayer(new LabelLayer("LINES",new Font("Arial",Font.BOLD,10),15, 94, new Color(255,255,255), 80));
			render.addLayer(new LabelLayer("SPEED",new Font("Arial",Font.BOLD,10),15, 132, new Color(255,255,255), 80));
			
			powerNameLabel = new LabelLayer("RELAUNCH",new Font("Arial",Font.BOLD,8),15, 170, new Color(255,255,255), 80);			
			render.addLayer(powerNameLabel);
			
			render.addLayer(pointsLabel);
			render.addLayer(multiplierLabel);
			render.addLayer(linesLabel);
			render.addLayer(speedLabel);
			render.addLayer(ammoLabel);
			
			smallMessage = new BottomMessageLayer((int)gameSize.getWidth(),(int)gameSize.getHeight(),new Font("Arial",0,12),200);
			render.addLayer(smallMessage);
			
			messageBox = new MessageLayer ((int)gameSize.getWidth(), (int)gameSize.getHeight(), 40, new Font("Arial",Font.BOLD,20), 230);
			render.addLayer(messageBox);
			
			secondMessage = new MessageLayer((int)gameSize.getWidth(), (int)gameSize.getHeight(), 20, 31, new Font("Arial",0,12), 230);
			render.addLayer(secondMessage);
			
			thirdMessage = new MessageLayer((int)gameSize.getWidth(), (int)gameSize.getHeight(), 20, 52, new Font("Arial",0,12), 230);
			render.addLayer(thirdMessage);
			
			fourthMessage = new MessageLayer((int)gameSize.getWidth(), (int)gameSize.getHeight(), 20, 73, new Font("Arial",0,12), 230);
			render.addLayer(fourthMessage);
			
			nextGrid = new TetrisGrid(this,new Dimension(150,334), 10, 22);
			nextLayer = new PieceLayer(nextGrid,nextPiece,215,-15,3,100);
			render.addLayer(nextLayer);
		}
		
		
		
	    timer = new Timer(500,this);
	    gravityColor.setTimerDelay(50);
	    
	}
    
    
    
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
        updateGui();
        render.repaint();
    }
    
    public void start()
    {
        

        isStarted = true;
        isPaused = false;
        isFallingFinished = false;
        numLinesRemoved = 0;
        points = 0;
        multiplier = 1;
        speed = 1;
        tetriGrid.clear();
        reloadAmmo();
        updateGui();
        newPiece();
        gravityColor.setGravity(0);
        gravityColor.setTimerDelay(50);
        timer.setDelay(500);
        timer.start();
        smallMessage.show("New game started", Color.green, 1500, 500);//what with simoultaneous message shows? ---> add a queue
        fadeOutMessages();
    }
    
    public void pause()
    {
        if (!isStarted)
            return;
        
        isPaused = !isPaused;
        if (isPaused) {
        	showPausedMenu();
            timer.stop();
            smallMessage.show("Paused", Color.yellow, 1500, 500);
        } else {
        	fadeOutMessages();
            timer.start();
            smallMessage.show("Resumed", Color.cyan, 1500, 500);
            //statusbar.setText(String.valueOf(numLinesRemoved));
        }
        render.repaint();
    }
    
   

	private void newPiece()
    {
    	curPiece.setShape(nextPiece.getShape());
    	//nextPiece = new Piece();
    	nextPiece.setRandomShape();
        //curPiece.setRandomShape();
        curPiece.setX(tetriGrid.columns / 2);
        curPiece.setY(tetriGrid.rows - 1 + curPiece.minY());

        if (!tryMove(curPiece, curPiece.x(), curPiece.y())) {
            gameOver();
        }
        ghostGrid.setGhost(curPiece);
        render.repaint();
        
        
    }
    
	public void gameOver()
	{
		curPiece.setShape(TetrominoesId.NOSHAPE);
		ghostGrid.setGhost(curPiece);
		ghostGrid.clear();
		
		timer.stop();
        isStarted = false;
        messageBox.show("Game Over!", Color.red, -1, 500);
        secondMessage.show("Press N to start a new game.", Color.white, -1, 500);
        thirdMessage.show("Press Y to change power.", Color.green, -1, 500);
	}
	
	public void showMainMenu()
	{
		curPiece.setShape(TetrominoesId.NOSHAPE);
		ghostGrid.setGhost(curPiece);
		ghostGrid.clear();
		tetriGrid.clear();
		timer.stop();
        isStarted = false;
        updateGui();
        messageBox.show("Petris", Color.cyan, -1, 500);
        secondMessage.show("Press N to start a new game.", Color.orange, -1, 500);
        thirdMessage.show("Press Y to change power.", Color.green, -1, 500);
        fourthMessage.show("Press T to change theme.", Color.red, -1, 500);
	}
	
	public void showPausedMenu() {
		messageBox.show("Paused", Color.white, -1, 300);
		secondMessage.show("Press P or Esc to resume", Color.green, -1, 300);
		thirdMessage.show("Press T to change theme", Color.orange, -1, 300);
		fourthMessage.show("Press Q to exit to main menu", Color.red, -1, 300);
	}
	
	private void fadeOutMessages() {
		messageBox.fadeOut();
		secondMessage.fadeOut();
		thirdMessage.fadeOut();
		fourthMessage.fadeOut();
	}
	
    public void oneLineDown()
    {
        if (!tryMove(curPiece, curPiece.x(), curPiece.y() - 1))
        {
    		pieceDropped();
        }
    }
    
    public boolean tryMoveLeft()
    {
    	return tryMove(curPiece, curPiece.x() - 1, curPiece.y());
    }
    
    public boolean tryMoveRight()
    {
    	return tryMove(curPiece, curPiece.x() + 1, curPiece.y());
    }
    
    public boolean tryRotateLeft()
    {
    	ClassicPiece tempPiece = new ClassicPiece(curPiece);
    	tempPiece.rotateLeft();
    	if(!canBeMoved(tempPiece, tempPiece.x(), tempPiece.y()))
		{
			return false;
		}
    	else
		{
    		curPiece.rotateLeft();
			return tryMove(curPiece, curPiece.x(), curPiece.y());
		}
    }
    
    public boolean tryRotateRight()
    {
    	ClassicPiece tempPiece = new ClassicPiece(curPiece);
    	tempPiece.rotateRight();
    	if(!canBeMoved(tempPiece, tempPiece.x(), tempPiece.y()))
		{
			return false;
		}
    	else
		{
    		curPiece.rotateRight();
			return tryMove(curPiece, curPiece.x(), curPiece.y());
		}
    }
    
    private boolean tryMove(ClassicPiece newPiece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= tetriGrid.columns || y < 0 || y >= tetriGrid.rows)
            {
        		return false;
            }
            if (!(tetriGrid.pieceAt(x, y).isNoShape()))
            {
        		return false;
            }
        }

        curPiece = newPiece;
        curPiece.setX(newX);
        curPiece.setY(newY);
        ghostGrid.clear();
        ghostGrid.setGhost(curPiece);
        render.repaint();
        return true;
    }
    
    private boolean canBeMoved(ClassicPiece newPiece, int newX, int newY)
    {
    	for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= tetriGrid.columns || y < 0 || y >= tetriGrid.rows)
            {
        		return false;
            }
            if (!(tetriGrid.pieceAt(x, y)).isNoShape())
            {
        		return false;
            }
        }
    	return true;
    }
    
    public void dropDown()
    {
        int newY = curPiece.y();
        while (newY > 0) {
            if (!tryMove(curPiece, curPiece.x(), newY - 1))
                break;
            --newY;
        }
        pieceDropped();
    }
    
    
    
    void updateGui()
    {
    	/*
    	console.intervalLabel.setText("" + timer.getDelay());
        console.squareHeightLabel.setText("" + squareHeight());
        console.squareWidthLabel.setText("" + squareWidth());
        statusbar.setText(String.valueOf(numLinesRemoved));
        speedLabel.setText(" " + speed);
        multiplierLabel.setText(multiplier + "    ");
        pointsLabel.setText("" + points);
        */
    	linesLabel.value = ("" + numLinesRemoved);
    	speedLabel.value = ("" + speed);
    	pointsLabel.value = ("" + points);
    	multiplierLabel.value = ("X " + multiplier);
    	if (powerMaxAmmo < 0) ammoLabel.value = "---";
    	else ammoLabel.value = ("" + powerAmmo);
    	powerNameLabel.value = curPower;
    }
    
    public void reloadAmmo()
    {
    	powerAmmo = powerMaxAmmo;
        updateGui();
    }
    
    private void updateLevel() {
    	int previousLvl = speed;
    	speed = numLinesRemoved / 10 + 1;
    	if(timer.getDelay() > 50)
    	{
    		timer.setDelay(500 - 50 * (speed - 1));
    		gravityColor.setTimerDelay(timer.getDelay()/10);
    	}
    	if (previousLvl < speed)
    	{
    		secondMessage.show("Speed up: " + speed + "!", Color.red, 1500, 300);
    	}
	}
    
    void updatePoints(int numLines)
    {
    	if(wasPreviousSuccesful && numLines > 0) 
		{
			++multiplier;
			
		}
    	if(numLines >= 2)
    	{
    		multiplier += numLines;
    	}
    	points += numLines * multiplier;
    	messageBox.show("+" + (numLines*multiplier),Color.green,500,300);
    }
	
	public void linesSuccessfullyRemoved(int numLines)
	{
		numLinesRemoved += numLines;
		updateLevel();
		updatePoints(numLines);
        isFallingFinished = true;
        wasPreviousSuccesful = numLines > 0;
        curPiece.setNoShape();
        gravityColor.setGravity(getGravity());
        updateGui();
	}
	
	private void pieceDropped()
    {
        tetriGrid.dropPiece();

        
        ghostGrid.clear();
        gravityColor.setGravity(getGravity());
        updateGui();
        if (!isFallingFinished)
            newPiece();
    }
	
	public boolean isStarted()
	{
		return isStarted;
	}
	
	public boolean isPaused()
	{
		return isPaused;
	}

	public void noLinesRemoved() {
		wasPreviousSuccesful = false;
		
	}
	
	
	
	public void removedLineAt(int gridX)
	{
		//int r = Math.abs(randomGenerator.nextInt() % 255);
		//int g = Math.abs(randomGenerator.nextInt() % 255);
		//int b = Math.abs(randomGenerator.nextInt() % 255);
		//System.out.println("" + r + "," + g + "," + b);
		//removeLineAnimation.show(gridX, new Color(r,g,b), 20, 300);
		removeLineAnimation.show(gridX, ClassicPiece.getColorFromShape(curPiece.getShape()), 20, 300);
		flashColor.flash(Color.white);
	}
	
	public void tryProcrastinate()
	{
		if (curPower!="PROCRASTINATE") return; 
		ClassicPiece tempPiece = curPiece;
		int coordX = curPiece.x();
		int coordY = curPiece.y();
		curPiece = new ClassicPiece(nextPiece);
		curPiece.setX(coordX);
		curPiece.setY(coordY);
		if (canBeMoved(curPiece,curPiece.x(),curPiece.y()))
		{
			//ok
			nextPiece = new ClassicPiece(tempPiece);
			fallLayer.setPiece(curPiece);
			nextPiece.setX(tetriGrid.columns / 2 - 3);
	        nextPiece.setY(tetriGrid.rows - 5 );
			nextLayer.setPiece(nextPiece);
			ghostGrid.clear();
			ghostGrid.setGhost(curPiece);
			messageBox.show("Procrastinate!", Color.green, 300, 200);
			flashColor.flash(Color.blue);
			render.repaint();
		}
		else curPiece = tempPiece;
	}

	public void tryRelaunch() 
	{		
		if (curPower!="RELAUNCH") return; 
		if (powerAmmo <= 0 && !infiniteAmmo)
			return;
		int coordX = tetriGrid.columns / 2;
		int coordY = tetriGrid.rows - 1 + curPiece.minY();
		if (canBeMoved(curPiece, coordX, coordY))
			{
				tryMove(curPiece,coordX,coordY);
				messageBox.show("Relaunch!", Color.green, 300, 200);
				flashColor.flash(Color.blue);
				powerAmmo-=1;
				updateGui();
			}
	}
	
	public void tryMirror() 
	{
		if (curPower!="MIRROR") return; 
		if (powerAmmo <= 0 && !infiniteAmmo)
			return;
		curPiece.mirror();
		if(!canBeMoved(curPiece,curPiece.x(),curPiece.y()))
			curPiece.mirror();
		else
		{
			ghostGrid.clear();
			ghostGrid.setGhost(curPiece);
			render.repaint();
			messageBox.show("Mirror!", Color.green, 300, 200);
			flashColor.flash(Color.blue);
			powerAmmo-=1;
			updateGui();
		}
	}
	
	public void tryErosion()
	{
		if (curPower!="EROSION") return;
		if (powerAmmo <= 0 && !infiniteAmmo)
			return;
		try
		{
			int gridX = tetriGrid.getTopLine() - 1;
			//System.out.println("Erode line:" + gridX);
			tetriGrid.removeLineAt(gridX);
			render.repaint();
			messageBox.show("Erosion!", Color.green, 300, 200);
			removeLineAnimation.show(gridX, Color.white, 20, 300);
			flashColor.flash(Color.blue);
			powerAmmo-=1;
			updateGui();
		}
		catch (RuntimeException e)
		{
			//just do nothing. 
		}
	}
	
	public double getHeightAverage()
	{
		int[] heights = tetriGrid.getMaxHeights();
		int sum = 0;
		for (int i = 0; i < heights.length; ++i)
			sum += heights[i];
		return (double)sum / (double)heights.length; 
	}
	
	public int getGravity()
	{
		//System.out.println(getHeightAverage() + " / " + tetriGrid.BoardHeight + " * 255 = " + (int) (getHeightAverage() / (tetriGrid.BoardHeight-5) * 255));
		return (Math.min((int) (getHeightAverage() /(tetriGrid.rows - 5) * 510), 510));
	}

	public void nextSquareStyle() {
		switch(curStyle)
		{
		case noBorder:
			curStyle = SquareStyle.SimpleBorder;
			break;
		case SimpleBorder:
			curStyle = SquareStyle.Border3d;
			break;
		case Border3d:
			curStyle = SquareStyle.RoundSquare;
			break;
		case RoundSquare:
			curStyle = SquareStyle.RoundSquareBorder;
			break;
		case RoundSquareBorder:
			curStyle = SquareStyle.Circle;
			break;
		case Circle:
			curStyle = SquareStyle.CircleBorder;
			break;
		case CircleBorder:
			curStyle = SquareStyle.Octagon;
			break;
		case Octagon:
			curStyle = SquareStyle.Medieval;
			break;
		case Medieval:
			curStyle = SquareStyle.noBorder;
			
		default:
			curStyle = SquareStyle.noBorder;
			break;
		}
		
		tetriLayer.setSquareStyle(curStyle);
		fallLayer.setSquareStyle(curStyle);
		nextLayer.setSquareStyle(curStyle);
		ghostLayer.setSquareStyle(curStyle);
		render.repaint();		
	}
	
	public void nextPower(){
		switch(curPower)
		{
		case "PROCRASTINATE":
			curPower = "RELAUNCH";
			powerMaxAmmo = 20;
			break;
		case "RELAUNCH":
			curPower = "MIRROR";
			powerMaxAmmo = 50;
			break;
		case "MIRROR":
			curPower = "EROSION";
			powerMaxAmmo = 10;
			break;
		case "EROSION":
			curPower = "NO POWER";
			powerMaxAmmo = -1;
			break;
		case "NO POWER":
			curPower = "PROCRASTINATE";
			powerMaxAmmo = -1;
			break;
		default:
			curPower = "NO POWER";
			break;
		}
		if (!isStarted) reloadAmmo();
		updateGui();
	}
	
	public void usePower()
	{
		if (powerAmmo <= 0 && powerMaxAmmo  >= 0) return;
		if (curPower == "NO POWER") return;
		switch (curPower)
		{
		case "PROCRASTINATE":
			tryProcrastinate();
			break;
		case "RELAUNCH":
			tryRelaunch();
			break;
		case "MIRROR":
			tryMirror();
			break;
		case "EROSION":
			tryErosion();
			break;
		}
	}
	
	public boolean isInGame() {
		return isStarted;
	}

	public int getScore() {
		return points;
	}

	public int getSpeedLevel() {
		return speed;
	}

	public int getSpeed() {
		return timer.getDelay();
	}

	public int getMultiplier() {
		return multiplier;
	}

	public int getTotalRemovedLines() {
		return numLinesRemoved;
	}
	
	public String getPowerName() {
		return curPower;
	}
	
	public int getPowerAmmo() {
		return powerAmmo;
	}
	
	public int getPowerMaxAmmo() {
		return powerMaxAmmo;
	}
	
	public String getCurrentSquareStyle() {
		return curStyle.toString();
	}
	
	public ClassicPiece getCurrentPiece() {
		return curPiece;
	}
	
	
}
