package petris;

import petris.gui.RenderInterface;

public class GlobalVarSet {

	public Game currentGame;
	
	public int idealFps;
	public int realFps;
	public boolean inGame;
	public Profile currentProfile;
	public String petrisVersion;
	
	public RenderInterface currentRender;
	
	public GlobalVarSet(Game game, RenderInterface render, Profile profile, String version) {
		currentGame = game;
		currentRender = render;
		currentProfile  =profile;
		petrisVersion = version;
	}
	
	public void getFreeMem()
	{
		
	}
	

}
