public class Ninja {

	public static void main ( String [] args ) {

        // Check if the client was trying to be spawned
        if ( args.length > 0 && args [ 0 ].toLowerCase ().equals ( "client" ) ) {
            // Spawn the server select GUI
            new ServerSelect ();
        }
        // Check if the server was trying to be spawned
        else if ( args.length > 0 && args [ 1 ].toLowerCase ().equals ( "server" ) ) {

            System.out.println ( "Spawn server object here!" );

        }
        // Otherwise print out usage
        else {

            System.out.println ( "Invalid command was passed!" );

        }
		
	}

}