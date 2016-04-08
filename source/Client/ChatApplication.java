package Client;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Paths;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import Graphic.Display;
import Graphic.TextField;
import Graphic.Button;

/**
 * This class implements the window listener and it is used to logout of the account and verify a
 * window close.  This class initializes all the sub panels of this chat application.
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
public class ChatApplication extends Display implements WindowListener {

	/**
	 * This data member saves the instance of the Connection object that is running on a separate
	 * thread in the background trying to receive information from the server.
	 * @var     Connection      connection          The connection instance opened on server select
	 */
	protected Connection connection;

	/**
	 * This data member saves an instance of the created message area panel.  This panel contains
	 * the group tabs, the and the actual message area along with the group users information area.
	 * @var     MessageArea     messageArea         The message area panel
	 */
	protected MessageArea messageArea;

	/**
	 * This data member saves an instance of the created chat area panel.  This panel contains the
	 * text field for string input and the send button.
	 * @var     CharArea        chatArea            The chat area panel
	 */
	protected ChatArea chatArea;

	/**
	 * This data member saves an instance of the created menu area panel.  This panel contains the
	 * users menu with a search box and the create chat room button.
	 * @var     MenuArea        menuArea            The menu area panel
	 */
	protected MenuArea menuArea;

	/**
	 * This data member saves the username of the logged in user.  It is extracted from the
	 * initially passed JSON object.
	 * @var     String          username            The username of the logged in user
	 */
	protected String username;

	/**
	 * This constructor saves the the passed parameters and it initializes all the areas of the GUI.
	 * It then plays an opening sound clip.
	 * @param   Connection      connection          The open socket connection to the server
	 * @param   JSONObject      json                The initial login json string from server
	 */
	public ChatApplication ( Connection connection, JSONObject json ) {
		// Run super constructor and set background color
		super ( "Ninja - Chat Application", 700, 600 );
		super.setDefaultCloseOperation ( JFrame.DO_NOTHING_ON_CLOSE );
		this.panel.setBackground ( Color.WHITE );
		// Save the connection thread reference
		this.connection = connection;
		// Save the users username
		this.username = json.get ( "username" ).toString ();
		// Create all the panels
		this.messageArea = new MessageArea ( this, ( JSONArray ) json.get ( "groups" ) );
		this.chatArea = new ChatArea ( this );
		this.menuArea = new MenuArea ( this, ( JSONArray ) json.get ( "users" ) );
		// Add all the panels to the main panel
		this.panel.add ( this.chatArea );
		this.panel.add ( this.menuArea );
		this.panel.add ( this.messageArea );
		// Render the frame and request focus to the message text box
		super.setIconImage ( Toolkit.getDefaultToolkit ().getImage ( Paths.get("assets/images/Logo.png").toUri().toString() ) );
		super.render ();
		this.chatArea.textbox.requestFocus ();
		// Add the window listener
		this.addWindowListener ( this );
		// Play opening sound effect
		try {
			// Load in the audio file as a stream and play it
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream (
					this.getClass ().getResource ( "./assets/audio/Opening.wav" )
			);
			Clip clip = AudioSystem.getClip ();
			clip.open ( audioInputStream );
			clip.start ();
		}
		// Catch any exception, if caught, we want to ignore this error
		catch ( Exception exception ) {}
	}

	/**
	 * This function runs whenever the close button on the window is closed.  It asks if the user
	 * really wants to logout, and if so, it sends a packet to the server letting it know about the
	 * logout, and then it closes the window.
	 * @param   WindowEvent     event               The caught window event
	 * @return  void
	 * @override
	 */
	@Override
	public void windowClosing ( WindowEvent event ) {
		// Initialize dialog to ask user if they want to logout
		int a = JOptionPane.showConfirmDialog ( null, "Are you sure you want to logout?" );
		// Check if the user choose yes
		if ( a == 0 ) {
			// Construct logout packet
			JSONObject json = new JSONObject ();
			json.put ( "type", "logout" );
			json.put ( "username", this.username );
			// Send logout packet to server
			this.connection.send ( json.toString () );
			// Exit the application
			System.exit ( 0 );
		}
	}

	/**
	 * This function needs to be here in order to implement the WindowListener class.  It is not
	 * used in this class, but it is required to be here.
	 * @param   WindowEvent     event               The caught window event
	 * @return  void
	 * @override
	 */
	@Override
	public void windowActivated ( WindowEvent event ) {}

	/**
	 * This function needs to be here in order to implement the WindowListener class.  It is not
	 * used in this class, but it is required to be here.
	 * @param   WindowEvent     event               The caught window event
	 * @return  void
	 * @override
	 */
	@Override
	public void windowClosed ( WindowEvent event ) {}

	/**
	 * This function needs to be here in order to implement the WindowListener class.  It is not
	 * used in this class, but it is required to be here.
	 * @param   WindowEvent     event               The caught window event
	 * @return  void
	 * @override
	 */
	@Override
	public void windowDeactivated ( WindowEvent event ) {}

	/**
	 * This function needs to be here in order to implement the WindowListener class.  It is not
	 * used in this class, but it is required to be here.
	 * @param   WindowEvent     event               The caught window event
	 * @return  void
	 * @override
	 */
	@Override
	public void windowDeiconified ( WindowEvent event ) {}

	/**
	 * This function needs to be here in order to implement the WindowListener class.  It is not
	 * used in this class, but it is required to be here.
	 * @param   WindowEvent     event               The caught window event
	 * @return  void
	 * @override
	 */
	@Override
	public void windowIconified ( WindowEvent event ) {}

	/**
	 * This function needs to be here in order to implement the WindowListener class.  It is not
	 * used in this class, but it is required to be here.
	 * @param   WindowEvent     event               The caught window event
	 * @return  void
	 * @override
	 */
	@Override
	public void windowOpened ( WindowEvent event ) {}

}