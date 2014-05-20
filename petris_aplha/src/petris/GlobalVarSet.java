package petris;

import java.util.ArrayList;
import java.util.Collections;

import petris.gui.RenderInterface;

public class GlobalVarSet {

	public Game currentGame;
	
	public int idealFps;
	public int realFps;
	public boolean inGame;
	public Profile currentProfile;
	public String petrisVersion;
	
	public RenderInterface currentRender;
	
	public ArrayList<Integer> highscores;
	public ArrayList<String> highscoreProfiles;
	
	public GlobalVarSet(Game game, RenderInterface render, Profile profile, String version) {
		currentGame = game;
		currentRender = render;
		currentProfile  =profile;
		petrisVersion = version;
		highscores = new ArrayList<Integer>();
		highscoreProfiles = new ArrayList<String>();
	}
	
	public int addHighscore(String profile, int score)
	{
		Comparable<Integer> cmp = (Comparable<Integer>) score;
		int rank= highscores.size()-1;
		highscores.add(score);
		for (int i = highscores.size()-1; i > 0 && cmp.compareTo(highscores.get(i-1)) < 0; i--) 
		{
			Collections.swap(highscores, i, i-1);
			rank = i;
		}
		if (highscoreProfiles.isEmpty()) highscoreProfiles.add(profile);
		else highscoreProfiles.add(rank, profile);
		//printHighscores();
		return rank;
	}
	
	
	
	private void printHighscores() {
		System.out.println("----------");
		for (int i = 0; i < highscores.size(); ++i)
			System.out.println("" + (i+1) + "-" + highscoreProfiles.get(i) + " - " + highscores.get(i));		
	}

	public void getFreeMem()
	{
		
	}
	

}
