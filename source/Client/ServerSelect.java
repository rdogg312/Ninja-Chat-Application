package Client;

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
 * This class simply creates the GUI elements for the server selection.  Although the method that
 * was implemented from the ActionListener actually spawns a connection instance in another thread
 * that con tolls the outcome of this instance.  If the connection is valid it will destroy this
 * instance, otherwise it itself will die and send an error message to the alert area.
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
public class ServerSelect extends Display implements ActionListener {

	/**
	 * This data member is simply a reference to the log on the GUI frame
	 * @var     JLabel          logo                The logo on the GUI frame
	 */
	private JLabel logo;

	/**
	 * This data member is the alert area, where messages or errors are displayed to the user.
	 * @var     JLabel          alert               The alert area in GUI frame
	 */
	protected JLabel alert;

	/**
	 * This is the IP address text box in the GUI, it is saved for the Action Listener methods.
	 * @var     TextField       ipAddress           The IP Address text field
	 */
	protected TextField ipAddress;

	/**
	 * This is the port number text field in the GUI, it is saved for the Action Listener methods.
	 * @var     TextField       port                The port text field
	 */
	protected TextField port;

	/**
	 * This is the connect button on the GUI frame, it is saved for the Action Listener methods
	 * @var     Button          connect             The connect button in GUI
	 */
	protected Button connect;

	/**
	 * This constructor simply loads in the GUI and sets all the styling for all the different
	 * elements in the GUI.  It also initially sets focus on the button.
	 */
	public ServerSelect () {
		// Run the super constructor and color background
		super ( "Ninja - Server Connection", 500, 500 );
		this.panel.setBackground ( new Color ( 0xF3F2F3 ) );
		// Create the logo
		this.logo = new JLabel ( "" );
		this.logo.setIcon ( new ImageIcon ( "./assets/images/Logo.png" ) );
		this.logo.setBounds ( 180, 50, 140, 140 );
		// Create an alert box to display messages in
		this.alert = new JLabel (
			"<html>Please provide the chat server's IP address along with the corresponding port " +
			"number.  Once a successful connection is established, you will be prompted to login " +
			"to your account.</html>"
		);
		this.alert.setFont ( new Font ( "Muli", Font.PLAIN, 11 ) );
		this.alert.setForeground ( new Color ( 0x6D6D6E ) );
		this.alert.setBounds ( 50, 225, 400, 50 );
		// Create an IP address text field
		this.ipAddress = new TextField ( "IP Address", 400, 50, 15 );
		this.ipAddress.setFont ( new Font ( "Muli", Font.PLAIN, 20 ) );
		this.ipAddress.setPosition ( 50, 310 );
		// Create a port field
		this.port = new TextField ( "Port", 190, 50, 15 );
		this.port.setFont ( new Font ( "Muli", Font.PLAIN, 20 ) );
		this.port.setPosition ( 50, 380 );
		// Create a connect button
		this.connect = new Button ( "Connect", 190, 50 );
		this.connect.setFont ( new Font ( "Muli", Font.PLAIN, 19 ) );
		this.connect.setPosition ( 260, 380 );
		// Add all of the elements to the panel
		this.panel.add ( this.logo );
		this.panel.add ( this.alert );
		this.panel.add ( this.ipAddress );
		this.panel.add ( this.port );
		this.panel.add ( this.connect );
		// Bind buttons to the action listener
		connect.addActionListener ( this );
		ipAddress.addActionListener ( this );
		port.addActionListener ( this );
		// Render the frame and request focus on the button
		super.render ();
		this.connect.requestFocus ();
	}

	/**
	 * This method evaluates if the connection is good, spawning a connection instance on another
	 * thread and if the thread lives and validates the connection it will free this instance,
	 * otherwise it will set an error message in the alert area of this frame and commit suicide.
	 * That was the user can try to spawn another instance and try a difference connection if it
	 * fails the first time.
	 * @param   ActionEvent     event               The action event that was caught by listener
	 * @override
	 */
	@Override
	public void actionPerformed ( ActionEvent event ) {
		// Get the values of the text fields
		String ip = this.ipAddress.getText ().replaceAll ( "\\s+", "" ).trim ();
		String port = this.port.getText ().replaceAll ( "\\s+", "" ).trim ();
		// Before we do anything, check that the fields are not empty
		if ( ip.equals ( "" ) || port.equals ( "" ) ) {
			// If any are empty, return
			return;
		}
		// No need to check source, because all preform the same task
		try {
			// Create a new connection to the passed server
			Connection connection = new Connection ( this, ip, Integer.parseInt ( port ) );
			// Create a listener thread
			Thread thread = new Thread ( connection );
			thread.start ();
			// Send in login packet to try to see if it is a Ninja server
			connection.send (
				"{\"type\":\"login\",\"username\":\"\",\"password\":\"\",\"public_key\":\"\"}"
			);
		}
		catch ( Exception exception ) {
			// Change the background color of the alert text area
			this.alert.setForeground ( new Color ( 0xD94C36 ) );
			// Alert user of error
			this.alert.setText (
				"<html>The connection to (" + ip + "," + port + ") failed, this is probably becau" +
				"se you entered in the wrong information or the Ninja Server is not running.  Ple" +
				"ase check credentials and that the Ninja server is running, and try again.</html>"
			);
		}
	}

}