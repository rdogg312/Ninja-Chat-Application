package edu.uic.cs342.ninja.client;

import java.awt.Cursor;
import java.awt.Color;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

/**
 * This class is an abstraction of a group and it inherits from the JLabel component because it also
 * serves a purpose as a tab element in the GUI.  It holds all information about the group as well
 * as a reference to the parent and the Messages instance that is binded to it.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @category    Project #04 - Ninja: Chat Application
 * @package     Client
 * @author      Rafael Grigorian
 * @author      Byambasuren Gansukh
 * @license     The MIT License, see LICENSE.md
 */
@SuppressWarnings ( "serial" )
public class Group extends JLabel {

	/**
	 * This data member saves the contents of the HTML template file for the group tabs.  It is used
	 * to replace certain variables and make the HTML custom to the group.
	 * @var     String          html                The HMTL template for a group tab
	 */
	private String html;

	/**
	 * This is the group hash.  It is unique to the group, and it is randomly generated on first
	 * group creation.
	 * @var     String          hash                Unique group hash
	 */
	private String hash;

	/**
	 * This data member holds the group name, and it does not have to be unique.  This name is
	 * defined when a user creates a group.
	 * @var     String          groupName           The group name
	 */
	private String groupName;

	/**
	 * This data member holds an instance to a Messages object, that is responsible for appending
	 * messages to itself, etc.
	 * @var     Messages        messages            An instance of the messages scrollable panel
	 */
	private Messages messages;

	/**
	 * This us an array list of strings that represent the users in the group.  There is always at
	 * least one person in a group (yourself).
	 * @var     ArrayList <String>  users           An array of users in this group
	 */
	protected ArrayList <String> users;

	/**
	 * This data member is a flag that signifies if a group has any messages that are unread.  If it
	 * is true there are none.
	 * @var     boolean         read                Are the messages in this group seen by the user?
	 */
	private boolean read;

	/**
	 * This flag signifies if the current group tab is selected or not.  If it is true then the
	 * group tab is selected.
	 * @var     boolean         selected            Is the current group selected by the user?
	 */
	private boolean selected;

	/**
	 * This data member saves the selected tab color of the group.  It is used for the GUI elements.
	 * @var     Color           selectedColor       The selected background color of the group tab
	 */
	private Color selectedColor;

	/**
	 * This data member saves the unselected tab color of the group.  It is used for the GUI
	 * elements.
	 * @var     Color           unSelectedColor     The unselected background color of the group tab
	 */
	private Color unSelectedColor;

	/**
	 * This data member saves the read notification background color as a string, to input into the
	 * template HTML for the group.  This color signifies that the group has been read.
	 * @var     String          readBackground      The color of the notification icon in group tab
	 */
	private String readBackground;

	/**
	 * This data member saves the read notification background color as a string, to input into the
	 * template HTML for the group.  This color signifies that the group has not been read.
	 * @var     String          unreadBackground    The color of the notification icon in group tab
	 */
	private String unreadBackground;

	/**
	 * This constructor takes in the group information and creates a JLabel from that, and styles it
	 * appropriately.
	 * @param   String              name                The name of the group
	 * @param   String              hash                The group hash id
	 * @param   ArrayList<String>   users               The users in this group
	 * @param   boolean             read                Initially, has the group been read by user?
	 */
	public Group ( String name, String hash, ArrayList <String> users, boolean read ) {
		// Run the super constructor
		super ( "" );
		// Save all the passed data and set initial values
		this.groupName = name;
		this.hash = hash;
		this.readBackground = "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAAH6ji2bAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAPlJREFUeNpiYEAHubl5/0E0E7IgQADBESNQ2gFI74erBQgg7MqwyDoCBBAD0QBD++TJkxixOghdMBFEAAQQafYgeRzhMwaGC0A7DTEU4vQxA4MiUMMDZPdcwKYKpAirdqDJClAnoACAABpAhB4854GUASw2gR45QGzwwBUzwXyKw8b96PGqQMiNTNBAPYBD/gK2lJKIJVYMcVoBihVsbgYIsMGPGAkpgHoL5rUHOFMnPgOBhiQAqfkE7EoEGr6AoIFoyYwQuIAec0xYsrQBCUFmgJ4H0UuYB2TEwwOcBkID3JEEwxzRI4kRX8IFUv1YggCULQrxZBvqAgCzcVLjA6vlOAAAAABJRU5ErkJggg==)";
		this.unreadBackground = "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAAH6ji2bAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAPlJREFUeNpiYEAHt33N/oNoJmRBgACCI0agtAOQ3g9XCxBA2JVhkXUECCAGogGGdtXNpxixOghdMBFEAAQQafYgeRzhMwaGC0A7DTEU4vQxA4MiUMMDZPdcwKYKpAirdqDJClAnoACAABpAhB4854GUASw2gR45QGzwwBUzwXyKw8b96PGqQMiNTNBAPYBD/gK2lJKIJVYMcVoBihVsbgYIsMGPGAkpgHoL5rUHOFMnPgOBhiQAqfkE7EoEGr6AoIFoyYwQuIAec0xYsrQBCUFmgJ4H0UuYB2TEwwOcBkID3JEEwxzRI4kRX8IFUv1YggCULQrxZBvqAgC7JFNnllH8wQAAAABJRU5ErkJggg==)";
		// this.readBackground = "url(data:image/png;base64," + Base64.getEncoder ().encodeToString ( this.loadHTML ("/images/no-new-message.png").getBytes () ) + ")";
		// this.unreadBackground = "url(data:image/png;base64," + Base64.getEncoder ().encodeToString ( this.loadHTML ("/images/new-message.png").getBytes () ) + ")";
		// this.readBackground = "url(file:assets/images/no-new-message.png)";
		// this.unreadBackground = "url(file:assets/images/new-message.png)";
		this.selectedColor = new Color ( 0xF3F2F3 );
		this.unSelectedColor = new Color ( 0xD3D3D3 );
		this.selected = false;
		this.html = this.loadHTML ( "/templates/group.tpl" );
		this.setRead ( read );
		this.users = users;
		// Create a new instance of the messages class
		this.messages = new Messages ( this );
		// Set the styling preferences for the JLabel
		super.setText ( this.setRead ( read ) );
		super.setCursor ( new Cursor ( Cursor.HAND_CURSOR ) );
		super.setOpaque ( true );
		super.setBackground ( this.unSelectedColor );
		super.setForeground ( new Color ( 0x6D6D6D ) );
	}

	/**
	 * This function marks the group as either read or unread based on the passed boolean.  It also
	 * updates the groups HTML template to reflect this in the notification area of the group tab.
	 * It then sends back that HTML to the caller.
	 * @param   boolean         read                What state to mark the read state
	 * @return  String                              The HTML for the changed results
	 */
	protected String setRead ( boolean read ) {
		// Save the read state internally
		this.read = read;
		// Copy the contents of the html file
		String contents = new String ( this.html );
		// Load in the group name
		contents = contents.replace ( "_GROUPNAME_", this.groupName );
		// If the state is marked as true ( read all messages )
		if ( read ) {
			// Set background of read box to be transparent
			contents = contents.replace ( "_READ_", this.readBackground );
		}
		// Otherwise assign a color to the read notification box
		else {
			// Set the color to the notification box
			contents = contents.replace ( "_READ_", this.unreadBackground );
		}
		// Save this into the label
		this.setText ( contents );
		// Return the resulting content
		return contents;
	}

	/**
	 * This function sets the group as being selected and returns the message area to the caller if
	 * the passed flag is to change to selected.  Otherwise it returns null.
	 * @param   boolean         selected            The selected flag to change group to
	 * @return  Messages                            The groups message area is returned if selected
	 */
	protected Messages setSelected ( boolean selected ) {
		// Save the passed selection boolean
		this.selected = selected;
		// If it is selected
		if ( selected ) {
			// Change the background color and set the read flag
			this.setBackground ( this.selectedColor );
			this.setRead ( true );
			// Return the messages instance
			return this.messages;
		}
		// Otherwise, it is not selected, set the background and return null
		this.setBackground ( this.unSelectedColor );
		return null;
	}

	/**
	 * This is a simply getter function that returns the groups name to the caller since it's set
	 * as private.
	 * @return  String                              The group name is returned to caller
	 */
	protected String getNames () {
		// Return the group name
		return this.groupName;
	}

	/**
	 * This is a simply getter function that returns the groups hash to the caller since it's set
	 * as private.
	 * @return  String                              The group hash is returned to caller
	 */
	protected String getHash () {
		// Return the group hash
		return this.hash;
	}

	/**
	 * This is a simple getter function that returns the groups hash to the caller since it's set as
	 * private.
	 * @return  Messages                            The groups message area instance is returned
	 */
	protected Messages getMessages () {
		// Return the message area instance
		return this.messages;
	}

	/**
	 * This is a simply getter function that returns the users in the group to the caller since it's set
	 * as private.  It is in the form of an array list of strings.
	 * @return  ArrayList <String>                  The users in this group
	 */
	protected ArrayList <String> getUsers () {
		// Return the string array list of users in the group
		return this.users;
	}

	/**
	 * This function takes in a filepath and loads the contents.  the contents of this file should
	 * be an HTML template for the groups tab.  It is then saved internally so we could replace the
	 * placeholders with actual data on initialization and on events.
	 * @param   String          filepath            The path to the HTML template file
	 * @return  String                              The contents of said file
	 */
	private String loadHTML ( String filepath ) {
		// Initialize contents variable
		String contents = "";
		// Try to open and read the target file
		try {
			// Create a scanner instance and open the target file
			Scanner scanner = new Scanner (
				getClass ().getResourceAsStream ( filepath )
			);
			// Read in the whole file into string
			contents = scanner.useDelimiter ( "\\A" ).next ();
			// Close the file descriptor
			scanner.close ();
		}
		// Attempt to catch any exception
		catch ( Exception exception ) {
			// If there is an error, then return empty string
			return "";
		}
		// Return the contents of the file
		return contents;
	}

	/**
	 * This function is a static function and is used to create a random group hash id.  This id is
	 * unique and is used as an identifier for a specific group.
	 * @param   int             length              The desired length of the hash
	 * @return  String                              The created unique hash id
	 * @static
	 */
	public static String createHash ( int length ) {
		// Initialize a random number generator
		Random rand = new Random ();
		// Initialize a library string to pull from
		String library = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		// Initialize hash string as empty string initially
		String hash = "";
		// Loop through n number of times, specified in the argument as the desired length
		for ( int i = 0; i < length; i++ ) {
			// Append a random character to the has string result
			hash += library.charAt ( rand.nextInt ( library.length () ) );
		}
		// Return the resulting hash string
		return hash;
	}

}
