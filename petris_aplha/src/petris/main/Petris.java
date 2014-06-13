package petris.main;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import petris.*;
import petris.db.DataLoader;
import petris.gui.DebugConsole;
import petris.gui.Render;
import petris.gui.RenderInterface;
import petris.gui.TAdapter;

public class Petris {
	
	private static Game game;
	private static JFrame gameFrame;	
	private static Render gameRender;	
	private static Render guiRender;	
	private static TAdapter keyboardAdapter;
	private static boolean sidegui = false;
	private static GlobalVarSet globals;
	private static Profile currentProfile;
	public static DebugConsole console;
	private static DataLoader dataLoader = new DataLoader();
	private static boolean isFirstLaunch = false;

	
	
	public static void main(String[] args)
	{
		gameFrame = new JFrame("Petris");
		gameFrame.setLocation(new Point(450,100));
		
		int y = 689; 
		int x = 300;
		if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0)
		{
			y = 689;
			x = 300;
		}
		else if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
		{
			y = 695;
			x = 306;
		}
		System.out.println(System.getProperty("os.name") + " --> Frame Size = (" + x + ", " + y + ").");
		
		if (sidegui)gameFrame.setSize(x + 150,y);
		else gameFrame.setSize(x,y);
		gameFrame.setResizable(false);		
		
		gameFrame.getContentPane().setBackground(new Color(0,0,0));		
		
		gameRender = new Render(Color.black, new Dimension(300,689));
		gameFrame.add(gameRender, BorderLayout.CENTER);
		if (sidegui)
		{
			guiRender = new Render(new Color(20,20,20), new Dimension(150,689));
			gameFrame.add(guiRender,BorderLayout.EAST);
			game = new Game(new Dimension(300,667), gameRender, guiRender);
		}
		else game = new Game(new Dimension(300,667), gameRender, null);
		
		if(dataLoader.schemaExists())
		{
			dataLoader.openConnection();
			if (dataLoader.isSchemaOutOfDate())
			{
				dataLoader.updateSchema();
			}
			String lastLogged = null;
			try{
				lastLogged = dataLoader.getLastLogged();
				currentProfile = dataLoader.loginAs(lastLogged);
			}
			catch(RuntimeException e)
			{
				firstLaunch();
			}			
		}
		else firstLaunch();
		
		globals = new GlobalVarSet(game,(RenderInterface)gameRender,currentProfile,"pre-Alpha 0.41");
		game.setGlobals(globals);
		gameFrame.setTitle("Petris (" + globals.petrisVersion + ")");
		
		game.setFirstLaunch(isFirstLaunch);
		game.setDataLoader(dataLoader);
		
		console = new DebugConsole(globals);
		
		keyboardAdapter = new TAdapter(game,console);
		gameFrame.addKeyListener(keyboardAdapter);
		console.addKeyListener(keyboardAdapter);
		gameFrame.setVisible(true);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gameFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				dataLoader.closeConnection();
				System.exit(0);
			}
		});
		
		game.init(isFirstLaunch);
	}
	
	public static void firstLaunch()
	{
		dataLoader.createNewSchema(false);
		isFirstLaunch = true;
	}
	
	
	
	
}

