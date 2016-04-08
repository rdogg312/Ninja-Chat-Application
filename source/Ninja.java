import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import Server.Server;
import Client.ServerSelect;

public class Ninja {

	public static void main ( String [] args ) {

		// Check if the client was trying to be spawned
		if ( args.length > 0 && args [ 0 ].toLowerCase ().equals ( "client" ) ) {
			// Spawn the server select GUI
			new ServerSelect ();

			// // Fake json for testing
			// String json = "{\"type\":\"login\",\"status\":\"success\",\"public_key\":\"SERVER_KEY\",\"username\":\"NULL\",\"users\":[{\"username\":\"NULL\",\"online\":true},{\"username\":\"BennyS\",\"online\":true},{\"username\":\"TheHolyBeast\",\"online\":false},{\"username\":\"HypeBeast\",\"online\":false},{\"username\":\"Clouds\",\"online\":false},{\"username\":\"TamerS\",\"online\":false}],\"groups\":[{\"name\":\"Everybody\",\"hash\":\"0\",\"users\":[\"Everybody\"],\"messages\":[{\"from\":\"TheHolyBeast\",\"timestamp\":\"04/04/2016 - 12:24:02\",\"message\":\"Hey!\"},{\"from\":\"Clouds\",\"timestamp\":\"04/04/2016 - 12:24:02\",\"message\":\"What up!\"},{\"from\":\"TamerS\",\"timestamp\":\"04/04/2016 - 12:24:02\",\"message\":\"@Unemployeed\"},{\"from\":\"BennyS\",\"timestamp\":\"04/04/2016 - 12:24:02\",\"message\":\"Ayyyye!\"},{\"from\":\"HypeBeast\",\"timestamp\":\"04/04/2016 - 12:24:02\",\"message\":\"What's Happening!\"},{\"from\":\"NULL\",\"timestamp\":\"04/04/2016 - 12:23:53\",\"message\":\"Hey what's up guys!\"}]},{\"name\":\"CS342\",\"hash\":\"SFVG67RE6GVS8SHCA7SCGDHSKAFIUFDSHAOW\",\"users\":[\"NULL\",\"BennyS\"],\"messages\":[{\"from\":\"NULL\",\"timestamp\":\"04/04/2016 - 12:27:22\",\"message\":\"What up Ben!\"},{\"from\":\"BennyS\",\"timestamp\":\"04/04/2016 - 12:24:02\",\"message\":\"Yo is the GUI done yet?\"},{\"from\":\"NULL\",\"timestamp\":\"04/04/2016 - 12:24:02\",\"message\":\"Yes ;)\"}]}]}";
			// // Initialize json parser
			// JSONParser parser = new JSONParser ();
			// // Try to parse the json string
			// try {
			// 	// Parse and cast as a JSON object
			// 	JSONObject object = ( JSONObject ) parser.parse ( json );
			// 	// Pass the json object to the chat application constructor
			// 	new ChatApplication ( object );
			// }
			// // Attempt to catch any parse exceptions
			// catch ( ParseException exception ) {
			// 	// Print error and exit
			// 	System.out.println ( exception.toString () );
			// 	System.exit ( 0 );
			// }
		}
		// Check if the server was trying to be spawned
		else if ( args.length > 0 && args [ 0 ].toLowerCase ().equals ( "server" ) ) {
			// Attempt to catch all exceptions
			try {
				// Initialize the Server server on specified port number
				Server server = new Server ( 2222 );
				// Loop forever, accepting connections
				server.accept ();
			}
			// Catch all exceptions
			catch ( Exception exception ) {
				// Print out failure
				System.out.println ( exception.toString () );
			}
		}
		// Otherwise print out usage
		else {
			System.out.println ( "Invalid command was passed!" );
		}
		
	}

}