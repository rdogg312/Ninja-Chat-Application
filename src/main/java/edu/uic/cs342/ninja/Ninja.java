package edu.uic.cs342.ninja;

import edu.uic.cs342.ninja.server.Server;
import edu.uic.cs342.ninja.client.ServerSelect;

/**
 * This class simply either spawns the GUI client side based on the arguments or the server side
 * that serves the clients.  If neither is called, an error pops up on the screen alerting user of
 * the valid arguments that are available.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @category    Project #04 - Ninja: Chat Application
 * @package     Ninja
 * @author      Rafael Grigorian
 * @author      Byambasuren Gansukh
 * @license     The MIT License, see LICENSE.md
 */
public class Ninja {

	/**
	 * This is the driver main method.  Based on the arguments it spawns either the client GUI
	 * application of the back end server that serves the clients.  If nether is passed, then it
	 * will warn users about the available types of arguments that can be passed.
	 * @param   String []       args                An array of arguments in string form
	 * @return  void
	 * @static
	 */
	public static void main ( String [] args ) {
		// Check if the client was trying to be spawned
		if ( args.length > 0 && args [ 0 ].toLowerCase ().equals ( "client" ) ) {
			// Spawn the server select GUI
			new ServerSelect ();
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
			// Inform use
			System.out.println ( "Invalid command was passed!" );
			System.out.println ( "\tThe argument 'client' will spawn a client application (GUI)" );
			System.out.println ( "\tThe argument 'server' will spawn the back end server" );
		}
	}

}
