package Server;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This class implements the Runnable class and it is used when the server spawns threads to handle
 * incoming requests.  This class basically wraps the Socket along with other useful functions that
 * can operate on said socket connections.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @category    Project #04 - Ninja: Chat Application
 * @package     Server
 * @author      Rafael Grigorian
 * @author      Byambasuren Gansukh
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
public class Respond implements Runnable {

	/**
	 * This data member holds an instance to the parent object that spawned it.  This object is the
	 * main server object and is holds the arrays for Respond instances.
	 * @var     Server          parent              The reference to the parent Server instance
	 */
	private Server parent;

	/**
	 * This data member is the socket connection that this thread is taking care of.  It is solely
	 * tied to this thread, and once it dies the tread should die too.
	 * @var     Socket          connection          The client socket connection for this thread
	 */
	private Socket connection;

	/**
	 * This data member initializes a Scanner string reader for reading input from the socket file
	 * descriptor.
	 * @var     BufferedReader  input               Buffered string reader for socket
	 */
	private BufferedReader input;

	/**
	 * This data member initializes a printing string writer used to send a message back to the\
	 * client from the server.
	 * @var     PrintWriter     output              String writer for socket
	 */
	private PrintWriter output;

	/**
	 * This constructor saves the passed reference objects and initializes the string reader and
	 * writer.
	 * @param   Server          parent              The reference to parent Server calling instance
	 * @param   Socket          connection          The socket designated for this worker thread
	 */
	public Respond ( Server parent, Socket connection ) {
		// Save the parent instance and the socket connection
		this.parent = parent;
		this.connection = connection;
		// Attempt to initialize the input and output string writers
		try {
			// Initialize an input buffer reader
			this.input = new BufferedReader (
				// Create a new instance of the input stream reader using the socket
				new InputStreamReader ( this.connection.getInputStream () )
			); 
			// Initialize the output print writer
			this.output = new PrintWriter ( this.connection.getOutputStream (), true );
		}
		// Seriously, this shouldn't fail...
		catch ( Exception exception ) {}
	}

	/**
	 * This function uses the internally stored socket to read the data being sent to the server
	 * from the client.  it attempts to read all the data and turn it into a string.  On error, null
	 * is returned.
	 * @return  String                              The read input string from client socket
	 */
	private String read () {
		// Try to read from the socket
		try {
			// Initialize request string to the input
			String request;
			// Loop until value is not null
			while ( ( request = this.input.readLine () ) == null ) {}
	    	// Return the result
	        return request;
	    }
	    // Catch the exception if one is thrown
	    catch ( Exception exception ) {
	    	// Return null is it was a failure
	    	return null;
	    }
	}

	/**
	 * This function sends a message back to the client using the socket saved internally.  It is
	 * important to note that there is no return type marking success or not.
	 * @param   String          response            The string to send to client
	 * @return  void
	 */
	protected void write ( String response ) {
		// Write string to print writer
		this.output.println ( response );
		this.output.flush ();
	}

	/**
	 * This function removes itself from the parents connections array.  This is used when a client
	 * doesn't follow the rules of the laid out API.  It also can close the connection if the
	 * disconnect flag is passed as true.
	 * @param   boolean         disconnect          Should we close the socket too?
	 * @return  void
	 */
	protected void removeConnection ( boolean disconnect ) {
		// Traverse the list of Respond requests
		for ( int i = 0; i < this.parent.connections.size (); i++ ) {
			// If we hit our instance of ourself
			if ( this.parent.connections.get ( i ) == this ) {
				// Remove it from the array list
				this.parent.connections.remove ( i );
				// Break out of loop
				break;
			}
		}
		// If the disconnect flag is true
		if ( disconnect ) {
			// Try to close connection
			try {
				// Close the connection
				this.connection.close ();
			}
			// If all fails, well we don't care
			catch ( Exception exception ) {}
		}

	}

	/**
	 * This function takes in a string that is in JSON format and it attempts to parse it into a
	 * java json object.  If it fails, or if the string is not in valid JSON format, then this
	 * function returns null to signify that an error has occurred.
	 * @param   String          json                The input JSON string to parse
	 * @return  JSONObject                          The resulting java json object
	 */
	private JSONObject parse ( String json ) {
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
	 * This function is implemented from the Runnable class, and when the thread instance is
	 * spawned, it runs this function on start.  This function basically validates a user connection
	 * and passes it to the appropriate handler, which is contained in the parents internal data
	 * structure.
	 * @return  void
	 */
	public void run () {
		// Loop forever, reading
		while ( true ) {
			// Parse the request that was given
			JSONObject request = this.parse ( this.read () );
			// If the request is valid JSON
			if ( request != null ) {
				// Make sure we have a type in our json object
				if ( request.get ( "type" ) != null ) {
					// If the type of request is a login
					if ( request.get ( "type" ).equals ( "login" ) ) {
						this.parent.handler.handleLogin ( this, request );
					}
					// If the type of request is a create account request
					else if ( request.get ( "type" ).equals ( "create" ) ) {
						this.parent.handler.handleCreate ( this, request );
					}
					// If the type of request is a message push request
					else if ( request.get ( "type" ).equals ( "message" ) ) {
						this.parent.handler.handleMessage ( this, request );
					}
					// If the type of request is a logout request
					else if ( request.get ( "type" ).equals ( "logout" ) ) {
						this.parent.handler.handleLogout ( this, request );
					}
					// Otherwise, handle default case
					else {
						// If it was no of the former request types, then close the connection
						this.removeConnection ( true );
					}
				}
			}
		}
	}

}