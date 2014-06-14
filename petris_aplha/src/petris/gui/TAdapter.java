package petris.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import petris.Game;


public class TAdapter extends KeyAdapter {
	
	private Game game;
	private DebugConsole console;
	
	
	
	public TAdapter(Game g,DebugConsole c)
	{
		game = g;
		console = c;
	}
	
	public TAdapter(Game g) {
		game = g;
	}

	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_F3) console.toggleShow();
		
		game.menuGeneralizedKeyboardInput(e);
		
		
   	 	if (game.isInMenu())
   	 	{
   	 		int kc = e.getKeyCode();
   	 		switch (kc)
   	 		{
   	 		case KeyEvent.VK_UP:
   	 			game.menuNavUp();
   	 			break;
   	 		case KeyEvent.VK_DOWN:
   	 			game.menuNavDown();
   	 			break;
   	 		case KeyEvent.VK_LEFT:
   	 			game.menuNavLeft();
   	 			break;
   	 		case KeyEvent.VK_RIGHT:
   	 			game.menuNavRight();
   	 			break;
   	 		case KeyEvent.VK_ENTER:
   	 		case KeyEvent.VK_SPACE:
   	 			game.menuNavOk();
   	 			break;
   	 		case KeyEvent.VK_ESCAPE:
   	 			game.menuNavBack();
   	 			break;
   	 		}
   	 		return;
   	 	}
		
		if (!game.isStarted())
        {
			if (e.getKeyCode() == 'N' || e.getKeyCode() == 'n')	game.start();
			else if (e.getKeyCode() == 'T' || e.getKeyCode() == 't') game.nextSquareStyle();
			else if (e.getKeyCode() == 'Y' || e.getKeyCode() == 'Y') game.nextPower();
			else if (e.getKeyCode() == 'Q' || e.getKeyCode() == 'q') game.showMainMenu();
			return;
        }
		/*
        if (curPiece.getShape() == Tetrominoes.NoShape) {  
            return;
        }
        */

        int keycode = e.getKeyCode();

        if (keycode == 'p' || keycode == 'P' || keycode == KeyEvent.VK_ESCAPE) {
            game.pause();
            return;
        }

        if (game.isPaused())
        {
        	if (e.getKeyCode() == 'N' || e.getKeyCode() == 'n')	game.start();
			else if (e.getKeyCode() == 'T' || e.getKeyCode() == 't') game.nextSquareStyle();
			else if (e.getKeyCode() == 'Q' || e.getKeyCode() == 'q') game.showMainMenu();
    		return;
        }

        switch (keycode) {
        case KeyEvent.VK_LEFT:
        {
	       	 if(game.tryMoveLeft())
	       	 {
	       		 //ghostX -= 1;
	       		 //clearGhostBoard();
	             //drawGhost();
	                
	       	 }
	       	 break;
        }
        case KeyEvent.VK_RIGHT:
        {
	       	 if(game.tryMoveRight())
	       	 {
	       		 //ghostX += 1;
	       		 //clearGhostBoard();
	             //drawGhost();
	       	 }
	       	 break;
        }
            
        case KeyEvent.VK_DOWN:
        {
       	 if(game.tryRotateRight())
       	 {
       		 //curGhost.rotateRight();
       		 //clearGhostBoard();
             //drawGhost();
            }
       	 break;
        }
            
            
        case KeyEvent.VK_UP:
        {
	       	if(game.tryRotateLeft())
	       	{
	       	 //curGhost.rotateLeft();
	       	 //clearGhostBoard();
	         //drawGhost(); 
	       	}
	       	break;
        }
            
        case KeyEvent.VK_SPACE:
			game.dropDown();
			break; 
        case 'd':
        case 'D':
            game.oneLineDown();
            break;
        case 'n':
        case 'N':
        	game.start();
        	break;
        case 's':
        case 'S':
        	game.usePower();
        	break;
        case 'T':
        case 't':
        	game.nextSquareStyle();
        	break;
        case 'C':
        case 'c':
        	console.toggleShow();
        	break;
        /*
        case 'W':
        case 'w':
        	 decreaseSpeed();
        	 break;
        case 'S':
        case 's':
       	 increaseSpeed();
       	 break;
      	
        case 'c':
        case 'C':
       	 if (console.isVisible()) console.setVisible(false);
       	 else 
       		 {
       		 	console.setVisible(true);
       		 	//give focus to tetris JFrame..
       		 	Board.this.parentFrame.setVisible(false);
       		 	Board.this.parentFrame.setVisible(true);
       		 }
       	 break;
       	 */
        	
            
        }

    }

	


}
