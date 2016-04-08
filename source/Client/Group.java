package Client;

import java.awt.Cursor;
import java.awt.Color;
import java.io.File;
import javax.swing.JLabel;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

/**
 * 
 *
 *
 *
 * 
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @category    Project #04 - Ninja: Chat Application
 * @package     Client
 * @author      Rafael Grigorian
 * @author      Byambasuren Gansukh
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
@SuppressWarnings ( "serial" )
public class Group extends JLabel {

	/**
	 * 
	 */
	private String html;

	/**
	 * 
	 */
	private String hash;

	/**
	 * 
	 */
	private String groupName;

	/**
	 * 
	 */
	private Messages messages;

	/**
	 * 
	 */
	protected ArrayList <String> users;

	/**
	 * 
	 */
	private boolean read;

	/**
	 * 
	 */
	private boolean selected;

	/**
	 * 
	 */
	private Color selectedColor;

	/**
	 * 
	 */
	private Color unSelectedColor;

	/**
	 * 
	 */
	private String readBackground;

	/**
	 * 
	 */
	private String unreadBackground;

	/**
	 * 
	 */
	public Group ( String name, String hash, ArrayList <String> users, boolean read ) {
		// Run the super constructor
		super ( "" );
		// Save all the passed data and set initial values
		this.groupName = name;
		this.hash = hash;
		this.readBackground = "url(file:assets/images/No-New-Message.png)";
		this.unreadBackground = "url(file:assets/images/New-Message.png)";
		this.selectedColor = new Color ( 0xF3F2F3 );
		this.unSelectedColor = new Color ( 0xD3D3D3 );
		this.selected = false;
		this.html = this.loadHTML ( "./assets/templates/group.tpl" );
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
	 * 
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
	 * 
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
	 * 
	 */
	protected String getHash () {
		// Return the group hash
		return this.hash;
	}

	/**
	 * 
	 */
	protected Messages getMessages () {
		// Return the message area instance
		return this.messages;
	}

	/**
	 * 
	 */
	protected String getNames () {
		// Return the group name
		return this.groupName;
	}

	/**
	 * 
	 */
	protected ArrayList <String> getUsers () {
		// Return the string array list of users in the group
		return this.users;
	}

	/**
	 * 
	 */
	private String loadHTML ( String filepath ) {
		// Initialize contents variable
		String contents = "";
		// Try to open and read the target file
		try {
			// Create a scanner instance and open the target file
			Scanner scanner = new Scanner ( new File ( filepath ) );
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
	 *
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