import java.util.ArrayList;
public class GroupDB {

	// @param 	String 		folderpath 		"../assets/databases/groups/"
	public GroupDB ( String folderpath ) {
		// Check to see that directories exist, if not create them
		// Load into internal data structure ( filename (aka hash_id), contents (parsed as JSONObject) )
	}

	public String createGroup ( String groupName, String groupHash, ArrayList <String> users ) {
		// Based on input, initialize and populate a new [group_hash].db file
		return "";
	}

	protected void addMessage ( String groupHash, String from, String timestamp, String message ) {
		// Append it to the json data in the [group_hash].db file
	}

	public boolean groupExists ( String groupHash ) {
		// See if file exisits with the passed hash
		return false;
	}

	// Length is 36 for our implementation
	public String hash ( int length ) {
		// Copy from what I have in ChatArea.java
		return "";
	}

	// Generate Login json string
	public String getLoginDetails () {
		return "";
	}

	public void update ( String groupHash ) {
		// Finds the contents based on group hash in internall data structure array
		// Stringifys it and saves it back into file (hash_id is filename)
	}
	
}
