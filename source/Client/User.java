package Client;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.io.File;
import java.lang.StringBuilder;
import java.util.Scanner;
import javax.swing.JLabel;

/**
 * This class simply implements a mouse listener to check if it was checked.  It inherits a JLabel
 * because it sets the content of the JLabel to be HTML and based on the state and if the user is
 * online, we change and manipulate that HTML and re-render the component.  There are functions in
 * this class to trigger such events.
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
public class User extends JLabel implements MouseListener {

	/**
	 * This data member is the username of the user as a string.
	 * @var     String          username            The username of the user
	 */
	private String username;

	/**
	 * This data member is a boolean flag that says whether this user is online or not.
	 * @var     boolean         online              Is the user online?
	 */
	private boolean online;

	/**
	 * This data member is a boolean flag that says whether this user is selected in the GUI.
	 * @var     boolean         selected            Is this user selected in the GUI?
	 */
	private boolean selected;

	/**
	 * This is the color of the selected background of the user label.
	 * @var     Color           selectedColor       The selected background color of user label
	 */
	private Color selectedColor;

	/**
	 * This is the color of the unselected background of the user label.
	 * @var     Color           unSelectedColor     The unselected background color of user label
	 */
	private Color unSelectedColor;

	/**
	 * This is the color of the online notification badge in string form for the HTML.
	 * @var     String          onlineColor         The online color for HTML
	 */
	private String onlineColor;

	/**
	 * This is the color of the offline notification badge in string form for the HTML.
	 * @var     String          offlineColor        The offline color for HTML
	 */
	private String offlineColor;

	/**
	 * This is the html content pulled from the HTML template file that is loaded in the
	 * constructor.
	 * @var     String          html                The HTML content of template file for a user
	 */
	private String html;

	/**
	 * The constructor simply initializes the GUi elements and loads in the state of the user label
	 * based on the online boolean.
	 * @var     String          username            The username of the user
	 * @var     boolean         online              Is the user online?
	 */
	public User ( String username, boolean online ) {
		// Call the super constructor
		super ( "" );
		// Define the default options for colors
		this.unSelectedColor = new Color ( 0xF3F2F3 );
		this.selectedColor = new Color ( 0xD3D3D3 );
		this.offlineColor = "#6D6D6D";
		this.onlineColor = "#B0D35D";
		// Load the template internally
		this.html = this.loadHTML ( "./assets/templates/user.tpl" );
		// Set the username to the template
		this.html = this.html.replace ( "_USERNAME_", username );
		// Set the cursor to be the hand
		super.setCursor ( new Cursor ( Cursor.HAND_CURSOR ) );
		// Save the passed data and render tab
		this.username = username;
		this.setOnline ( online );
		this.selected = false;
		// Set super options
		super.setOpaque ( true );
		super.setBackground ( this.unSelectedColor );
		super.setForeground ( new Color ( 0x6D6D6D ) );
		// Add itself to the event listener
		this.addMouseListener ( this );
	}

	/**
	 * This function sets the state of the user to be online and renders the label based on that
	 * passed boolean value.
	 * @var     boolean         online              Should we mark the user as being online?
	 * @return  void
	 */
	protected void setOnline ( boolean online ) {
		// Set the online marker to be whatever was passed
		this.online = online;
		// If they are online
		if ( online ) {
			// Load the HTML template and change the color to be online
			String temp = this.html.replace ( "_ONLINE_", this.onlineColor );
			this.setText ( temp );
		}
		// Otherwise they are offline
		else {
			// Load the HTML template and change color to be offline
			String temp = this.html.replace ( "_ONLINE_", this.offlineColor );
			this.setText ( temp );
		}
	}

	/**
	 * This function selects the user based on the passes flag.  It also reflects that choice by
	 * rendering out the selection in the GUI.
	 * @param   boolean         state               Should we make the user as selected?
	 * @return  void
	 */
	protected void setSelected ( boolean state ) {
		// If the selected passed state is true
		if ( state ) {
			// Set the background and save the state
			this.setBackground ( this.selectedColor );
			this.selected = true;
		}
		// Otherwise, it is not selected
		else {
			// Save the state and set the background to be not selected
			this.setBackground ( this.unSelectedColor );
			this.selected = false;
		}
	}

	/**
	 * This is a simple getter function that returns the username of the user.
	 * @return  String                              Return the username of user
	 */
	protected String getUsername () {
		// Return the username of the user
		return this.username;
	}

	/**
	 * This is a simple getter function and it returns a boolean that says if the user is selected.
	 * @return  boolean                             Is the user selected?
	 */
	protected boolean getSelected () {
		// Return whether the user is selected or not
		return this.selected;
	}

	/**
	 * This function takes in a filepath and loads the contents.  the contents of this file should
	 * be an HTML template for the user tab.  It is then saved internally so we could replace the
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
	 * This function selects and deselects based on the previous state of the user label.  It also
	 * reflects this in the GUI.
	 * @param   MouseEvent      event               The mouse event that is triggered by listener
	 * @return  void
	 */
	public void mouseClicked ( MouseEvent event ) {
		// Since this listener is only for this label, we don't have to check the source
		if ( this.selected ) {
			// Mark and render as unselected
			this.setBackground ( this.unSelectedColor );
			this.selected = false;
		}
		// Otherwise its not selected
		else {
			// Mark and render as selected
			this.setBackground ( this.selectedColor );
			this.selected = true;
		}
	}

	/**
	 * This is a required function that is required to implement the mouse listener, but has no
	 * purpose in this application.
	 * @param   MouseEvent      event               The mouse event that occurred
	 * @return  void
	 */
	public void mousePressed ( MouseEvent event ) {}

	/**
	 * This is a required function that is required to implement the mouse listener, but has no
	 * purpose in this application.
	 * @param   MouseEvent      event               The mouse event that occurred
	 * @return  void
	 */
	public void mouseReleased ( MouseEvent event ) {}

	/**
	 * This is a required function that is required to implement the mouse listener, but has no
	 * purpose in this application.
	 * @param   MouseEvent      event               The mouse event that occurred
	 * @return  void
	 */
	public void mouseEntered ( MouseEvent event ) {}

	/**
	 * This is a required function that is required to implement the mouse listener, but has no
	 * purpose in this application.
	 * @param   MouseEvent      event               The mouse event that occurred
	 * @return  void
	 */
	public void mouseExited ( MouseEvent event ) {}


}