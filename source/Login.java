import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Graphic.Display;
import Graphic.TextField;
import Graphic.PasswordField;
import Graphic.Button;

/**
 * 
 */
@SuppressWarnings ( "serial" )
public class Login extends Display implements ActionListener {

	private JLabel logo;

	private JLabel alert;

	private TextField username;

	private PasswordField password;

	private Button login;

	private Button account;

	/**
	 * 
	 */
	public Login () {
		// Run the super constructor and color background
		super ( "Ninja - Account Login", 500, 500 );
		this.panel.setBackground ( new Color ( 0xF3F2F3 ) );
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
		account.addActionListener ( this );
		// Render the frame
		super.render ();
	}

	@Override
    public void actionPerformed ( ActionEvent event ) {
    	// Get the values of the username and password fields
    	String user = this.username.getText ();
    	String pass = this.password.getPasswordString ();
    	// Check to see which button was pressed
 		if ( event.getSource () == this.login ) {
	        // Fake json for testing
			String json = "{\"type\":\"login\",\"status\":\"success\",\"public_key\":\"SERVER_KEY\",\"username\":\"NULL\",\"users\":[{\"username\":\"NULL\",\"online\":true},{\"username\":\"BennyS\",\"online\":false},{\"username\":\"TheHolyBeast\",\"online\":true}],\"groups\":[{\"name\":\"Everybody\",\"hash\":\"0\",\"users\":[\"Everybody\"],\"messages\":[{\"from\":\"NULL\",\"timestamp\":\"04/04/2016 - 12:23:53\",\"message\":\"Hey what's up man!\"},{\"from\":\"TheHolyBeast\",\"timestamp\":\"04/04/2016 - 12:24:02\",\"message\":\"Hey!\"}]},{\"name\":\"CS342\",\"hash\":\"SFVG67RE6GVS8SHCA7SCGDHSKAFIUFDSHAOW\",\"users\":[\"NULL\",\"BennyS\"],\"messages\":[{\"from\":\"NULL\",\"timestamp\":\"04/04/2016 - 12:27:22\",\"message\":\"What up Ben!\"}]}]}";
			// Initialize json parser
			JSONParser parser = new JSONParser();
			// Try to parse the json string
			try {
				// Parse and cast as a JSON object
	        	JSONObject object = ( JSONObject ) parser.parse ( json );
	        	// Pass the json object to the chat application constructor
	            new ChatApplication ( object );
	        }
	        // Attempt to catch any parse exceptions
	        catch ( ParseException exception ) {
	     		// Print error and exit
	        	System.out.println ( exception.toString () );
	        	System.exit ( 0 );
	        }
	        // Close this window frame
	        this.setVisible ( false );
 			this.dispose ();
 		}
 		else if ( event.getSource() == this.account ) {
	    	// Create patterns for username and password
	    	Pattern userPattern = Pattern.compile ( "^[a-zA-Z0-9]*$" );
	    	Pattern passPattern = Pattern.compile ( "^[a-zA-Z0-9]*$" );
	    	// First check to see if a valid username was passed
	    	if ( userPattern.matcher ( user ).find () && user.length () > 2 && user.length () < 17 ) {
	    		// Next check to see if a valid password was passed
	    		if ( passPattern.matcher ( pass ).find () && pass.length () > 2 && pass.length () < 17 ) {
	    			// Finally we can create the account
	    			this.alert.setText ("Account successfully created!");
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