package Client;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import Graphic.Display;
import Graphic.TextField;
import Graphic.PasswordField;
import Graphic.Button;

/**
 * This class simply displays the login window for the user to log into their account.  It also uses
 * the Connection instance that is initialized and run on a separate thread for incoming packets
 * that are sent to the server.
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
public class Login extends Display implements ActionListener {

	/**
	 * This data member stores the connection to the thread that was spawned by the server select,
	 * and it is responsible for receiving and handling all the incoming message packets from the
	 * server.
	 * @var     Connection          connection      The connection instance handle
	 */
	private Connection connection;

	/**
	 * This data member holds an instance to the logo JLabel of the window.
	 * @var     JLabel              logo            The logo that is displayed
	 */
	private JLabel logo;

	/**
	 * This data member holds the alert message JLabel that displays information about the previous
	 * operation.
	 * @var     JLabel              alert           The alert message center
	 */
	protected JLabel alert;

	/**
	 * This data member is the saved instance of the username text field and it is saves because it
	 * is linked to an Action Listener that this class implements.
	 * @var     TextField           username        The desired username textbox
	 */
	protected TextField username;

	/**
	 * This data member is the saved instance of the password text field and it is saves because it
	 * is linked to an Action Listener that this class implements.
	 * @var     PasswordField       password        The desired password textbox
	 */
	protected PasswordField password;

	/**
	 * This is the login button and it is also used in the Action Listener to submit a login request
	 * to the server.
	 * @var     Button              login           The login button
	 */
	protected Button login;

	/**
	 * This is the create account button and it is also used in the Action Listener to submit a
	 * create account request to the server.
	 * @var     Button              account         The create account button
	 */
	protected Button account;

	/**
	 * This constructor initializes all the GUI elements onto the JFrame and it attaches an Action
	 * Listener to the appropriate components.
	 * @param   Connection          connection      The created connection on separate thread
	 */
	public Login ( Connection connection ) {
		// Run the super constructor and color background
		super ( "Ninja - Account Login", 500, 500 );
		this.panel.setBackground ( new Color ( 0xF3F2F3 ) );
		// Save the connection handle
		this.connection = connection;
		// Create the logo
		this.logo = new JLabel ( "" );
		this.logo.setIcon ( new ImageIcon ( "./assets/images/Logo.png" ) );
		this.logo.setBounds ( 180, 50, 140, 140 );
		// Create an alert box to display messages in
		this.alert = new JLabel ("");
		this.alert.setFont ( new Font ( "Muli", Font.PLAIN, 11 ) );
		this.alert.setForeground ( new Color ( 0xD94C36 ) );
		this.alert.setBounds ( 50, 205, 400, 20 );
		// Create a username text field
		this.username = new TextField ( "Username", 400, 50, 15 );
		this.username.setFont ( new Font ( "Muli", Font.PLAIN, 20 ) );
		this.username.setPosition ( 50, 240 );
		// Create a password field
		this.password = new PasswordField ( "Password", 400, 50, 15 );
		this.password.setFont ( new Font ( "Muli", Font.PLAIN, 20 ) );
		this.password.setPosition ( 50, 310 );
		// Create a login button
		this.login = new Button ( "Login", 190, 50 );
		this.login.setBackground ( new Color ( 0xD94C36 ) );
		this.login.setFont ( new Font ( "Muli", Font.PLAIN, 19 ) );
		this.login.setPosition ( 50, 380 );
		// Create a create account button
		this.account = new Button ( "Create Account", 190, 50 );
		this.account.setBackground ( new Color ( 0x6D6D6D ) );
		this.account.setHighlight ( Color.WHITE, new Color ( 0x565656 ) );
		this.account.setFont ( new Font ( "Muli", Font.PLAIN, 19 ) );
		this.account.setPosition ( 260, 380 );
		// Add all of the elements to the panel
		this.panel.add ( this.logo );
		this.panel.add ( this.alert );
		this.panel.add ( this.login );
		this.panel.add ( this.account );
		this.panel.add ( this.username );
		this.panel.add ( this.password );
		// Bind buttons to the action listener
		login.addActionListener ( this );
		password.addActionListener ( this );
		account.addActionListener ( this );
		// Render the frame
		super.render ();
	}

	/**
	 * This function deals with the action listener and handles the request.  If the buttons are
	 * clicked of whether the enter button was clicked within the password field.
	 * @param   ActionEvent     event               The caught action event
	 * @return  void
	 * @override
	 */
	@Override
	public void actionPerformed ( ActionEvent event ) {
		// Get the values of the username and password fields
		String user = this.username.getText ();
		String pass = this.password.getPasswordString ();
		// Check to see which button was pressed
		if ( event.getSource () == this.login || event.getSource () == this.password ) {
			// Disable the GUI components
			this.username.setEnabled ( false );
			this.password.setEnabled ( false );
			this.account.setEnabled ( false );
			this.login.setEnabled ( false );
			// Create a login JSON object
			JSONObject json = new JSONObject ();
			json.put ( "type", "login" );
			json.put ( "username", user );
			json.put ( "password", pass );
			json.put ( "public_key", "" );
			// Send it to the server
			this.connection.send ( json.toString () );
		}
		else if ( event.getSource() == this.account ) {
			// Create patterns for username and password
			Pattern userPattern = Pattern.compile ( "^[a-zA-Z0-9]*$" );
			Pattern passPattern = Pattern.compile ( "^[a-zA-Z0-9]*$" );
			// First check to see if a valid username was passed
			if ( userPattern.matcher ( user ).find () && user.length () > 2 && user.length () < 17 ) {
				// Next check to see if a valid password was passed
				if ( passPattern.matcher ( pass ).find () && pass.length () > 2 && pass.length () < 17 ) {
					// Finally we can create the account, disable the GUI components
					this.username.setEnabled ( false );
					this.password.setEnabled ( false );
					this.account.setEnabled ( false );
					this.login.setEnabled ( false );
					// Create a login JSON object
					JSONObject json = new JSONObject ();
					json.put ( "type", "create" );
					json.put ( "username", user );
					json.put ( "password", pass );
					json.put ( "public_key", "" );
					// Send it to the server
					this.connection.send ( json.toString () );
					// Return without displaying errors
					return;
				}
				// Display an alert to the user
				this.alert.setText ("Passwords are alphanumeric and must be in range [3,16]");
				// Return so we don't print extra errors
				return;
			}
			// Display an error message to the user
			this.alert.setText ("Usernames are alphanumeric and must be in range [3,16]");
		}
	}

}