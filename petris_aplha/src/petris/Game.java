package petris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Timer;
import javax.swing.plaf.ProgressBarUI;

import parsleyj.utils.GlobalUtils;
import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GuiUtils;
import parsleyj.utils.GraphicsUtils.GradientMode;
import petris.ClassicPiece.TetrominoesId;
import petris.db.DataLoader;
import petris.gui.GraphicSquare;
import petris.gui.GraphicSquare.SquareStyle;
import petris.gui.BackgroundLayer;
import petris.gui.BottomMessageLayer;
import petris.gui.ColorFlash;
import petris.gui.DialogConfirmAction;
import petris.gui.FadingColor;
import petris.gui.FlashingGravityColor;
import petris.gui.LabelLayer.DockSide;
import petris.gui.LineAnimationLayer;
import petris.gui.MenuLayer;
import petris.gui.MessageLayer;
import petris.gui.PetrisChildMenu;
import petris.gui.PetrisColor;
import petris.gui.PetrisDialogMenu;
import petris.gui.PetrisGridOptionMenuEntry;
import petris.gui.PetrisMenu;
import petris.gui.PetrisMenuEntry;
import petris.gui.PetrisOptionMenuEntry;
import petris.gui.ProgressBarMenuEntry;
import petris.gui.PetrisSliderEntry;
import petris.gui.PieceLayer;
import petris.gui.GhostLayer;
import petris.gui.PanelLayer;
import petris.gui.LabelLayer;
import petris.gui.ProgressIndicatorMenuEntry;
import petris.gui.RainbowColor;
import petris.gui.RainbowColor.Phase;
import petris.gui.Render;
import petris.gui.RenderInterface;
import petris.gui.TAdapter;
import petris.gui.TetrisGridLayer;
import petris.gui.TextFieldMenuEntry;
import petris.gui.ToggleMenuEntry;
import petris.gui.effects.BlurFilter;
import petris.gui.effects.BlurFilter2;

public class Game implements ActionListener{
	
	public ClassicPiece curPiece;
	public ClassicPiece nextPiece;
	private ClassicPiece previevPiece;
	
	private Dimension gameSize;
	
	private Render render;
	
	private TetrisGrid tetriGrid;
	private TetrisGridLayer tetriLayer;
	private PieceLayer fallLayer;
	
	private GhostGrid ghostGrid;
	private GhostLayer ghostLayer;
	private TetrisGrid previewGrid;
	
	private LineAnimationLayer removeLineAnimation;
	
	private TetrisGrid nextGrid;
	private PieceLayer nextLayer;
	private PieceLayer themePreviewLayer; //in settings menu
	
	private LabelLayer nextTextLabel;
	private LabelLayer scoreTextLabel;
	private LabelLayer multiplierTextLabel;
	private LabelLayer linesTextLabel;
	private LabelLayer speedTextLabel;
	
	private LabelLayer powerNameLabel;
	
	private LabelLayer pointsLabel;
	private LabelLayer multiplierLabel;
	private LabelLayer speedLabel;
	private LabelLayer linesLabel;
	private LabelLayer ammoLabel;
	
	private MessageLayer messageBox;
	private MessageLayer secondMessage;
	private MessageLayer thirdMessage;
	private MessageLayer fourthMessage;
	private BottomMessageLayer smallMessage;
	
	private Timer timer;
	private Timer timer2;
	
	private boolean isFallingFinished = false;
	private boolean isStarted = false;
	private boolean isPaused = false;
    
	private Render guiRender;
    
	private boolean wasPreviousSuccesful = false;
    
	private int numLinesRemoved = 0;
	private int speed = 1;
	private int points = 0;
	private int multiplier = 1;
    
	private int streak = 0;
	
    private int powerAmmo;
    private boolean infiniteAmmo;
    
    
	private FlashingGravityColor gravityColor;
	private ColorFlash flashColor;
	
	private SquareStyle curStyle = SquareStyle.noBorder;
	private BackgroundStyles curBackgroundStyle = BackgroundStyles.FlashingGravity;
	private String curPower = "PROCRASTINATE";
	
	private int powerMaxAmmo = -1;
	private float baseFontSize = 4;
	private int baseFontStyle = 0;
	private File fontFile;
	private Font gameFont;
	
	private PetrisMenu mainMenu;
	private MenuLayer menuLayer;
	
	PetrisChildMenu pauseMenu;
	PetrisChildMenu settingsMenuEntry;
	PetrisOptionMenuEntry selectPowerMenuEntry;
	PetrisGridOptionMenuEntry selectThemeMenuEntry;
	PetrisOptionMenuEntry selectBackgroundMenuEntry;
	PetrisOptionMenuEntry filterMenuEntry;
	ToggleMenuEntry renderModeEntry;
	
	PetrisChildMenu leaderboardsChildMenu;
	
	PetrisChildMenu scrollTestMenuEntry;
	PetrisChildMenu testsMenuEntry;
	PetrisMenuEntry cloneEntry;
	PetrisMenuEntry backMenuEntry;
	private int cloneCounter = 0;
	
	BackgroundLayer bgLayer;
	BackgroundLayer bgLayer2;
	PetrisColor customColor1 = new PetrisColor(new Color(0,0,0,255));
	PetrisColor customColor2 = new PetrisColor(new Color(0,0,0,255));
	BackgroundLayer colorPreviewLayer;
	
	
	private PetrisDialogMenu colorDialogMenu;	
	private PetrisSliderEntry redSlider1;
	private PetrisSliderEntry greenSlider1;
	private PetrisSliderEntry blueSlider1;
	private PetrisSliderEntry redSlider2;
	private PetrisSliderEntry greenSlider2;
	private PetrisSliderEntry blueSlider2;
	private PetrisOptionMenuEntry backgroundPaintMode;
	private Painter customPainter = new Painter(){
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.black);
			g.fillRect(0, 0, gameSize.width, gameSize.height);
		}
	};
	
	
	private boolean inMenu = false;
	private PanelLayer leftGuiPanel;
	private PanelLayer rightGuiPanel;
	private boolean freezeOn = false;
	private GlobalVarSet globals;
	
	private boolean isFirstLaunch;
	private DataLoader dataLoader;
	private boolean resumeOnEsc = false;
	private PetrisChildMenu firstLaunchDialog;
	private PetrisChildMenu profileChildMenu;
	
	
	
	public enum BackgroundStyles {JustBlack, FlashingGravity, Rainbow, MonochromeGradient, GravityGradient, Custom};
	public static RainbowColor rainbowDark = new RainbowColor(2,Phase.GREEN,100);
	public static RainbowColor customRainbow = new RainbowColor(2,Phase.BLUE);
	
	public boolean resizeEnabled = true;
    
    public Game(Dimension d, Render r, Render g)
	{
    	gameSize = d;
    	
		curPiece = new ClassicPiece();
		
		tetriGrid = new TetrisGrid(this);
		tetriGrid.setSize(gameSize);
		tetriLayer = new TetrisGridLayer(tetriGrid);
		
		ghostGrid = new GhostGrid(tetriGrid);
		
		render = r;
		
		gravityColor = new FlashingGravityColor(0,20);
		bgLayer = new BackgroundLayer(new PetrisColor(gravityColor),gameSize);
		
		flashColor = new ColorFlash(new Color(0,0,0,0),20);
		bgLayer2 = new BackgroundLayer(new PetrisColor(flashColor),gameSize);
		
		colorPreviewLayer = new BackgroundLayer(customColor1, gameSize);
		colorPreviewLayer.setEnabled(false);
		colorPreviewLayer.setColor(new PetrisColor(new Color(0,0,0)));
		
		render.addLayer(bgLayer);
		render.addLayer(bgLayer2);
		render.addLayer(colorPreviewLayer);
		
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
		
		try
		{
			//fontFile = new File(this.getClass().getResourceAsInputStream("resources/Mecha.ttf").toString());
			gameFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/Mecha.ttf"));
			gameFont = gameFont.deriveFont(baseFontStyle);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			baseFontSize = 0;
			gameFont = new Font("Arial", Font.BOLD, 18);
		}
		
		nextTextLabel = new LabelLayer("NEXT",gameFont.deriveFont(baseFontSize + 16F),(int)gameSize.getWidth()-70, 25, new Color(255,255,255), 80);
		nextTextLabel.setOriginalPosition(new Point((int)gameSize.getWidth()-70, 25), gameSize, LabelLayer.DockSide.Right);
		
		scoreTextLabel = new LabelLayer("SCORE",gameFont.deriveFont(baseFontSize + 12F),15, 20, new Color(255,255,255), 80);
		multiplierTextLabel = new LabelLayer("MULTIPLIER",gameFont.deriveFont(baseFontSize + 10F),15, 60, new Color(255,255,255), 80);
		linesTextLabel = new LabelLayer("LINES",gameFont.deriveFont(baseFontSize + 10F),15, 94, new Color(255,255,255), 80);
		speedTextLabel = new LabelLayer("SPEED",gameFont.deriveFont(baseFontSize + 10F),15, 132, new Color(255,255,255), 80);
		
		pointsLabel = new LabelLayer("",gameFont.deriveFont(baseFontSize + 18F),15, 40, new Color(50,255,0), 100);
		multiplierLabel = new LabelLayer("",gameFont.deriveFont(baseFontSize + 12F),15, 76, new Color(0,255,255), 80);
		linesLabel = new LabelLayer("0",gameFont.deriveFont(baseFontSize + 14F),15, 112, Color.yellow, 100);
		speedLabel = new LabelLayer("",gameFont.deriveFont(baseFontSize + 14F),15, 148, new Color(255,0,0), 100);
		ammoLabel = new LabelLayer("0",gameFont.deriveFont(baseFontSize + 14F), 15, 186, Color.orange, 100);
		
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
			leftGuiPanel = new PanelLayer(new Dimension(80,200), 5, 5);
			render.addLayer(leftGuiPanel);
			rightGuiPanel = new PanelLayer(new Dimension(80,120), (int)gameSize.getWidth()-85, 5);
			rightGuiPanel.setOriginalPosition(new Point((int)gameSize.getWidth()-85, 5), gameSize, PanelLayer.DockSide.Right);
			render.addLayer(rightGuiPanel);
			
			render.addLayer(nextTextLabel);
			render.addLayer(scoreTextLabel);
			render.addLayer(multiplierTextLabel);
			render.addLayer(linesTextLabel);
			render.addLayer(speedTextLabel);
			
			powerNameLabel = new LabelLayer("RELAUNCH",gameFont.deriveFont(baseFontSize + 8F),15, 170, new Color(255,255,255), 80);			
			render.addLayer(powerNameLabel);
			
			render.addLayer(pointsLabel);
			render.addLayer(multiplierLabel);
			render.addLayer(linesLabel);
			render.addLayer(speedLabel);
			render.addLayer(ammoLabel);
			
			//notification messages configuration
			
			
			messageBox = new MessageLayer ((int)gameSize.getWidth(), (int)gameSize.getHeight(), 40, gameFont.deriveFont(baseFontSize + 20F), 230);
			render.addLayer(messageBox);
			
			secondMessage = new MessageLayer((int)gameSize.getWidth(), (int)gameSize.getHeight(), 20, 31, gameFont.deriveFont(baseFontSize + 12F), 230);
			render.addLayer(secondMessage);
			
			thirdMessage = new MessageLayer((int)gameSize.getWidth(), (int)gameSize.getHeight(), 20, 52, gameFont.deriveFont(baseFontSize + 12F), 230);
			render.addLayer(thirdMessage);
			
			fourthMessage = new MessageLayer((int)gameSize.getWidth(), (int)gameSize.getHeight(), 20, 73, gameFont.deriveFont(baseFontSize + 12F), 230);
			render.addLayer(fourthMessage);
			
			nextGrid = new TetrisGrid(this,new Dimension(150,334), 10, 22);
			nextLayer = new PieceLayer(nextGrid,nextPiece,215,-15,3,100);
			nextLayer.setOriginalPosition(215,-15, gameSize, PieceLayer.DockSide.Right);
			render.addLayer(nextLayer);
			
			//MENUS	---------------------------------------
			
			initializeMenu();
			
			initializePauseMenu();
			
			initializeFirstLaunchDialog();
			
			initializeCreateProfileDialog();
			
			initializeRenameProfileDialog();
			
			smallMessage = new BottomMessageLayer((int)gameSize.getWidth(),(int)gameSize.getHeight(),gameFont.deriveFont(baseFontSize + 12F),200);
			render.addLayer(smallMessage);
		}
		
		timer2 = new Timer(1500,this);
		
	    timer = new Timer(500,this);
	    gravityColor.setTimerDelay(50);
	    updateSquareStyle();
	    
	}
    
    
    
  



	public void actionPerformed(ActionEvent e) {
    	if (e.getSource().equals(timer2))
    	{
    		mainMenu.show();
    		timer2.stop();
    		return;
    	}
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
        	if(!freezeOn) oneLineDown();
        }
        updateGui();
        render.repaint();
    }
    
	public void init(boolean firstLaunch) {
		if (firstLaunch)
		{
			mainMenu.enterChildMenu(firstLaunchDialog);
		}
		else 
		{
			smallMessage.show("Welcome back, " + globals.currentProfile.getName() + "!", Color.green, 2000, 500);
			loadAllPrefs();
		}
		showMainMenu();
		
		
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
        
        setPower(selectPowerMenuEntry.getSelected());
        reloadAmmo();
        
        updateGui();
        
        nextPiece.setRandomShape();
        newPiece();
        gravityColor.setGravity(0);
        gravityColor.setTimerDelay(50);
        timer.setDelay(500);
        timer.start();
        
        smallMessage.show("New game started", Color.green, 1500, 500);//TODO: what with simoultaneous message shows? ---> add a queue
        fadeOutMenus();
        fadeInGui();
        
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
        	closePausedMenu();
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
        if (points >0)dataLoader.addHighscore(globals);
        messageBox.show("Game Over!", Color.red, -1, 500);
        secondMessage.show("Press N to start a new game.", Color.white, -1, 500);
        thirdMessage.show("Press Y to change power.", Color.green, -1, 500);
        fourthMessage.show("Press Q to exit to main menu", Color.red, -1, 300);
	}
	
	public void showMainMenu()
	{
		curPiece.setShape(TetrominoesId.NOSHAPE);
		ghostGrid.setGhost(curPiece);
		nextPiece.setShape(TetrominoesId.NOSHAPE);
		ghostGrid.clear();
		tetriGrid.clear();
		timer.stop();
        isStarted = false;
        updateGui();
        /* old menu
        messageBox.show("Petris", Color.cyan, -1, 500);
        secondMessage.show("Press N to start a new game.", Color.orange, -1, 500);
        thirdMessage.show("Press Y to change power.", Color.green, -1, 500);
        fourthMessage.show("Press T to change theme.", Color.red, -1, 500);
        */
        //fadeOutMenus();
        fadeOutGui();
        //menuLayer.setBackFilterEnabled(true);
        mainMenu.show(); //new menu
        inMenu = true;
	}
	
	public void showPausedMenu() {
		//messageBox.show("Paused", Color.white, -1, 300);
		//secondMessage.show("Press P or Esc to resume", Color.green, -1, 300);
		//thirdMessage.show("Press T to change theme", Color.orange, -1, 300);
		//fourthMessage.show("Press Q to exit to main menu", Color.red, -1, 300);
		//this.resumeOnEsc =true;
		menuLayer.setBackFilterEnabled(true);
	
		mainMenu.show();
		mainMenu.enterChildMenu(pauseMenu);
		
		inMenu = true;
	}
	
	public void closePausedMenu()
	{
		this.resumeOnEsc = false;
		menuLayer.setBackFilterEnabled(false);
		mainMenu.resetRootEntries();
		mainMenu.close();
	
		inMenu = false;
	}
	
	private void fadeOutMenus() {
		messageBox.fadeOut();
		secondMessage.fadeOut();
		thirdMessage.fadeOut();
		fourthMessage.fadeOut();
		
		mainMenu.close();
		menuLayer.setBackFilterEnabled(false);
		mainMenu.resetRootEntries();

		
		inMenu = false;
	}
	
	public void fadeInGui() {
		nextTextLabel.fadeIn(-1, 500);
		scoreTextLabel.fadeIn(-1, 500);
		multiplierTextLabel.fadeIn(-1, 500);
		linesTextLabel.fadeIn(-1, 500);
		speedTextLabel.fadeIn(-1, 500);
		
		powerNameLabel.fadeIn(-1, 500);
		
		pointsLabel.fadeIn(-1, 500);
		multiplierLabel.fadeIn(-1, 500);
		linesLabel.fadeIn(-1, 500);
		speedLabel.fadeIn(-1, 500);
		ammoLabel.fadeIn(-1, 500);
		
		leftGuiPanel.fadeIn(-1, 500);
		rightGuiPanel.fadeIn(-1, 500);
	}
	    
    public void fadeOutGui()
    {
    	nextTextLabel.fadeOut();
		scoreTextLabel.fadeOut();
		multiplierTextLabel.fadeOut();
		linesTextLabel.fadeOut();
		speedTextLabel.fadeOut();
		
		powerNameLabel.fadeOut();
		
		pointsLabel.fadeOut();
		multiplierLabel.fadeOut();
		linesLabel.fadeOut();
		speedLabel.fadeOut();
		ammoLabel.fadeOut();
		
		leftGuiPanel.fadeOut();
		rightGuiPanel.fadeOut();
		
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
    		if (freezeOn) 
    			if (tryMove(curPiece,curPiece.x(),curPiece.y()-1))
    				tryRotateLeft();
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
    		if (freezeOn) 
    			if (tryMove(curPiece,curPiece.x(),curPiece.y()-1))
    				tryRotateRight();
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
    		fourthMessage.show("Speed up: " + speed + "!", Color.red, 2000, 300);
    	}
	}
    
    void updatePoints(int numLines)
    {
    	if(wasPreviousSuccesful && numLines > 0) 
		{
    		if (streak == 0) streak = 1;
    		
			multiplier+=streak;
			thirdMessage.show("Streak! + X" + streak, Color.orange, 1500, 500);
			++streak;
		}
    	if(numLines >= 2)
    	{
    		multiplier += numLines;
    		secondMessage.show("Combo! + X" + numLines, Color.cyan, 1500, 500);
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
        
        if (freezeOn)
        {
        	--powerAmmo;
        	if (powerAmmo==0)
        	{
        		freezeOn=false;
        		messageBox.show("Unfreeze!", Color.red, 300, 200);
    			flashColor.flash(Color.blue);
        	}
        	updateGui();
        }
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
		streak = 0;
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
	
	public void tryFreeze()
	{
		if (curPower!="FREEZE" || freezeOn) return;
		if (powerAmmo <= 0 && !infiniteAmmo)
			return;
		
		freezeOn=true;
		messageBox.show("Freeze!", Color.green, 300, 200);
		flashColor.flash(Color.blue);
		
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
		
		SquareStyle[] allv = SquareStyle.values();
		if (curStyle.ordinal() + 1 >= allv.length) curStyle = allv[0];
		else curStyle = allv[curStyle.ordinal() + 1];
		updateSquareStyle();
		savePref("square_style");
		if(!inMenu)smallMessage.show("Style selected: " + curStyle.toString(), Color.yellow, 1500, 500);
	}
	
	public void previousSquareStyle() {
		SquareStyle[] allv = SquareStyle.values();
		if (curStyle.ordinal() - 1 < 0) curStyle = allv[allv.length-1];
		else curStyle = allv[curStyle.ordinal() - 1];
		updateSquareStyle();
		savePref("square_style");
		if(!inMenu)smallMessage.show("Style selected: " + curStyle.toString(), Color.yellow, 1500, 500);	
	}
	
	private void updateSquareStyle()
	{
		tetriLayer.setSquareStyle(curStyle);
		fallLayer.setSquareStyle(curStyle);
		nextLayer.setSquareStyle(curStyle);
		ghostLayer.setSquareStyle(curStyle);
		themePreviewLayer.setSquareStyle(curStyle);
		selectThemeMenuEntry.setOptionText(curStyle.toString());
		render.repaint();
	}
	
	public void nextBackground()
	{
		BackgroundStyles[] allv = BackgroundStyles.values();
		if (curBackgroundStyle.ordinal() + 1 >= allv.length) curBackgroundStyle = allv[0];
		else curBackgroundStyle = allv[curBackgroundStyle.ordinal()+1];
		updateBackground();
		savePref("bg_style");
		if(!inMenu)smallMessage.show("Background selected: " + curBackgroundStyle.toString(), Color.magenta, 1500, 500);
	}
	
	public void previousBackground()
	{
		BackgroundStyles[] allv = BackgroundStyles.values();
		if (curBackgroundStyle.ordinal() - 1 < 0) curBackgroundStyle = allv[allv.length-1];
		else curBackgroundStyle = allv[curBackgroundStyle.ordinal()-1];
		updateBackground();
		savePref("bg_style");
		if(!inMenu)smallMessage.show("Background selected: " + curBackgroundStyle.toString(), Color.magenta, 1500, 500);
	}
	
	private void updateBackground()
	{	
		
		switch(curBackgroundStyle)
		{
		case FlashingGravity:
			bgLayer.setColor(new PetrisColor(gravityColor));
			bgLayer.setCustomPaint(null);
			break;
		case Rainbow:
			bgLayer.setColor(new PetrisColor(rainbowDark));
			bgLayer.setCustomPaint(null);
			break;
		case JustBlack:
			bgLayer.setColor(new PetrisColor(Color.black));
			bgLayer.setCustomPaint(null);
			break;
		case MonochromeGradient:
			bgLayer.setCustomPaint(new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, Color.black, Color.white, 0, 0, gameSize.width, gameSize.height, GradientMode.VERTICAL);
				}
			});
			break;
		case GravityGradient:
			bgLayer.setCustomPaint(new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(40,0,0), new Color(0,40,0), 0, 0, gameSize.width, gameSize.height, GradientMode.VERTICAL);
				}
			});
			break;
		case Custom:
			bgLayer.setCustomPaint(customPainter);
			break;
		default:
			break;
		}
		selectBackgroundMenuEntry.setOptionText(curBackgroundStyle.toString());
		render.repaint();
		
	}
	
	public void setPower(String power)
	{
		switch(power.toUpperCase())
		{
		case "PROCRASTINATE":
			curPower = "PROCRASTINATE";
			powerMaxAmmo = -1;
			break;
		case "RELAUNCH":
			curPower = "RELAUNCH";
			powerMaxAmmo = 20;
			break;
		case "MIRROR":
			curPower = "MIRROR";
			powerMaxAmmo = 50;
			break;
		case "EROSION":
			curPower = "EROSION";
			powerMaxAmmo = 10;
			break;
		case "FREEZE":
			curPower = "FREEZE";
			powerMaxAmmo = 10;
			break;
		case "NO POWER":
			curPower = "NO POWER";
			powerMaxAmmo = -1;
			break;
		default:
			curPower = "NO POWER";
			break;
		}
		if (!isStarted) reloadAmmo();
		updateGui();
		
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
			curPower = "FREEZE";
			powerMaxAmmo = 10;
			break;
		case "FREEZE":
			curPower = "NO POWER";
			powerMaxAmmo = -1;
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
		case "FREEZE":
			tryFreeze();
			break;
		}
	}
	
	public void updateCustomBackground(){
		Painter p;
		switch(backgroundPaintMode.getSelected())
		{
		case "Vertical Gradient":
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							new Color(redSlider2.getValue(),greenSlider2.getValue(),blueSlider2.getValue()), 
							0, 0, gameSize.width, gameSize.height, GradientMode.VERTICAL);
				}
			};
			break;
		}
		case "Horizontal Gradient":
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							new Color(redSlider2.getValue(),greenSlider2.getValue(),blueSlider2.getValue()), 
							0, 0, gameSize.width, gameSize.height, GradientMode.HORIZONTAL);
				}
			};
			break;
		}
		case "Solid Color":
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							0, 0, gameSize.width, gameSize.height, GradientMode.HORIZONTAL);
				}
			};
			break;
		}
		case "Rainbow Up":
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, customRainbow.getStaticColor(), 
							new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							0, 0, gameSize.width, gameSize.height, GradientMode.VERTICAL);
				}
			};
			break;
		}
		case "Rainbow Down":
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							customRainbow.getStaticColor(),	0, 0, gameSize.width, gameSize.height, GradientMode.VERTICAL);
				}
			};
			break;
		}
		default:
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							0, 0, gameSize.width, gameSize.height, GradientMode.HORIZONTAL);
				}
			};
			break;
		}
		}
		customPainter = p;
		bgLayer.setCustomPaint(customPainter);
	}
	
	public void updatePrevievBackground()
	{
		if (backgroundPaintMode.getSelected() == "Solid Color" || backgroundPaintMode.getSelected() ==  "Rainbow Up")
		{
			redSlider2.setEnabled(false);
			greenSlider2.setEnabled(false);
			blueSlider2.setEnabled(false);
		}else{
			redSlider2.setEnabled(true);
			greenSlider2.setEnabled(true);
			blueSlider2.setEnabled(true);
		}
		Painter p;
		switch(backgroundPaintMode.getSelected())
		{
		case "Vertical Gradient":
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							new Color(redSlider2.getValue(),greenSlider2.getValue(),blueSlider2.getValue()), 
							0, 0, gameSize.width, gameSize.height, GradientMode.VERTICAL);
				}
			};
			break;
		}
		case "Horizontal Gradient":
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							new Color(redSlider2.getValue(),greenSlider2.getValue(),blueSlider2.getValue()), 
							0, 0, gameSize.width, gameSize.height, GradientMode.HORIZONTAL);
				}
			};
			break;
		}
		case "Solid Color":
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							0, 0, gameSize.width, gameSize.height, GradientMode.HORIZONTAL);
				}
			};
			break;
		}
		case "Rainbow Up":
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, customRainbow.getStaticColor(), 
							new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							0, 0, gameSize.width, gameSize.height, GradientMode.VERTICAL);
				}
			};
			break;
		}
		case "Rainbow Down":
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							customRainbow.getStaticColor(),	0, 0, gameSize.width, gameSize.height, GradientMode.VERTICAL);
				}
			};
			break;
		}
		default:
		{
			p = new Painter(){
				@Override
				public void paint(Graphics g) {
					GraphicsUtils.fillGradientRect(g, new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							new Color(redSlider1.getValue(),greenSlider1.getValue(),blueSlider1.getValue()), 
							0, 0, gameSize.width, gameSize.height, GradientMode.HORIZONTAL);
				}
			};
			break;
		}
		}
		
		colorPreviewLayer.setCustomPaint(p);
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

	public boolean isInMenu() {
		return inMenu;
	}

	public void menuNavDown() {
		mainMenu.performGoDown();		
	}

	public void menuNavUp() {
		mainMenu.performGoUp();		
	}
	
	public void menuNavOk() {
		mainMenu.performOk();
		
	}
	
	public void menuNavBack() {
		if (resumeOnEsc)
		{
			this.pause();
			return;
		}
		mainMenu.performBack();
		
	}
	
	public void menuNavLeft() {
		mainMenu.performGoLeft();		
	}
	
	public void menuNavRight() {
		mainMenu.performGoRight();
	}
	
	public void setPreviewMode(boolean mode)
	{
		colorPreviewLayer.setEnabled(mode);
	}



	public void setGlobals(GlobalVarSet globals) {
		this.globals = globals;
		
	}
	
	public void addHighscoresToMenu(PetrisChildMenu x)
	{
		x.clearEntries();
		ResultSet rs = dataLoader.getHighscores();
		int rank = 1;
		if (rs == null)
		{
			x.addEntry(new PetrisMenuEntry("There are no scores yet.",
							gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 40,	
							new FadingColor(new Color(50,50,50,230), 50), new FadingColor(Color.gray, 230)));
		}
		else
		{
			try{
				while (rs.next()){
					x.addEntry(new PetrisMenuEntry("" + rank + ". " + rs.getString("profile_name") + ": " + rs.getInt("match_score"),
							gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 40,	
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230)));
					++rank;
				}
				mainMenu.updateFocusedEntry();
			
			}
			catch(SQLException | NullPointerException e)
			{
				e.printStackTrace();
			}
		}
		x.addEntry(backMenuEntry);
		/*for (int i = globals.highscores.size() - 1; i >= 0; --i)
		{
			x.addEntry(new PetrisMenuEntry("" + (i+1) + ". " + globals.highscoreProfiles.get(i) + ": " +  globals.highscores.get(i), 
					gameFont.deriveFont(baseFontSize + 14F), gameSize.width));
		}*/
	}
	
	public void addProfilesToMenu(PetrisChildMenu x)
	{
		x.clearEntries();
		ResultSet rs = dataLoader.getProfiles();
		Action loginAction = new Action() {			
			@Override
			public void run() {
				String tmpString = mainMenu.getTitle();
				Game.this.globals.currentProfile = Game.this.dataLoader.loginAs(tmpString);
				Game.this.mainMenu.setCanGoBack(true);
				Game.this.mainMenu.performBack();
				Game.this.mainMenu.performBack();
				Game.this.smallMessage.show("Logged in as " + globals.currentProfile.getName() + ", welcome!", Color.green, 2000, 500);
				Game.this.loadAllPrefs();
			}
		};
	
		if (rs == null)
		{
			x.addEntry(new PetrisMenuEntry("There are no profiles yet.",
							gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 40,	
							new FadingColor(new Color(50,50,50,230), 50), new FadingColor(Color.gray, 230)));
		}
		else
		{
			try{
				while (rs.next()){
					PetrisChildMenu profileEntry = new PetrisChildMenu("" + rs.getString("name"),
							gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 40,	
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
					PetrisMenuEntry loginEntry =new PetrisMenuEntry("Log in",
							gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,	
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
					loginEntry.setOnOk(loginAction);
					profileEntry.addEntry(loginEntry);
		
					profileEntry.addEntry(new PetrisMenuEntry("Stats",
							gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,	
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230),false));
					profileEntry.addEntry(new PetrisMenuEntry("Saved customizations",
							gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,	
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230),false));
					profileEntry.addEntry(new PetrisMenuEntry("Export profile",
							gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,	
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230),false));
					renameProfileDialog.setText("Rename profile");
					
					profileEntry.addEntry(renameProfileDialog);
					
					
					renameProfileDialog.setOnEntered(new Action() {
						@Override
						public void run() {
							Game.this.mainMenu.setTitle(Game.this.mainMenu.getPreviousTitle());
						}
					});
					
					
					PetrisMenuEntry deleteProfileEntry = new PetrisMenuEntry("Delete profile",
							gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,	
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.red, 230));
					
					
					if (rs.getString("name").equals(globals.currentProfile.getName())) deleteProfileEntry.setEnabled(false);
					else deleteProfileEntry.setEnabled(true);
					
					
					deleteProfileEntry.setOnOk(new Action() {
						@Override
						public void run() {
							String tmpstring = Game.this.mainMenu.getTitle();
							Game.this.dataLoader.removeProfile(tmpstring);
							Game.this.mainMenu.setCanGoBack(true);
							Game.this.mainMenu.performBack();
							Game.this.mainMenu.performBack();
							Game.this.smallMessage.show("Profile '"+tmpstring+"' removed.", Color.green, 2000, 500);
						}
					});
						
						
					profileEntry.addEntry(deleteProfileEntry);
					
					profileEntry.addEntry(backMenuEntry);
					
					x.addEntry(profileEntry);
					
				
				}
				mainMenu.updateFocusedEntry();
			
			}
			catch(SQLException | NullPointerException e)
			{
				e.printStackTrace();
			}
		}
		x.addEntry(createProfileDialog);
		x.addEntry(new PetrisMenuEntry("Import profile",
				gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,	
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230),false));
		x.addEntry(backMenuEntry);
		/*for (int i = globals.highscores.size() - 1; i >= 0; --i)
		{
			x.addEntry(new PetrisMenuEntry("" + (i+1) + ". " + globals.highscoreProfiles.get(i) + ": " +  globals.highscores.get(i), 
					gameFont.deriveFont(baseFontSize + 14F), gameSize.width));
		}*/
	}
	
	public void setFirstLaunch(boolean isFirstLaunch) {
		this.isFirstLaunch = isFirstLaunch;
		
	}
	
	public void setDataLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
		
	}
	



	private void initializeMenu()
	{
		
		
		
		backMenuEntry = new PetrisMenuEntry("Back", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,  
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.red, 230));
		backMenuEntry.setOnOk(new Action(){
			@Override
			public void run() {
				Game.this.menuNavBack();	
				Game.this.backMenuEntry.setFocused(false);
			}
		});
		
		//Color picker dialog menu configuration:
		
		colorDialogMenu = new PetrisDialogMenu(gameSize,"Custom Background:",gameFont.deriveFont(baseFontSize + 16F), 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230), new DialogConfirmAction() {
					@Override
					public void run(ArrayList<PetrisMenuEntry> entries) {
						//PetrisOptionMenuEntry colorMode = (PetrisOptionMenuEntry)entries.get(0);
						int red1 = ((PetrisSliderEntry)entries.get(0)).getValue();
						int green1 = ((PetrisSliderEntry)entries.get(1)).getValue();
						int blue1 = ((PetrisSliderEntry)entries.get(2)).getValue();
						String mode = ((PetrisOptionMenuEntry)entries.get(3)).getSelected();
						int red2 = ((PetrisSliderEntry)entries.get(4)).getValue();
						int green2 = ((PetrisSliderEntry)entries.get(5)).getValue();
						int blue2 = ((PetrisSliderEntry)entries.get(6)).getValue();
						Game.this.customColor1.setColor(new Color(red1,green1,blue1));
						Game.this.customColor2.setColor(new Color(red2,green2,blue2));
						Game.this.updateCustomBackground();
						Game.this.savePref("custom1_red");
						Game.this.savePref("custom1_green");
						Game.this.savePref("custom1_blue");
						Game.this.savePref("custom2_red");
						Game.this.savePref("custom2_green");
						Game.this.savePref("custom2_blue");
						Game.this.savePref("bg_custom_mode");
						Game.this.menuNavBack();
					}
				
				});	
		colorDialogMenu.setOnEntered(new Action(){
			public void run() {
				Game.this.setPreviewMode(true);
				Game.this.updatePrevievBackground();
			}//TODO: save current entries status
		});
		colorDialogMenu.setOnExiting(new Action(){
			public void run() {
				Game.this.setPreviewMode(false);
			}//TODO: recover entries status if cancel
		});
		
		Action updatePC= new Action() {				
			@Override
			public void run() {
				Game.this.updatePrevievBackground();
			}
		};	
		redSlider1 = new PetrisSliderEntry("", gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 50, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.red, 230));
		greenSlider1 = new PetrisSliderEntry("", gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 50, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
		blueSlider1 = new PetrisSliderEntry("", gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 50, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.blue, 230));
		redSlider2 = new PetrisSliderEntry("", gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 50, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.red, 230));
		greenSlider2 = new PetrisSliderEntry("", gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 50, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
		blueSlider2 = new PetrisSliderEntry("", gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 50, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.blue, 230));
		redSlider1.setOnLeft(updatePC);
		redSlider1.setOnRight(updatePC);
		greenSlider1.setOnLeft(updatePC);
		greenSlider1.setOnRight(updatePC);
		blueSlider1.setOnLeft(updatePC);
		blueSlider1.setOnRight(updatePC);
		redSlider2.setOnLeft(updatePC);
		redSlider2.setOnRight(updatePC);
		greenSlider2.setOnLeft(updatePC);
		greenSlider2.setOnRight(updatePC);
		blueSlider2.setOnLeft(updatePC);
		blueSlider2.setOnRight(updatePC); //TODO: numbers are 0 on loading
		backgroundPaintMode = new PetrisOptionMenuEntry("Select paint mode:", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 70,
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.cyan, 230));
		backgroundPaintMode.addOption("Vertical Gradient");
		backgroundPaintMode.addOption("Horizontal Gradient");
		backgroundPaintMode.addOption("Solid Color");
		backgroundPaintMode.addOption("Rainbow Up");
		backgroundPaintMode.addOption("Rainbow Down");
		backgroundPaintMode.setOnLeft(updatePC);
		backgroundPaintMode.setOnRight(updatePC);
		backgroundPaintMode.setOkForNext(false);
		colorDialogMenu.addEntry(redSlider1);
		colorDialogMenu.addEntry(greenSlider1);
		colorDialogMenu.addEntry(blueSlider1);
		colorDialogMenu.addEntry(backgroundPaintMode);
		colorDialogMenu.addEntry(redSlider2);
		colorDialogMenu.addEntry(greenSlider2);
		colorDialogMenu.addEntry(blueSlider2);
		PetrisMenuEntry previewEntry = new PetrisMenuEntry("Show preview", gameFont.deriveFont(baseFontSize + 14F), (int)gameSize.getWidth(), 40,
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230));
		previewEntry.setOnOk(new Action(){
			@Override
			public void run() {
				mainMenu.close();
				timer2.start();
			}
		});
		colorDialogMenu.addEntry(previewEntry);
		
		/*PetrisOptionMenuEntry colorModeDialogEntry = new PetrisOptionMenuEntry("Color paint style:", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 70,
		new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.white, 230));
colorModeDialogEntry.addOption("Color");
colorModeDialogEntry.addOption("Rainbow");
colorModeDialogEntry.addOption("FlashingGravity");
colorDialogMenu.addEntry(colorModeDialogEntry);*/
		
		//Main menu configuration:
		
		menuLayer = new MenuLayer();
		mainMenu = new PetrisMenu(gameSize,"Petris",gameFont.deriveFont(baseFontSize + 34F), 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
		menuLayer.setCurrentMenu(mainMenu);
		menuLayer.setBackFilter(new BlurFilter2());
		render.addLayer(menuLayer);
			
					//New Game menu configuration   ----------------------------
					PetrisChildMenu newGameMenuEntry = new PetrisChildMenu("New game", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,  
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
					
					selectPowerMenuEntry = new PetrisOptionMenuEntry("Select power:", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 70,
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230));
					selectPowerMenuEntry.addOption("No Power");
					selectPowerMenuEntry.addOption("Procrastinate");
					selectPowerMenuEntry.addOption("Relaunch");
					selectPowerMenuEntry.addOption("Mirror");
					selectPowerMenuEntry.addOption("Erosion");
					selectPowerMenuEntry.addOption("Freeze");
					
					newGameMenuEntry.addEntry(selectPowerMenuEntry);
					
					PetrisOptionMenuEntry pieceSetOptionMenuEntry = new PetrisOptionMenuEntry("Select set of pieces:", gameFont.deriveFont(baseFontSize + 16F), 
							(int)gameSize.getWidth(), 70, new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.blue, 230));
					pieceSetOptionMenuEntry.setEnabled(false);
					pieceSetOptionMenuEntry.setOptionText("Coming soon...");
					newGameMenuEntry.addEntry(pieceSetOptionMenuEntry);
					
					PetrisOptionMenuEntry comboBonusOptionMenuEntry = new PetrisOptionMenuEntry("Select Combo Bonus:", gameFont.deriveFont(baseFontSize + 16F), 
							(int)gameSize.getWidth(), 70, new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.magenta, 230));
					comboBonusOptionMenuEntry.setEnabled(false);
					comboBonusOptionMenuEntry.setOptionText("Coming soon...");
					newGameMenuEntry.addEntry(comboBonusOptionMenuEntry);
					
					PetrisMenuEntry startGameMenuEntry = new PetrisMenuEntry("Start!", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,  
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
					startGameMenuEntry.setOnOk(new Action(){
						@Override
						public void run() {
							Game.this.start();					
						}
					});
					newGameMenuEntry.addEntry(startGameMenuEntry);
					newGameMenuEntry.addEntry(backMenuEntry);
					//------------------------------------------
			
			
		
		mainMenu.addEntry(newGameMenuEntry);
		leaderboardsChildMenu = new PetrisChildMenu("Leaderboards", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,  
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230));
		leaderboardsChildMenu.setOnEntered(new Action(){
			@Override
			public void run() {
				Game.this.addHighscoresToMenu(leaderboardsChildMenu);
			}
		});
		
		mainMenu.addEntry(leaderboardsChildMenu);
		
		profileChildMenu = new PetrisChildMenu("Profiles", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.magenta, 230));
		profileChildMenu.setOnEntered(new Action(){
			@Override
			public void run() {
				Game.this.addProfilesToMenu(profileChildMenu);
			}
		});
		
		mainMenu.addEntry(profileChildMenu);
		
		
		
					//Settings menu configuration-----------------------
					settingsMenuEntry = new PetrisChildMenu("Settings", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,  
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.cyan, 230));
					
					previewGrid = new TetrisGrid(this,new Dimension(125,125),5,5);
					previevPiece = new ClassicPiece(TetrominoesId.SQUARE);
					previevPiece.setX(0);
					previevPiece.setY(previewGrid.rows -1);
					themePreviewLayer = new PieceLayer(previewGrid, previevPiece, 0, 0, 5, 255);
					selectThemeMenuEntry = new PetrisGridOptionMenuEntry("Select square style:", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 150, 
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.magenta, 230), themePreviewLayer, 50, 50);
					selectThemeMenuEntry.setOnLeft(new Action(){
						@Override
						public void run() {
							Game.this.previousSquareStyle();
						}							
					});
					selectThemeMenuEntry.setOnRight(new Action(){
						@Override
						public void run() {
							Game.this.nextSquareStyle();
						}							
					});
					selectThemeMenuEntry.setOnOk(new Action(){
						@Override
						public void run() {
							Game.this.nextSquareStyle();
						}
					});
					selectThemeMenuEntry.setOptionText("noBorder");
					settingsMenuEntry.addEntry(selectThemeMenuEntry);
					
					selectBackgroundMenuEntry = new PetrisOptionMenuEntry("Select background:", gameFont.deriveFont(baseFontSize + 16F), 
							(int)gameSize.getWidth(), 70, new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.yellow, 230));
					selectBackgroundMenuEntry.setOnLeft(new Action(){
						@Override
						public void run() {
							Game.this.previousBackground();
							if(Game.this.curBackgroundStyle==BackgroundStyles.Custom)
								selectBackgroundMenuEntry.setOptionText("Custom (press Enter)...");
						}
					});
					selectBackgroundMenuEntry.setOnRight(new Action(){
						@Override
						public void run() {
							Game.this.nextBackground();
							if(Game.this.curBackgroundStyle==BackgroundStyles.Custom)
								selectBackgroundMenuEntry.setOptionText("Custom (press Enter)...");
						}
					});
					selectBackgroundMenuEntry.setOnOk(new Action(){
						@Override
						public void run() {
							if(Game.this.curBackgroundStyle==BackgroundStyles.Custom)
								mainMenu.enterChildMenu(colorDialogMenu);
						}
					});
					selectBackgroundMenuEntry.setOkForNext(false);
					selectBackgroundMenuEntry.setOptionText("FlashingGravity");
					settingsMenuEntry.addEntry(selectBackgroundMenuEntry);
					
					//settingsMenuEntry.addEntry(colorDialogMenu); //now is hidden and accessible through selectBackgroundMenuEntry
					
					settingsMenuEntry.addEntry(new PetrisMenuEntry("Controls", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
							new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.cyan, 230), false));
					
						PetrisChildMenu graphicsSettingsMenuEntry = new PetrisChildMenu("Graphics settings", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
								new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230));
						
						renderModeEntry = new ToggleMenuEntry("Menus background blur", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
								new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230));
						renderModeEntry.setChecked(true);
						renderModeEntry.setOnOk(new Action() {
							@Override
							public void run() {
								Game.this.render.setLayerBasedFiltersDisabled(Game.this.renderModeEntry.isChecked());
								Game.this.savePref("menu_blur_bg");
							}
						});
						graphicsSettingsMenuEntry.addEntry(renderModeEntry);
						graphicsSettingsMenuEntry.addEntry(backMenuEntry);
					
					settingsMenuEntry.addEntry(graphicsSettingsMenuEntry);
					
						testsMenuEntry = new PetrisChildMenu("For Testers", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
								new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.cyan, 230));
						
							scrollTestMenuEntry = new PetrisChildMenu("Menu Tests", gameFont.deriveFont(baseFontSize + 16F), gameSize.width, 40, 
									new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.cyan, 230));
							
								TextFieldMenuEntry textFieldTest = new TextFieldMenuEntry("Text Field:", gameFont.deriveFont(baseFontSize + 16F), gameSize.width, 70, 
										new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.yellow, 230), "write text here...(press Enter)");
								scrollTestMenuEntry.addEntry(textFieldTest);
								
								ProgressIndicatorMenuEntry indicatorTest = new ProgressIndicatorMenuEntry("Progress Indicator:", gameFont.deriveFont(baseFontSize + 16F), gameSize.width, 40, 
										new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230));
								scrollTestMenuEntry.addEntry(indicatorTest);		
								
								ProgressBarMenuEntry progressBarTest = new ProgressBarMenuEntry("Progress Bar:", gameFont.deriveFont(baseFontSize + 16F), gameSize.width, 60, 
										new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.magenta, 230),0,100,1);
								progressBarTest.setValue(60);
								scrollTestMenuEntry.addEntry(progressBarTest);
								
								ToggleMenuEntry toggleTest = new ToggleMenuEntry("Toggle (on/off):", gameFont.deriveFont(baseFontSize + 16F), gameSize.width, 40, 
										new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.cyan, 230));
								scrollTestMenuEntry.addEntry(toggleTest);	
								
								PetrisOptionMenuEntry optionTest = new PetrisOptionMenuEntry("Options:", gameFont.deriveFont(baseFontSize + 16F), gameSize.width, 70, 
										new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.red, 230));
								optionTest.addOption("Option1");
								optionTest.addOption("Option2");
								optionTest.addOption("Option3");
								scrollTestMenuEntry.addEntry(optionTest);
								
								PetrisSliderEntry sliderTest = new PetrisSliderEntry("Slider:", gameFont.deriveFont(baseFontSize + 16F), gameSize.width, 70, 
										new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.pink, 230));
								scrollTestMenuEntry.addEntry(sliderTest);
								
								cloneEntry = new PetrisMenuEntry("0.Clone this entry", gameFont.deriveFont(baseFontSize + 16F), gameSize.width, 35, 
										new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
								cloneEntry.setOnOk(new Action() {
									@Override
									public void run() {
										PetrisMenuEntry newClone = new PetrisMenuEntry(cloneEntry);
										++cloneCounter;
										newClone.setText("" + cloneCounter + ".Clone this entry");
										scrollTestMenuEntry.addEntry(newClone);
									}
								});
								
								
								scrollTestMenuEntry.addEntry(cloneEntry);
								
							
							testsMenuEntry.addEntry(scrollTestMenuEntry);
							filterMenuEntry = new PetrisOptionMenuEntry("Select render filter:", gameFont.deriveFont(baseFontSize + 16F), 
									(int)gameSize.getWidth(), 70, new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.yellow, 230));
							filterMenuEntry.setOnRight(new Action(){
								@Override
								public void run() {
									Game.this.render.nextFilter();
									filterMenuEntry.setOptionText(Game.this.render.getCurrentFilter().toString());
								}
							});
							filterMenuEntry.setOnLeft(new Action(){
								@Override
								public void run() {
									Game.this.render.previousFilter();
									filterMenuEntry.setOptionText(Game.this.render.getCurrentFilter().toString());
								}
							});
							filterMenuEntry.setOptionText(render.getCurrentFilter().toString());
							testsMenuEntry.addEntry(filterMenuEntry);
							
							
							testsMenuEntry.addEntry(backMenuEntry);
						
						
						settingsMenuEntry.addEntry(testsMenuEntry);
						
					settingsMenuEntry.addEntry(backMenuEntry);
					
					//--------------------------------------------------
			
			
		mainMenu.addEntry(settingsMenuEntry);
		
		PetrisMenuEntry quitMenuEntry = new PetrisMenuEntry("Quit", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,  
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.red, 230));
		quitMenuEntry.setOnOk(new Action() {
			@Override
			public void run() {
				System.exit(0);
			}
		});
		mainMenu.addEntry(quitMenuEntry);
	}
	
	private void initializePauseMenu()
	{
		
		pauseMenu = new PetrisChildMenu("Paused", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
		pauseMenu.setRootMenu(mainMenu);
		
		PetrisMenuEntry resumeMenuEntry = new PetrisMenuEntry("Resume", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.getWidth(), 40,  
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
		resumeMenuEntry.setOnOk(new Action(){
			@Override
			public void run() {
				Game.this.pause();
			}
		});
		pauseMenu.addEntry(resumeMenuEntry);
		pauseMenu.addEntry(settingsMenuEntry);
		pauseMenu.setOnEntered(new Action(){
			@Override
			public void run() {
				Game.this.resumeOnEsc = true;
			}
		});
		pauseMenu.setOnReturnedFromChild(new Action(){
			@Override
			public void run() {
				Game.this.resumeOnEsc = true;
			}
		});
		pauseMenu.setOnEnteringChild(new Action(){
			@Override
			public void run() {
				Game.this.resumeOnEsc = false;
			}
		});
		pauseMenu.setOnExiting(new Action(){
			@Override
			public void run() {
				Game.this.resumeOnEsc = false;
			}
		});
		
		PetrisChildMenu confirmReturnToMain = new PetrisChildMenu("Return to Main Menu", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.red, 230));
		
		PetrisMenuEntry backToMainEntry = new PetrisMenuEntry("Confirm", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.red, 230));
		backToMainEntry.setOnOk(new Action() {
			@Override
			public void run() {
				Game.this.closePausedMenu();
				Game.this.showMainMenu();
			}
		});
		PetrisMenuEntry cancelReturnToMain = new PetrisMenuEntry("Cancel", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.yellow, 230));
		cancelReturnToMain.setOnOk(new Action(){
			@Override
			public void run() {
				Game.this.mainMenu.performBack();
			}
		});
		confirmReturnToMain.addEntry(backToMainEntry);
		confirmReturnToMain.addEntry(cancelReturnToMain);
		pauseMenu.addEntry(confirmReturnToMain);
		
	}


	private PetrisMenuEntry acceptNameEntry;
	private TextFieldMenuEntry nameTextEntry;
	private void initializeFirstLaunchDialog() {
		firstLaunchDialog = new PetrisChildMenu("Welcome!", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230));
		
		nameTextEntry = new TextFieldMenuEntry("Insert your profile name:", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 70, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230), "press Enter and type");
		nameTextEntry.setOnExitingEdit(new Action(){
			@Override
			public void run() {
				if (nameTextEntry.getFieldText() == "" || nameTextEntry.getFieldText() == null 
						|| nameTextEntry.getFieldText().length() == 0) acceptNameEntry.setEnabled(false);
				else acceptNameEntry.setEnabled(true);
			}
		});
		firstLaunchDialog.addEntry(nameTextEntry);
		
		acceptNameEntry = new PetrisMenuEntry("Done", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230), false);
		acceptNameEntry.setOnOk(new Action() {
			@Override
			public void run() {
				Game.this.globals.currentProfile = Game.this.dataLoader.loginAs(nameTextEntry.getFieldText());
				Game.this.mainMenu.setCanGoBack(true);
				Game.this.mainMenu.performBack();
				Game.this.smallMessage.show("Welcome, " + globals.currentProfile.getName() + "!", Color.green, 2000, 500);
				Game.this.setDefaults();
				
			}
		});
		firstLaunchDialog.addEntry(acceptNameEntry);
		firstLaunchDialog.setOnEntered(new Action(){
			@Override
			public void run() {
				mainMenu.setCanGoBack(false);
			}
		});

	}
	
	private PetrisMenuEntry accept2NameEntry;
	private TextFieldMenuEntry name2TextEntry;
	private PetrisChildMenu createProfileDialog;
	private void initializeCreateProfileDialog() {
		createProfileDialog = new PetrisChildMenu("Create new profile", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.cyan, 230));
		
		name2TextEntry = new TextFieldMenuEntry("Insert your profile name:", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 70, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230), "press Enter and type");
		name2TextEntry.setOnExitingEdit(new Action(){
			@Override
			public void run() {
				if (name2TextEntry.getFieldText() == "" || name2TextEntry.getFieldText() == null 
						|| name2TextEntry.getFieldText().length() == 0) accept2NameEntry.setEnabled(false);
				else accept2NameEntry.setEnabled(true);
			}
		});
		createProfileDialog.addEntry(name2TextEntry);
		
		accept2NameEntry = new PetrisMenuEntry("Done", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230), false);
		accept2NameEntry.setOnOk(new Action() {
			@Override
			public void run() {
				Game.this.globals.currentProfile = Game.this.dataLoader.loginAs(name2TextEntry.getFieldText());
				Game.this.mainMenu.setCanGoBack(true);
				//Game.this.mainMenu.resetRootEntries();
				Game.this.addProfilesToMenu(profileChildMenu);
				Game.this.mainMenu.performBack();
				Game.this.mainMenu.performBack();
				Game.this.smallMessage.show("Welcome, " + globals.currentProfile.getName() + "!", Color.green, 2000, 500);
				Game.this.setDefaults();
			}
		});
		createProfileDialog.addEntry(accept2NameEntry);
		createProfileDialog.setOnEntered(new Action(){
			@Override
			public void run() {
				mainMenu.setCanGoBack(true);
			}
		});
		createProfileDialog.setOnExiting(new Action(){
			@Override
			public void run() {
				Game.this.addProfilesToMenu(profileChildMenu);
			}
		});
	}
	
	private PetrisMenuEntry accept3NameEntry;
	private TextFieldMenuEntry name3TextEntry;
	private PetrisChildMenu renameProfileDialog;
	private void initializeRenameProfileDialog() {//TODO: what happens eith a used new name?
		renameProfileDialog = new PetrisChildMenu("Rename profile", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.cyan, 230));
		
		name3TextEntry = new TextFieldMenuEntry("Insert the new name:", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 70, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.orange, 230), "press Enter and type");
		name3TextEntry.setOnExitingEdit(new Action(){
			@Override
			public void run() {
				if (name3TextEntry.getFieldText() == "" || name3TextEntry.getFieldText() == null 
						|| name3TextEntry.getFieldText().length() == 0) accept3NameEntry.setEnabled(false);
				else accept3NameEntry.setEnabled(true);
			}
		});
		renameProfileDialog.addEntry(name3TextEntry);
		
		accept3NameEntry = new PetrisMenuEntry("Done", gameFont.deriveFont(baseFontSize + 16F), (int)gameSize.width, 40, 
				new FadingColor(new Color(50,50,50,230), 230), new FadingColor(Color.green, 230), false);
		accept3NameEntry.setOnOk(new Action() {
			@Override
			public void run() {
				String tmp = name3TextEntry.getFieldText();
				Game.this.dataLoader.renameProfile(Game.this.mainMenu.getTitle(), tmp);
				//Game.this.globals.currentProfile = Game.this.dataLoader.loginAs(name3TextEntry.getFieldText());
				//WHY RESETS EVERYTHING???
				Game.this.mainMenu.setCanGoBack(true);
				//Game.this.mainMenu.resetRootEntries();
				Game.this.addProfilesToMenu(profileChildMenu);
				Game.this.mainMenu.performBack();
				Game.this.mainMenu.performBack();
				Game.this.mainMenu.performBack();
				Game.this.smallMessage.show("Profile renamed to: " + tmp, Color.green, 2000, 500);
				//Game.this.setDefaults();
			}
		});
		renameProfileDialog.addEntry(accept3NameEntry);
		renameProfileDialog.setOnEntered(new Action(){
			@Override
			public void run() {
				mainMenu.setCanGoBack(true);
			}
		});
		renameProfileDialog.setOnExiting(new Action(){
			@Override
			public void run() {
				name3TextEntry.setFieldText("");
				Game.this.addProfilesToMenu(profileChildMenu);
			}
		});
	}


	public void menuGeneralizedKeyboardInput(KeyEvent keyCode)
	{
		mainMenu.performGeneralizedKeyboardInput(keyCode);
	}

	public void saveAllPrefs()
	{
		savePref("bg_style");
		savePref("square_style");
		savePref("custom1_red");
		savePref("custom1_green");
		savePref("custom1_blue");
		savePref("custom2_red");
		savePref("custom2_green");
		savePref("custom2_blue");
		savePref("bg_custom_mode");
		savePref("menu_blur_bg");
	}
	
	public void loadAllPrefs()
	{
		loadPref("bg_style");
		loadPref("square_style");
		loadPref("custom1_red");
		loadPref("custom1_green");
		loadPref("custom1_blue");
		loadPref("custom2_red");
		loadPref("custom2_green");
		loadPref("custom2_blue");
		loadPref("bg_custom_mode");
		loadPref("menu_blur_bg");
	}
	
	public void setDefaults()
	{
		curBackgroundStyle = BackgroundStyles.FlashingGravity;
		curStyle = SquareStyle.noBorder;
		redSlider1.setValue(0);
		greenSlider1.setValue(0);
		blueSlider1.setValue(0);
		redSlider2.setValue(0);
		greenSlider2.setValue(0);
		blueSlider2.setValue(0);
		backgroundPaintMode.setSelected("Vertical Gradient");
		renderModeEntry.setChecked(true);
		saveAllPrefs();
		loadAllPrefs();
	}

	public void savePref(String pref)
	{
		
		switch(pref)
		{
		case "bg_style":
		{
			dataLoader.setPref(pref, curBackgroundStyle.ordinal(), globals.currentProfile.getID());
			break;
		}
		case "square_style":
		{
			dataLoader.setPref(pref, curStyle.ordinal(), globals.currentProfile.getID());
			break;
		}
		case "custom1_red":
		{
			dataLoader.setPref(pref, redSlider1.getValue(), globals.currentProfile.getID());
			break;
		}
		case "custom1_green":
		{
			dataLoader.setPref(pref, greenSlider1.getValue(), globals.currentProfile.getID());
			break;
		}
		case "custom1_blue":
		{
			dataLoader.setPref(pref, blueSlider1.getValue(), globals.currentProfile.getID());
			break;
		}
		case "custom2_red":
		{
			dataLoader.setPref(pref, redSlider2.getValue(), globals.currentProfile.getID());
			break;
		}
		case "custom2_green":
		{
			dataLoader.setPref(pref, greenSlider2.getValue(), globals.currentProfile.getID());
			break;
		}
		case "custom2_blue":
		{
			dataLoader.setPref(pref, blueSlider2.getValue(), globals.currentProfile.getID());
			break;
		}
		case "bg_custom_mode":
		{
			dataLoader.setPref(pref, backgroundPaintMode.getSelected(), globals.currentProfile.getID());
			break;
		}
		case "menu_blur_bg":
		{
			int tmp = 0;
			if (!this.renderModeEntry.isChecked()) tmp = 1;
			dataLoader.setPref(pref, tmp, globals.currentProfile.getID());
			break;
		}
		}
	}
	
	public void loadPref(String pref)
	{
		switch(pref)
		{
		case "bg_style":
		{
			int mode = dataLoader.getIntPref(pref, globals.currentProfile.getID());
			curBackgroundStyle = BackgroundStyles.values()[mode];
			updateBackground();
		}
		case "square_style":
		{
			int style = dataLoader.getIntPref(pref, globals.currentProfile.getID());
			curStyle = SquareStyle.values()[style];
			updateSquareStyle();
			break;
		}
		case "custom1_red":
		{
			redSlider1.setValue(dataLoader.getIntPref(pref, globals.currentProfile.getID()));
			if(curBackgroundStyle == BackgroundStyles.Custom)updateCustomBackground();
			break;
		}
		case "custom1_green":
		{
			greenSlider1.setValue(dataLoader.getIntPref(pref, globals.currentProfile.getID()));
			if(curBackgroundStyle == BackgroundStyles.Custom)updateCustomBackground();
			break;
		}
		case "custom1_blue":
		{
			blueSlider1.setValue(dataLoader.getIntPref(pref, globals.currentProfile.getID()));
			if(curBackgroundStyle == BackgroundStyles.Custom)updateCustomBackground();
			break;
		}
		case "custom2_red":
		{
			redSlider2.setValue(dataLoader.getIntPref(pref, globals.currentProfile.getID()));
			if(curBackgroundStyle == BackgroundStyles.Custom)updateCustomBackground();
			break;
		}
		case "custom2_green":
		{
			greenSlider2.setValue(dataLoader.getIntPref(pref, globals.currentProfile.getID()));
			if(curBackgroundStyle == BackgroundStyles.Custom)updateCustomBackground();
			break;
		}
		case "custom2_blue":
		{
			blueSlider2.setValue(dataLoader.getIntPref(pref, globals.currentProfile.getID()));
			if(curBackgroundStyle == BackgroundStyles.Custom)updateCustomBackground();
			break;
		}
		case "bg_custom_mode":
		{
			backgroundPaintMode.setSelected(dataLoader.getStrPref(pref,  globals.currentProfile.getID()));
			if(curBackgroundStyle == BackgroundStyles.Custom)updateCustomBackground();
			break;
		}
		case "menu_blur_bg":
		{
			boolean tmp = false;
			if (dataLoader.getIntPref(pref, globals.currentProfile.getID()) != 0) tmp = true;
			renderModeEntry.setChecked(tmp);
			render.setLayerBasedFiltersDisabled(!tmp);
			break;
		}
		}
	}







	public void resize(Dimension size) {
		
		if (resizeEnabled) 
		{
			Dimension ssize = GuiUtils.getScaledDimension(new Dimension(300,667), size);
			System.out.println(""+size.width+" - "+size.height+" --> "+ssize.width+" - "+ssize.height);
			gameSize=ssize;
			render.setSize(ssize);
			bgLayer.setSize(ssize);
			bgLayer2.setSize(ssize);
			tetriGrid.setSize(ssize);
			colorPreviewLayer.setSize(ssize);
			ghostGrid.setSize(ssize);
			removeLineAnimation.setSize(ssize);
			nextTextLabel.updatePosition(ssize);
			rightGuiPanel.updatePosition(ssize);
			nextLayer.updatePosition(ssize);
			smallMessage.setParentSize(ssize);
			messageBox.setParentSize(ssize);
			secondMessage.setParentSize(ssize);
			thirdMessage.setParentSize(ssize);
			fourthMessage.setParentSize(ssize);
			
			mainMenu.setSize(ssize);
			Point centeredGame = GuiUtils.getCenteredChildRectCoords2(new Point(0,0), size, ssize);
			render.setLocation(centeredGame);
			System.out.println("(" + centeredGame.x+", "+centeredGame.y+")");
		}
		else{
			Point centeredGame = GuiUtils.getCenteredChildRectCoords2(new Point(0,0), size, gameSize);
			render.setLocation(centeredGame);
		}
		
	}







	public TetrisGrid getTetrisGrid() {
		return tetriGrid;
	}
	


}
