package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class implements the actual server, and is responsible for spawning worker threads to handle
 * incoming connections from clients.  It also contains an array of all connections, and an array of
 * clients which means that the connection is logged in to our server with credentials.  These are
 * tupled with a username so it was easy to differentiate between the socket connections.  It also
 * holds a Handler class that is used in the Respond class to handle asynchronous operations.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @category    Project #04 - Ninja: Chat Application
 * @package     Server
 * @author      Rafael Grigorian
 * @author      Byambasuren Gansukh
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
public class Server {

	/**
	 * This is an instance of the handler class.  It is meant to group the handler functions
	 * together.  Inside we handler all types of valid requests and serve responses.
	 * @var     Handler                 handler             The server's handler functions
	 */
	protected Handler handler;

	/**
	 * This is the server socket that is created and it uses the current host's hostname, and the
	 * port number that is passed to the constructor.
	 * @var     ServerSock              sock                The server socket
	 */
	protected ServerSocket sock;

	/**
	 * This is an array list of Respond instances that are made to handle a socket connection.  They
	 * are made up of client connection sockets and useful functions to operate on them.
	 * @var     ArrayList <Respond>     connections         All the client Respond instances
	 */
	protected ArrayList <Respond> connections;

	/**
	 * This holds an array of tuples which bind a socket connection to a username.  This is used to
	 * figure out which users are actually logged in right now.
	 * @var     ArrayList <Tuple>       clients             All logged in client sockets
	 */
	protected ArrayList <Tuple> clients;

	/**
	 * This constructor initializes a server socket, based on the passed port, and initialized the
	 * array list of client server sockets.
	 * @param   int                     port                The port number for the server socket
	 */
	public Server ( int port ) throws Exception {
		// Initialize the server socket
		this.sock = new ServerSocket ( port );
		// Initialize the array list of client server sockets
		this.connections = new ArrayList <Respond> ();
		// Initialize an array list of clients (username, socket)
		this.clients = new ArrayList <Tuple> ();
		// Initialize the handler class
		this.handler = new Handler ( this );
	}

	/**
	 * This function infinitely waits for an incoming connection, accepts it and adds it to the
	 * connections array list.
	 * @return  void
	 */
	public void accept () throws Exception {
		// Print that we are now accepting connections
		System.out.println ( "Waiting for clients to connect ..." );
		// Loop forever
		while ( true ) {
			// Accept the socket connection and save the client socket reference
			Socket current = this.sock.accept ();
			// Alert that a connection was caught
			System.out.println ( "A client connection was detected" );
			// Initialize a Respond instance
			Respond respond = new Respond ( this, current );
			// Add Respond object to the array of connections
			this.connections.add ( respond );
			// Create a new thread and let the helper class handle response
			Thread thread = new Thread ( respond );
			// Start the thread
			thread.start ();
		}
	}

	/**
	 * This function binds the respond instance for the connection with a username and appends it to
	 * the clients array, effectively signifying that that connection has logged in successfully and
	 * the client is currently online.
	 * @param   String          username            The username associated with connection
	 * @param   Respond         connection          The Respond instance holding socket connection
	 * @return  void
	 */
	protected void addClient ( String username, Respond connection ) {
		// Check that the user is already not in the clients array
		if ( this.findClient ( username ) == null ) {
			// Bind the username and connection together and append to the clients array list
			this.clients.add ( new Tuple ( username, connection ) );
		}
	}

	/**
	 * This function removes a user from the clients array list, and it also removes the connection
	 * from the connections array list.  In addition it also closes the socket connection with that
	 * client.  This effectively signifies that a user is logged out.
	 * @param   String          username            The username to look for to remove from clients
	 * @return  void
	 */
	protected void removeClient ( String username ) {
		// Loop though all the clients in the client array
		for ( int i = 0; i < this.clients.size (); i++ ) {
			// Check if the username matches the one binded to the connection
			if ( this.clients.get ( i ).first ().equals ( username ) ) {
				// Remove from the connections array and disconnect client
				( ( Respond ) this.clients.get ( i ).second () ).removeConnection ( true );
				// Remove from the clients array ( online )
				this.clients.remove ( i );
				// Return because we are done
				return;
			}
		}
	}

	/**
	 * This function finds a user connection based on a passed username.  If the client is not
	 * found, then null is returned.  In a way this function can serve another purpose.  If you want
	 * to see if a user is online, you can call this function and see if null is returned.  If it is
	 * then the user is not currently logged in.  Otherwise the username binded with the Respond
	 * instance is returned as a Tuple object.
	 * @param   String          username            The target username to search for
	 * @return  Tuple                               The username binded with a Respond instance
	 */
	protected Tuple findClient ( String username ) {
		// Traverse through all the logged in users
		for ( Tuple client : this.clients ) {
			// Check to see if the client is binded with the target username
			if ( client.first ().toString ().equals ( username ) ) {
				// Return the found client
				return client;
			}
		}
		// By default return null
		return null;
	}

	/**
	 * This function sends a message to all clients that are logged in.  It uses the stored Respond
	 * instance to send the message individually.
	 * @param   String          message             The message to send out to everyone
	 * @return  void
	 */
	protected void sendAllClients ( String message ) {
		// Traverse through all logged in clients
		for ( Tuple client : this.clients ) {
			// Get the Respond instance for that client
			Respond iterator = ( Respond ) client.second ();
			// Write message to that socket
			iterator.write ( message );
		}
	}

	/**
	 * This function traverses the array of logged in clients and returns a list of usernames as
	 * strings.
	 * @return  ArrayList <String>                  An array of username strings (currently online)
	 */
	protected ArrayList <String> clientsOnline () {
		// Create an empty array list initially
		ArrayList <String> result = new ArrayList <String> ();
		// Traverse through the clients array
		for ( Tuple client : this.clients ) {
			// Append the username to the array
			result.add ( client.first ().toString () );
		}
		// Return the resulting array
		return result;
	}

}