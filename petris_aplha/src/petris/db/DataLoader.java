package petris.db;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import petris.Profile;

public class DataLoader {
	Connection connection = null;
	Statement statement;
	public final String CURRENT_SCHEMA_VERSION = "0.1";
	private boolean schemaOutOfDate = true;
	

	public static void copyFile(File source, File dest)

			throws IOException {

		Files.copy(source.toPath(), dest.toPath());

	}
	
	public void openConnection()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:petris.petris");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from savefile_metadata");
			rs.next();
			if (getMetadata("schema_version") != CURRENT_SCHEMA_VERSION) 
				schemaOutOfDate = true;
			else schemaOutOfDate = false;
		}
		catch(SQLException e)
		{
			// if the error message is "out of memory", 
			// it probably means no database file is found
			System.err.println(e.getMessage());//TODO: log
			System.err.println("Probably database is empty. ");//Defining new schema...");
			//createNewSchema(false);
		} 
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean schemaExists()
	{
		Connection tmpConn = null;
		try{
			Class.forName("org.sqlite.JDBC");
			// create a database connection
			tmpConn = DriverManager.getConnection("jdbc:sqlite:petris.petris");
			Statement tmpStat = tmpConn.createStatement();

			tmpStat.setQueryTimeout(30);
			ResultSet rs = tmpStat.executeQuery("select * from savefile_metadata");
			rs.next();
			if (getMetadata("schema_version", tmpConn) != CURRENT_SCHEMA_VERSION) 
				schemaOutOfDate = true;
			else schemaOutOfDate = false;
		}
		catch(SQLException e)
		{
			// if the error message is "out of memory", 
			// it probably means no database file is found
			System.err.println(e.getMessage());//TODO: log
			System.err.println("Probably database is empty.");
			return false;

		} 		
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally{
			try{
				if (tmpConn != null) tmpConn.close();
			}
			catch (SQLException e)
			{
				// connection close failed
				e.printStackTrace();
			}
		}
		return true;
	}

	public void createNewSchema(boolean withBackup)
	{
		try{
			if (withBackup)
				createBackup();
		}
		catch(IOException e)
		{
			//TODO: log
			System.out.println("Unable to create a backup file."); //TODO: continue? abort? ask?
		}
		Statement stat;
		if (connection == null) {
			try{
				connection = DriverManager.getConnection("jdbc:sqlite:petris.petris");
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				System.err.println("Create save file failed.");
			}
		}
		try {
			
			stat = connection.createStatement();
			stat.setQueryTimeout(30);  // set timeout to 30 sec.
			
			stat.executeUpdate("drop table if exists profiles");
			stat.executeUpdate("drop table if exists savefile_metadata");
			//stat.executeUpdate("drop table if exists match_stats");
			//stat.executeUpdate("drop table if exists scores");
			//stat.executeUpdate("drop table if exists prefs");
			//stat.executeUpdate("drop table if exists colors");
			stat.executeUpdate("create table profiles ("
					+ "id integer primary key autoincrement, "
					+ "name text, "
					+ "last_logged text not null, "
					+ "last_modified text"
					+ ")");
			stat.executeUpdate("create table savefile_metadata ("
					+ "id text primary key,"
					+ "value text "
					+ ")");
			stat.executeUpdate("insert into savefile_metadata(id,value)"
					+ "values('schema_version','" + CURRENT_SCHEMA_VERSION + "')");
			/*
			stat.executeUpdate("create table scores ("
					+ "id integer primary key autoincrement, "
					+ "petris_version text, "
					+ "score integer not null, "
					+ "multiplier integer, "
					+ "lines integer, "
					+ "started text, " //not null
					+ "ended text, " //not null
					+ "power text"
					+ ")");*/
			/*
			stat.executeUpdate("create table match_stats ("
					+ "id integer primary key autoincrement, "
					+ "match integer references scores(id), "
					+ "event text, "
					+ "timestamp text"
					+ ")");*/
			/*
			stat.executeUpdate("create table prefs("
					+ "id integer primary key autoincrement , "
					+ "profile integer references profiles(id), "
					+ "background text, "
					+ "power text, "
					+ "custom_color integer "
					+ ")");*/
			/*
			stat.executeUpdate("create table colors("
					+ "id integer primary key autoincrement, "
					+ "color_mode text, "
					+ "color_r1 integer, " //check between 0 and 255?
					+ "color_g1 integer, "
					+ "color_b1 integer, "
					+ "color_r2 integer, "
					+ "color_g2 integer, "
					+ "color_b2 integer "
					+ ")");*/
			//statement.executeUpdate("insert into person values(1, 'leo')");
			//statement.executeUpdate("insert into person values(2, 'yui')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createBackup() throws IOException
	{
		copyFile(new File("petris.petris"), new File("petris_backup.petris"));
	}

	public Profile loginAs(String profile) {

		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			ResultSet rs = statement.executeQuery("select count(*) from profiles where name = '" +profile + "'");
			rs.next();
			if (rs.getInt(1) == 1)
			{
				statement.executeUpdate("update profiles "
						+ "set last_logged = datetime('now')"
						+ "where name = '" + profile + "'");
				return new Profile(profile);
			}
			else if (rs.getInt(1) == 0)
			{
				statement.executeUpdate("insert into profiles(name, last_logged, last_modified)"
						+ "values('" + profile + "', "
						+ "datetime('now'), "
						+ "datetime('now'))");
				return new Profile(profile);
			}
			else return new Profile("Guest"); //TODO: greater than 1? More people with same name?
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to login or to create a new user.");
		}

	}

	public String getLastLogged() throws RuntimeException
	{
		String result = "ERROR";
		try {
			ResultSet rs =  statement.executeQuery("select name from profiles order by name desc");
			result = rs.getString("name");
		} catch (SQLException e) {
			throw new RuntimeException("No profiles yet");
		}
		return result;
	}

	public void closeConnection()
	{
		try
		{
			if(connection != null)
				connection.close();
		}
		catch(SQLException e)
		{
			// connection close failed.
			System.err.println(e);
		}
	}

	public boolean isSchemaOutOfDate() {
		return schemaOutOfDate;
	}
	
	public String getMetadata(String key)
	{
		ResultSet rs = null;
		try {
			rs = statement.executeQuery("select value from savefile_metadata "
					+ "where id = '" + key + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getMetadata(String key, Connection conn)
	{
		ResultSet rs = null;
		try {
			Statement stat = conn.createStatement();
			rs = stat.executeQuery("select value from savefile_metadata "
					+ "where id = '" + key + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


}
