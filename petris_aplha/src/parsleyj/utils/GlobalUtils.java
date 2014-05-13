package parsleyj.utils;

public class GlobalUtils {

	public static boolean checkInterval(int val, int min, int max)
	{
		return (min <= val && val <= max);
	}
	
	public static String boolToString(boolean x)
	{
		if (x) return "true";
		else return "false";
	}
}
