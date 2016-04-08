import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Connection implements Runnable {

	/**
	 * This data member is here to save the reference of the initial server select class.  It is
	 * here because we use it to control error message output and to disable text fields and buttons
	 * while the connection is verified.
	 * @var 	ServerSelect 	parent 				The server select instance for GUI output
	 */
	private ServerSelect parent;

	/**
	 * This data member saves the login window that we spawn, it is used to handle logging into an
	 * account.
	 * @var 	Login 			login 				The login window instance spawned by this class
	 */
	private Login login;

	/**
	 * This data member holds the reference to the chat application class and is used for
	 * interacting with the message packets.
	 * @var 	ChatApplication application 		The chat application reference
	 */
	private ChatApplication application;

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
	 * This constructor disables GUI elements during verification, and initializes a socket
	 * connection with the user.
	 * @var 	SeverSelect 	parent 				This is a reference to the parent caller
	 * @var 	String 			ip 					The IP address to connect socket to
	 * @var 	int 			port 				The port to connect socket to
	 */
	public Connection ( ServerSelect parent, String ip, int port ) {
		// Save the parent reference handle
		this.parent = parent;
		// While verifying connection, disable GUI elements
		this.parent.connect.setEnabled ( false );
		this.parent.ipAddress.setEnabled ( false );
		this.parent.port.setEnabled ( false );
		// Try to create a socket connection
		try {
			// Create a socket connection using passed IP address and port number
			this.connection = new Socket ( ip, port );
			// Initialize the print writer for sending data to the server
			this.output = new PrintWriter ( this.connection.getOutputStream (), true );
			// Initialize an input buffer reader
			this.input = new BufferedReader (
				// Create a new instance of the input stream reader using the socket
				new InputStreamReader ( this.connection.getInputStream () )
			); 
		}
		// Attempt to catch exception
		catch ( Exception exception ) {
			// Re-Enable the GUI elements
			this.parent.connect.setEnabled ( true );
			this.parent.ipAddress.setEnabled ( true );
			this.parent.port.setEnabled ( true );
			// Print error message
			this.parent.alert.setText (
				"<html>Oh no! Something went wrong when we were creating your socket!<html>"
			);
		}
	}

	/**
	 * This function simply sends the passed message to the socket connection
	 * @var 	String 			message 			The message to send
	 * @return  return
	 */
	protected void send ( String message ) {
		// Send the message to the server
		this.output.println ( message );
	}

	/**
	 * This function attempts to read the input from the server and returns the result.  If there
	 * are errors, then null is returned.
	 * @return  String  							The string that was read from the socket
	 */
	private String read () {
		// Attempt to read the socket
		try {
			// Initialize the string and the integer based character variable
			String output;
			// Loop until it's null
			while ( ( output = this.input.readLine () ) == null ) {}
			// Return the string
			return output;
		}
		// If any exceptions are thrown
		catch ( Exception exception ) {
			// Return null to notify error
			return null;
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

	private void handler ( JSONObject json ) {
		// First check that the object is not null
		if ( json != null ) {
			// Save the type
			String type = json.get ( "type" ).toString ();
			// Check to see if the type is a login response or create request
			if ( type.equals ( "login" ) || type.equals ( "create" ) ) {
				// Check if it was a success or a failure
				if ( json.get ( "status" ).toString ().equals ( "success" ) ) {
					// Create the new chat application window
					this.application = new ChatApplication ( this, json );
					// Close the login window
					this.login.dispose ();
				}
				else {
					// Re-Enable login components
					this.login.username.setEnabled ( true );
					this.login.password.setEnabled ( true );
					this.login.account.setEnabled ( true );
					this.login.login.setEnabled ( true );
					// Set the error message to login screen
					this.login.alert.setText (
						"<html>" + json.get ( "message" ).toString () + "</html>"
					);
				}
			}
			// Check to see if the type is a message response
			else if ( type.equals ( "message" ) ) {
				System.out.println ( "okay im in messages at least" );
				// Get current messages
				Messages current = this.application.messageArea.groups.getCurrentMessages ();

				// Get the group by searching with hash
				Group target = this.application.messageArea.groups.getGroupByHash (
					json.get ( "hash" ).toString ()
				);

				if ( target != null ) {
					if ( current == target.getMessages () ) {
						current.addMessage (
							json.get ( "message" ).toString (),
							json.get ( "from" ).toString (),
							json.get ( "timestamp" ).toString (),
							json.get ( "from" ).toString ().equals ( this.application.username )
						);
						System.out.println ( "message was for current tab" );
					}
					else {
						target.getMessages ().addMessage (
							json.get ( "message" ).toString (),
							json.get ( "from" ).toString (),
							json.get ( "timestamp" ).toString (),
							json.get ( "from" ).toString ().equals ( this.application.username )
						);
						target.setRead ( false );
						// Play opening sound effect
						try {
							// Load in the audio file as a stream and play it
							AudioInputStream audioInputStream = AudioSystem.getAudioInputStream (
									this.getClass ().getResource ( "./assets/audio/New-Message.wav" )
							);
							Clip clip = AudioSystem.getClip ();
							clip.open ( audioInputStream );
							clip.start ();
						}
						// Catch any exception, if caught, we want to ignore this error
						catch ( Exception exception ) {}
					}
				}
				else {
					Groups groups = this.application.messageArea.groups;
					String name = json.get ( "name" ).toString ();
					String hash = json.get ( "hash" ).toString ();
					ArrayList <String> users = new ArrayList <String> ();
					for ( Object user : ( ArrayList <Object> ) json.get ( "users" ) ) {
						users.add ( user.toString () );
					}
					Group group = groups.addGroup ( name, hash, users );
					group.setSelected ( true );
					group.getMessages ().addMessage (
						json.get ( "message" ).toString (),
						json.get ( "from" ).toString (),
						json.get ( "timestamp" ).toString (),
						json.get ( "from" ).toString ().equals ( this.application.username )
					);
					groups.setCurrentMessage ( hash );
					// Play opening sound effect
					try {
						// Load in the audio file as a stream and play it
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream (
								this.getClass ().getResource ( "./assets/audio/New-Message.wav" )
						);
						Clip clip = AudioSystem.getClip ();
						clip.open ( audioInputStream );
						clip.start ();
					}
					// Catch any exception, if caught, we want to ignore this error
					catch ( Exception exception ) {}
					System.out.println ( "group does not exist locally" );
				}
			}
			// Check to see if the type is an online notifier
			else if ( type.equals ( "online" ) ) {
				String username = json.get ( "username" ).toString ();
				boolean online = Boolean.parseBoolean ( json.get ( "value" ) );
				User target = this.application.menuArea.users.findUser ( username );
				if ( target != null ) {
					target.setOnline ( online );
				}
			}
			// Check to see if it is a new user notifier
			else if ( type.equals ( "created" ) ) {
				String username = json.get ( "username" ).toString ();
				boolean online = Boolean.parseBoolean ( json.get ( "value" ) );
				User newUser = new User ( username, online );
				this.application.menuArea.users.append ( newUser );
			}
		}
	}

	/**
	 * This function is implemented from the Runnable class and it first verifies if the connection
	 * is to a valid Ninja server.  If it isn't the tread dies, if it is a valid connection then the
	 * Login window is spawned and this thread is dedicated to listening to the incoming messages.
	 * @return  void
	 */
	public void run () {
		// Read the response from the server and parse it as JSON
		String response = this.read ();
		JSONObject json = this.parse ( response );
		// If it is not in JSON format or an invalid response is sent, then mark as invalid
		if ( json != null && json.get ( "status" ).equals ( "fail" ) ) {
			// Close the server select window
			this.parent.dispose ();
			// Spawn the login window
			this.login = new Login ( this );
			// Loop forever listening to messages
			while ( true ) {
				// Read the response
				response = this.read ();
				// Pass to the handler to handle each case
				this.handler ( this.parse ( response ) );
			}
		}
		// If it is not a valid connection to a Ninja server
		else {
			// Set the GUI elements to be enabled again
			this.parent.connect.setEnabled ( true );
			this.parent.ipAddress.setEnabled ( true );
			this.parent.port.setEnabled ( true );
			// Set an error message and let thread and object die
			this.parent.alert.setText (
				"<html>Invalid credentials have been passed!  Please make sure that the Ninja server is started.<html>"
			);
		}
	}

}