package Client;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import Graphic.Button;
import Graphic.TextField;

/**
 * This class implements the action listener class to handle the send button click and it inherits
 * from a JPanel so that it can populate the chat area sub section of the char application.
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
public class ChatArea extends JPanel implements ActionListener {

	/**
	 * This data member saves the instance of the calling parent, and is used to communicate with
	 * other parts of the GUI components.
	 * @var     ChatApplication     parent          The calling parent class
	 */
	private ChatApplication parent;

	/**
	 * This data member saves the instance of the TextBox class that is used to input a message to
	 * be sent to the group.
	 * @var     TextField       textbox             The input message text box
	 */
	protected TextField textbox;

	/**
	 * This data member stores the instance of the send button that is used to send an inputed
	 * message to the server.
	 * @var     Button          send                The send message button
	 */
	protected Button send;

	/**
	 * This constructor initializes the components in this panel and attaches listeners to them.
	 * @param   ChatApplication     frame           The reference to the parent caller class
	 */
	public ChatArea ( ChatApplication frame ) {
		// Call the super constructor and don't use a manager
		super ( null );
		// Save the parent internally
		this.parent = frame;
		// Set the background color, dimensions, and position
		this.setBackground ( new Color ( 0xEBECEC ) );
		this.setBounds ( 0, 500, 700, 80 );
		// Create a text box and set it's properties
		textbox = new TextField ( "Send Message...", 555, 50, 15 );
		textbox.setPosition ( 15, 15 );
		textbox.setFont ( new Font ( "Muli", Font.PLAIN, 19 ) );
		// Create a send button and set it's properties
		send = new Button ( "Send", 100, 50 );
		send.setPosition ( 585, 15 );
		send.setFont ( new Font ( "Muli", Font.PLAIN, 19 ) );
		// Attach the event listener
		send.addActionListener ( this );
		textbox.addActionListener ( this );
		// Add elements to the panel
		this.add ( textbox );
		this.add ( send );
	}

	/**
	 * This function is a static function and it generates a timestamp containing the date and time.
	 * @return  String                              Returns a string representation of a timestamp
	 * @static
	 */
	protected static String timestamp () {
		// Initialize a date format using a string
		SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy - kk:mm:ss");
		// Format current time / date into specified string
		return dateFormat.format ( new Date () );
	}

	/**
	 * This function is here because this class implements an action listener class and it is used
	 * when the send button is clicked or when the enter key is pressed in the text box instance.
	 * @param   ActionEvent     event               The caught event from the Action Listener
	 * @return  void
	 */
	public void actionPerformed ( ActionEvent event ) {
		// Check to see that the action was on the send button, even though thats all we binded
		if ( event.getSource () == this.send || event.getSource () == this.textbox ) {
			String value = this.textbox.getText ();
			// Max 57 chars
			if ( !value.replaceAll ( "\\s+", "" ).equals ( "" ) ) {
				// Collect information
				String name = this.parent.messageArea.groups.getCurrentMessages ().group.getNames ();
				String hash = this.parent.messageArea.groups.getCurrentMessages ().group.getHash ();
				ArrayList <String> users = this.parent.messageArea.groups.getCurrentMessages ().group.getUsers ();
				// Create a JSON array of users
				JSONArray usersArray = new JSONArray ();
				// Populate the users JSON array
				for ( String user : users ) {
					// Add string to JSON array
					usersArray.add ( user );
				}
				// Create the message JSON string
				JSONObject json = new JSONObject ();
				json.put ( "type", "message" );
				json.put ( "name", name );
				json.put ( "hash", hash );
				json.put ( "users", usersArray );
				json.put ( "from", this.parent.username );
				json.put ( "timestamp", ChatArea.timestamp () );
				json.put ( "message", value.trim () );
				// Send the json message to the server
				this.parent.connection.send ( json.toString () );
				// Set the message text-box to be empty
				this.textbox.setText ("");
			}
			this.textbox.requestFocus ();
		}
	}

}