package petris;

public class Profile {

	private String name;
	private boolean guestProfile;
	private int xp;
	private int classicGamesCount;
	private int totalLines;
	private int bestScore;
	
	public Profile()
	{
		name = "Guest";
		guestProfile = true;
		setXP(0);
		setClassicGamesCount(0);
		setBestScore(0);
	}
	
	public Profile(String profilename)
	{
		name = profilename;
		guestProfile = false;
		setXP(0);
		setClassicGamesCount(0);
		setBestScore(0);
	}
	
	public void setName(String arg)
	{
		this.name = arg;
	}
	public String getName()
	{
		return name;
	}
	
	public boolean isGuest()
	{
		return guestProfile;
	}

	public int getXP() {
		return xp;
	}

	public void setXP(int xp) {
		this.xp = xp;
	}

	public int getClassicGamesCount() {
		return classicGamesCount;
	}

	public void setClassicGamesCount(int classicGamesCount) {
		this.classicGamesCount = classicGamesCount;
	}

	public int getBestScore() {
		return bestScore;
	}

	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}

	public int getTotalLines() {
		return totalLines;
	}

	public void setTotalLines(int totalLines) {
		this.totalLines = totalLines;
	}
	
	
}
