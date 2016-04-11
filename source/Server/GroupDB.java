package Server;

import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
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

/**
 * This class is meant to create a new group file whenever a group is created.
 * The file would in the form of JSON and it would contain the group name, group hash, array of 
 * users in the group, array of messages, where each message consists of a timestamp, username of 
 * the person sending the message, as well as the actual content of the message. Also the names of 
 * the files are hashed hence the "group hash" stated above.
 *
 * This class then allows us to store the messages sent to the groups and when a user logs back in
 * the messages load in to her application on all the groups that user is in.
 * 
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @category    Project #04 - Ninja: Chat Application
 * @package     Server
 * @author      Rafael Grigorian
 * @author      Byambasuren Gansukh
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
public class GroupDB {

	/**
	 * This internal data member saves the database folder path in a static final modifier.
	 * @var     String           FOLDERPATH          The path to the database folder
	 */
	private static 	final String FOLDERPATH = "./assets/databases/groups/";

	/**
	 * This internal data member saves the database file path in a static final modifier.
	 * @var     String           FILEPATH          The path to the database file
	 */
	private static	String FILEPATH; 

	/**
	 * This internal data member stores the complete JSON object to be stored stored in
	 * the database file.
	 * @var     JSONObject           group_json         Main JSON object
	 */
	private JSONObject  group_json;

	/**
	 * This internal data member stores the name of the group.
	 * @var     String           groupname          Name of the group
	 */
	private String 		groupname;

	/**
	 * This internal data member stores the hash of the group name;
	 * @var     String           group_hash          hash of the group name
	 */
	private String 		group_hash;

	/**
	 * This internal data member stores the array of users in a JSON array.
	 * @var     JSONArray           users          Array of users
	 */
	private JSONArray	users;

	/**
	 * This internal data member stores the array of messages which are JSON objects 
	 * in a JSON array.
	 * @var     JSONArray           users          Array of messages
	 */
	private JSONArray	messages;


	/**
	 * This constructor creates the file and the path to the database file and fills
	 * it with the passed in parameters if the file for the group hasn't been made yet.
	 * Otherwise, the file exists, thus it loads the contents from the file and inits the 
	 * data members.
	 * @param   String          group_hash         Value to init data member group_hash
	 * @param   String          groupname          Value to init data member groupname
	 * @param   JSONArray       users              Value to init data member users
	 */
	public GroupDB ( String group_hash, String groupname, JSONArray users) {
		
		// Init the FILEPATH based on the group_hash
		FILEPATH = FOLDERPATH + group_hash + ".db";

		// Check to see whether the file exists or not 
		if(!createFileAndPath())
		{
			// File exists thus load the contents of the file and init the data members
			load();
			// Then update the file back to preserve file contents
			update ();
		}
		else
		{		
			// File doesn't exist thus initialize the data members
			// This function will update the data members to the file at the end
			initGroupJSON(group_hash, groupname, users);
		}
	}

	/**
	 * This function simply initializes a new group database. Fills the data members then
	 * updates it to the created file.
	 * @param   String          group_hash         Value to init data member group_hash
	 * @param   String          groupname          Value to init data member groupname
	 * @param   JSONArray       users              Value to init data member users
	 * @return  void
	 */
	protected void initGroupJSON(String group_hash, String groupname, JSONArray users)
	{
		// Init the main Data members
		this.group_json = new JSONObject();
		this.group_hash = group_hash;
		this.groupname = groupname;
		this.users = users;
		this.messages = new JSONArray();

		// Populate the main JSONObject this.group_json
		this.group_json.put("name", this.groupname);
		this.group_json.put("hash", this.group_hash);
		this.group_json.put("users", this.users);
		this.group_json.put("messages", this.messages);

		// Print the JSONObject as a string to the console for debugging.
		System.out.println(this.group_json.toString());

		// Update the file with the this.group_json
		update();
	}

	/**
	 * This function checks whether the file specified by FILEPATH exists or not.
	 * If it doesn't exist then it creates otherwise it doesn't do anything.
	 * @return  boolean		Returns true when the file doesn't exist, false otherwise.
	 */
	protected boolean createFileAndPath()
	{
		// Create a new File object
		File file = new File(FILEPATH);

		// Create parent directories if file is a child of folder(s)
		if(file.getParentFile() != null)
			file.getParentFile().mkdirs();

		try
		{
			// Create a new file
			// .createNewFile() returns true if the file is created, false otherwise
			return file.createNewFile();
		}
		// Catch exceptions
		catch(Exception e)
		{
			// Print debug message
			System.out.println("UsersDB createFilePath(): " + e);
			return false;
		}
	}

	/**
	 * This function takes in a string that is in JSON format and it attempts to parse it into a
	 * java json object.  If it fails, or if the string is not in valid JSON format, then this
	 * function returns null to signify that an error has occurred.
	 * @param   String          json                The input JSON string to parse
	 * @return  JSONObject                          The resulting java json object
	 */
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
		catch ( Exception exception ) {
			// If there was an error, return null
			return null;
		}
	}

	/**
	 * This function loads in the contents of the file into a String using a StringBuilder.
	 * The String is then parsed to a JSONObject and then we populate our data members from the
	 * JSONObject.
	 * 
	 * Synchronized to make sure no two threads try to load at the same time.
	 * @return  boolean                          Returns false if group_json is null, true
	 *										     otherwise.
	 */
	private synchronized boolean load()
	{
		try
		{
			// Create file objects 
			FileReader fileReader = new FileReader(FILEPATH);
			BufferedReader buffReader = new BufferedReader(fileReader);
			StringBuilder stringBuilder = new StringBuilder();

			// Used to store each line
			String line = null;

			// Read the contents of the file line by line
			while((line = buffReader.readLine()) != null)
			{
				//Skip empty lines
				if(!line.equals(""))
				{
					// Populate the stringBuilder
					stringBuilder.append(line);
				}
			}

			// Parse the stringBuilder and init this.group_json
			group_json = parse(stringBuilder.toString());

			// Get the specific data from group_json to populate the other datamembers
			this.groupname = group_json.get("name").toString();
			this.group_hash = group_json.get("hash").toString();
			this.users = (JSONArray) group_json.get("users");
			this.messages = (JSONArray) group_json.get("messages");

			// Check if group_json is null. If null return false, true otherwise.
			if(group_json == null)
				return false;
			return true;
			
		}
		// Catch exceptions
		catch(Exception e)
		{
			// Print debug message
			System.err.println("GroupDB load(): " + e);
			return false;
		}
	}

	/**
	 * This function creates a new message JSONObject then it updates the file with the new 
	 * message JSONObject.
	 * @param   String          from                Name of the sender
	 * @param   String          timestamp           Timestamp of when the message was sent
	 * @param   String          message             Content of the message
	 * @return  void
	 */
	protected void addMessage ( String from, String timestamp, String message ) {
		
		//Create a new JSONObject
		JSONObject messageJSON = new JSONObject();

		// Populate the object with the passed in data.
		messageJSON.put("from", from);
		messageJSON.put("timestamp", timestamp);
		messageJSON.put("message", message);

		// Add to data member messages
		this.messages.add(messageJSON);
		// Add the messages to the group_json
		this.group_json.put("messages", this.messages);
		// Update the file 
		update();
	}

	/**
	 * Getter for this.group_json
	 * @return  	JSONObject		this.group_json			Data member group_json
	 */
	protected JSONObject getGroupJSON()
	{
		return this.group_json;
	}

	/**
	 * This function updates the database file with the contents inside group_json.
	 *
	 * Synchronized to make sure no two threads try to load at the same time.
	 * @return  	void
	 */
	public synchronized void update () {
		
		// Declare File objects
		FileWriter fileWriter = null;
		BufferedWriter buffWriter = null;
		PrintWriter printWriter = null;

		try
		{
			// Create file objects
			File file = new File(FILEPATH);
			fileWriter = new FileWriter(file);
			buffWriter = new BufferedWriter(fileWriter);
			printWriter = new PrintWriter(buffWriter);

			// Update file with group_json
			printWriter.println(this.group_json.toString());
		}
		// Catch exceptions
		catch(Exception e)
		{
			// Print debug message
			System.err.println("UsersDB update(): " + e);
		}
		finally
		{
			// Close out the printWriter
			if(printWriter != null)
				printWriter.close();
		}
	}

	/**
	 * This static function retrieves all the groups that have the passed in parameter as a user.
	 * This function makes it easy to figure out who to send message and what groups to open up
	 * when a user logs back in.
	 * @param   String              username        Name of the user to search for in the groupfiles
	 * @return  JSONArray			group 			JSONArray of groups that contain the username
	 */
	public static JSONArray getGroups( String username )
	{
		// Create a JSONArray
		JSONArray groups = new JSONArray();

		// Retrieve all the group files
		File dir = new File(FOLDERPATH);
		File[] list = dir.listFiles();

		// Retrieve the names of the group files
		ArrayList<String> fileNames = new ArrayList<String>();
		for(File file : list)
		{
			String name = file.getName();
			if(name.contains(".db"))
				fileNames.add(name);
		}	

		try
		{
			// Go through the files
			for(String filename : fileNames)
			{
				// Create file objects
				FileReader fileReader = new FileReader(FOLDERPATH + filename);
				BufferedReader buffReader = new BufferedReader(fileReader);
				StringBuilder stringBuilder = new StringBuilder();

				// Used to store each line
				String line = null;

				// Read the contents of the file line by line
				while((line = buffReader.readLine()) != null)
				{
					// Skip empty lines
					if(!line.equals(""))
					{
						// Populate the stringBuilder
						stringBuilder.append(line);
					}
				}

				// Create a JSONObject on the stringBuilder.toString()
				JSONObject group = (JSONObject) parse(stringBuilder.toString());

				// Add onto the JSONArray groups IFF the object ot be added is not null and contains
				// the username or "Everybody"
				if(group != null && 
					(group.toString().contains(username) 
					|| group.toString().contains("Everybody")))
						groups.add(group);
				
			}

			// Return the array of groups that have the username we're searching for
			return groups;
			
		}
		// Catch exceptions
		catch(Exception e)
		{
			// Print debug message
			System.err.println("GroupDB load(): " + e);
			
		}
		// Nothing found, return null
		return null;
	}
}
