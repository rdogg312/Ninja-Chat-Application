import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class Ninja {

	public static void main ( String [] args ) {

		String json = "{\"type\":\"login\",\"status\":\"success\",\"public_key\":\"SERVER_KEY\",\"username\":\"NULL\",\"users\":[{\"username\":\"NULL\",\"online\":true},{\"username\":\"BennyS\",\"online\":false},{\"username\":\"TheHolyBeast\",\"online\":true}],\"groups\":[{\"name\":\"Everybody\",\"hash\":\"0\",\"users\":[\"Everybody\"],\"messages\":[{\"from\":\"NULL\",\"timestamp\":\"04/04/2016 - 12:23:53\",\"message\":\"Hey what's up man!\"},{\"from\":\"TheHolyBeast\",\"timestamp\":\"04/04/2016 - 12:24:02\",\"message\":\"Hey!\"}]},{\"name\":\"CS342\",\"hash\":\"SFVG67RE6GVS8SHCA7SCGDHSKAFIUFDSHAOW\",\"users\":[\"NULL\",\"BennyS\"],\"messages\":[{\"from\":\"NULL\",\"timestamp\":\"04/04/2016 - 12:27:22\",\"message\":\"What up Ben!\"}]}]}";

		// Initialize json parser
		JSONParser parser = new JSONParser();
		// Try to parse the json string
		try {
			// Parse and cast as a JSON object
        	JSONObject object = ( JSONObject ) parser.parse ( json );
        	// Pass the json object to the chat application constructor
        	//new ChatApplication ( object );
            new ServerSelect ();
        }
        // Attempt to catch any parse exceptions
        catch ( ParseException exception ) {
     		// Print error and exit
        	System.out.println ( exception.toString () );
        	System.exit ( 0 );
        }
		
	}

}