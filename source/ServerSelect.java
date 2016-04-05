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
public class ServerSelect extends Display implements ActionListener {

	private JLabel logo;

	private JLabel alert;

	private TextField ipAddress;

	private TextField port;

	private Button connect;

	/**
	 * 
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
		this.connect.setBackground ( new Color ( 0x6D6D6D ) );
		this.connect.setHighlight ( Color.WHITE, new Color ( 0x565656 ) );
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

	@Override
	public void actionPerformed ( ActionEvent event ) {
		// No need to check source, because all preform the same task
		System.out.println ( "Connecting" );
	}

}