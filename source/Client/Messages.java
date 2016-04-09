package Client;

import java.awt.Color;
import java.io.File;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import Graphic.ScrollPanel;

/**
 * This class simply initializes a scrollable panel that can be populated with messages.  A function
 * to do so is also included.
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
public class Messages extends ScrollPanel {

	/**
	 * This is the HTML string that is loaded for the messages template for the JLabel
	 * @var     String          html                The HTML template
	 */
	protected String html;

	/**
	 * This data member simply holds a link to the group that this message object belongs to
	 * @var     Group           group               The group this message is binded to
	 */
	protected Group group;

	/**
	 * This constructor simply calls the super constructor and saves the instance of the caller to
	 * be the binded group that this instance belongs to.
	 * @param   Group           parent              The calling group that this is binded to
	 */
	public Messages ( Group parent ) {
		// Create the message panel that is scrollable and set the default properties
		super ( 500, 420, BoxLayout.Y_AXIS );
		super.setPosition ( 0, 80 );
		super.getContentPanel ().setBorder (
			BorderFactory.createEmptyBorder ( 0, 0, 15, 0 )
		);
		// Save the caller to be the group that this instance is binded to
		this.group = parent;
	}

	/**
	 * This function simply loads the contents of the passed file and stores it internally.  The
	 * contents of this file should be an HTML template.  It will be used to create a message and
	 * feed the resulting HTML into a JLabel.
	 * @param   String          filepath            The filepath to the HTML file
	 * @return  String                              The contents of said HTML file
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
	 * This method simply appends a new message to the message area, or itself, and based on the
	 * passed self flag if renders it out to be either from you or from someone else and styles
	 * accordingly.
	 * @param   String          text                The content of the message
	 * @param   String          username            The username of the sender
	 * @param   String          timestamp           The timestamp of the message
	 * @param   boolean         self                Was the message sent by you?
	 * @return  void
	 */
	public void addMessage ( String text, String username, String timestamp, boolean self ) {
		// Set the default values
		int labelFloat = JLabel.LEFT;
		String backgroundColor = "#6D6D6D";
		String fontColor = "#FFFFFF";
		String textAlign = "left";
		// Check if the flag was raised to change the float side and style properties
		if ( self ) {
			// Change then so they reflected the style of the user
			labelFloat = JLabel.RIGHT;
			backgroundColor = "#F3F2F3";
			fontColor = "#6D6D6D";
			textAlign = "right";
		}
		// Replace HTML entities and load into HTML template
		text = text.replace ( "<", "&lt;" );
		text = text.replace ( ">", "&gt;" );
		String content = this.loadHTML ( "./assets/templates/message.tpl" );
		content = content.replace ( "_ALIGN_", textAlign );
		content = content.replace ( "_COLOR_", fontColor );
		content = content.replace ( "_BACKGROUND_", backgroundColor );
		content = content.replace ( "_CONTENT_", text );
		content = content.replace ( "_INFORMATION_", username + " - " + timestamp );
		// Create the message label using HTML
		JLabel message = new JLabel ( content );
		message.setOpaque ( true );
		message.setBackground ( Color.WHITE );
		message.setForeground ( Color.WHITE );
		message.setBorder ( BorderFactory.createEmptyBorder ( 15, 15, 0, 15 ) );
		message.setHorizontalAlignment ( labelFloat );
		this.append ( message );
		this.scrollToBottom ();
	}

}