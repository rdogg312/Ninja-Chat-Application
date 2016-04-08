package Server;

import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
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

public class GroupDB {

	// @param 	String 		folderpath 		"../assets/databases/groups/"

	private static 	final String FOLDERPATH = "./assets/databases/groups/";
	private static	String FILEPATH; 
	private JSONObject  group_json;
	private String 		groupname;
	private String 		group_hash;
	private JSONArray	users;

	private JSONArray	messages;

	public GroupDB ( String group_hash, String groupname, JSONArray users) {
		// Check to see that directories exist, if not create them
		// Load into internal data structure ( filename (aka hash_id), contents (parsed as JSONObject) )
		FILEPATH = FOLDERPATH + group_hash + ".db";

		if(!createFileAndPath())
		{
			load();
		}
		else
		{		
			initGroupJSON(group_hash, groupname, users);
		}
	}

	protected void initGroupJSON(String group_hash, String groupname, JSONArray users)
	{
		this.group_json = new JSONObject();
		this.group_hash = group_hash;
		this.groupname = groupname;
		this.users = users;
		this.messages = new JSONArray();

		this.group_json.put("name", this.groupname);
		this.group_json.put("hash", this.group_hash);
		this.group_json.put("users", this.users);
		this.group_json.put("messages", this.messages);
		System.out.println(this.group_json.toString());
		update();
	}

	protected boolean createFileAndPath()
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

	private static JSONObject parse ( String json ) {
		// Initialize json parser
		JSONParser parser = new JSONParser ();
		// Try to parse the json string
		try {
			// Parse and cast as a JSON object
			JSONObject object = ( JSONObject ) parser.parse ( json );
			// Return JSON object
			return object;
		}
		// Attempt to catch any parse exceptions
		catch ( ParseException exception ) {
			// If there was an error, return null
			return null;
		}
	}

	private synchronized boolean load()
	{
		// Load file content to internal datastructure
		
		try
		{
			FileReader fileReader = new FileReader(FILEPATH);

			BufferedReader buffReader = new BufferedReader(fileReader);

			StringBuilder stringBuilder = new StringBuilder();

			String line = null;

			while((line = buffReader.readLine()) != null)
			{
				if(!line.equals(""))
				{
					stringBuilder.append(line);
				}
			}
			group_json = parse(stringBuilder.toString());
			this.groupname = group_json.get("name").toString();
			this.group_hash = group_json.get("hash").toString();
			this.users = (JSONArray) group_json.get("users");
			this.messages = (JSONArray) group_json.get("messages");
			if(group_json == null)
				return false;
			return true;
			
		}
		catch(Exception e)
		{
			System.err.println("GroupDB load(): " + e);
			return false;
		}
	}

	protected void addMessage ( String from, String timestamp, String message ) {
		// Append it to the json data in the [group_hash].db file
		JSONObject messageJSON = new JSONObject();
		messageJSON.put("from", from);
		messageJSON.put("timestamp", timestamp);
		messageJSON.put("message", message);

		this.messages.add(messageJSON);
		this.group_json.put("messages", this.messages);
		update();
	}

	protected JSONObject getGroupJSON()
	{
		return this.group_json;
	}

	public void update () {
		// Finds the contents based on group hash in internall data structure array
		// Stringifys it and saves it back into file (hash_id is filename)
		FileWriter fileWriter = null;
		BufferedWriter buffWriter = null;
		PrintWriter printWriter = null;

		try
		{
			File file = new File(FILEPATH);
			fileWriter = new FileWriter(file);
			buffWriter = new BufferedWriter(fileWriter);
			printWriter = new PrintWriter(buffWriter);
			printWriter.println(this.group_json.toString());
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

	public static JSONArray getGroups( String username )
	{
		JSONArray groups = new JSONArray();

		File dir = new File(FOLDERPATH);
		File[] list = dir.listFiles();

		ArrayList<String> fileNames = new ArrayList<String>();
		for(File file : list)
		{
			String name = file.getName();
			if(name.contains(".db"))
				fileNames.add(name);
		}	

		try
		{
			for(String filename : fileNames)
			{
				FileReader fileReader = new FileReader(FOLDERPATH + filename);

				BufferedReader buffReader = new BufferedReader(fileReader);

				StringBuilder stringBuilder = new StringBuilder();

				String line = null;

				while((line = buffReader.readLine()) != null)
				{
					if(!line.equals(""))
					{
						stringBuilder.append(line);
					}
				}

				JSONObject group = (JSONObject) parse(stringBuilder.toString());
				if(group != null && (group.toString().contains(username) || group.toString().contains("Everybody")))
					groups.add(group);
				
			}
			return groups;
			
		}
		catch(Exception e)
		{
			System.err.println("GroupDB load(): " + e);
			
		}
		return null;
	}
	
}
