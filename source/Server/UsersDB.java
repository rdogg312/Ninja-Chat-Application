package Server;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.lang.Exception;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.lang.StringBuilder;

public class UsersDB {

	// @param 	String 		filepath 		"../assets/databases/users/user.db"
	protected  	static 	ArrayList<User> 	USERS;
	private		static 	final 	String 		FILEPATH = "../assets/databases/users/user.db";

	public UsersDB () {
		// Does path folder and users.db file exist?
		USERS = new ArrayList<User>();

		if(!createFileAndPath())
			//read all the contents and store internally (Username,password_hash)
			load();
	}

	protected static boolean createFileAndPath()
	{
		File file = new File(FILEPATH);

		// Create parent directories if file is child 
		if(file.getParentFile() != null)
			file.getParentFile().mkdirs();

		try
		{
			// Create a new file
			// True if the file doesn't exist and successfully create the file
			// False otherwise.
			return file.createNewFile();
		}
		catch(Exception e)
		{
			System.out.println("UsersDB createFilePath(): " + e);
			return false;
		}
	}

	protected static boolean deleteFile( )
	{
	   File file = new File(FILEPATH);
	   System.out.println(file.getName());
	   return file.delete();
	}

	protected boolean userExists ( String username ) {
		// Traverse through internal data structure
		String inUsername_lowered = username.toLowerCase();
		for(User user : USERS)
		{
			String username_lowered = user.getUsername().toLowerCase();
			if(inUsername_lowered.equals(username_lowered))
				return true;
		}
		return false;
	}

	protected boolean userAdd ( String username, String password) {
		// Check if username exists
		// If it doesn't then append to data structure
		// Export back into file 
		String password_hash = hash(password);
		if(!userExists( username ))
		{
			USERS.add(new User(username, password_hash));
			update(username, password_hash);
			return true;
		}
		return false;
	}

	protected boolean userLogin ( String username, String passedPassword ) {
		// Check if username exists
		// Hash the passed password
		// Compare hashed passed password with real hased password
		String passedPassword_hash = hash(passedPassword);
		for(User user : USERS)
			if(user.getUsername().equals(username)
				&& user.getPassword_hash().equals(passedPassword_hash))
				return true;
		return false;
	}

	private void update (String username, String password_hash) {
		// Converts internal datastructure back into file format and saves it back in

		FileWriter fileWriter = null;
		BufferedWriter buffWriter = null;
		PrintWriter printWriter = null;

		try
		{
			File file = new File(FILEPATH);
			fileWriter = new FileWriter(file, true);
			buffWriter = new BufferedWriter(fileWriter);
			printWriter = new PrintWriter(buffWriter);
			printWriter.println(username + "\t" + password_hash);
		}
		catch(Exception e)
		{
			System.err.println("UsersDB update(): " + e);
		}
		finally
		{
			if(printWriter != null)
				printWriter.close();
		}
	}

	private void load()
	{
		// Load file content to internal datastructure
		try
		{
		FileReader fileReader = new FileReader(FILEPATH);

		BufferedReader buffReader = new BufferedReader(fileReader);

		String line = null;

		while((line = buffReader.readLine()) != null)
		{
			if(!line.equals(""))
			{
				String[] lineContents = line.split("\\s+");
				String tempUsername = lineContents[0];
				String tempPassword = lineContents[1];

				User tempUser = new User(tempUsername, tempPassword);
				USERS.add(tempUser);
			}
		}
		}
		catch(Exception e)
		{
			System.err.println("UsersDB load(): " + e);
		}
	}

	protected String hash ( String password ) {
		// hash function
		// Using MD5 algorithm from MessageDigest library
		String password_hash = null;

		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] passwordBytes = md.digest();

			StringBuilder stringBuilder = new StringBuilder();

			for(byte b : passwordBytes)
			{
				stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));		
			}

			password_hash = stringBuilder.toString();
		}
		catch(Exception e)
		{
			System.err.println("UsersDB hash(): " + e);
		}

		return password_hash;
	}

}
