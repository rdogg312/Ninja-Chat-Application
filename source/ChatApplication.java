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

@SuppressWarnings ( "serial" )
public class ChatApplication extends Display implements WindowListener {

	protected Connection connection;

	protected MessageArea messageArea;

	protected ChatArea chatArea;

	protected MenuArea menuArea;

	protected String username;

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

	@Override
	public void windowActivated ( WindowEvent event ) {}

	@Override
	public void windowClosed ( WindowEvent event ) {}

	@Override
	public void windowDeactivated ( WindowEvent event ) {}

	@Override
	public void windowDeiconified ( WindowEvent event ) {}

	@Override
	public void windowIconified ( WindowEvent event ) {}

	@Override
	public void windowOpened ( WindowEvent event ) {}
		
}