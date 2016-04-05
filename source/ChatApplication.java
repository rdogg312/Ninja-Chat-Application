import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
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
public class ChatApplication extends Display {

	protected MessageArea messageArea;

	protected ChatArea chatArea;

	protected MenuArea menuArea;

	protected String username;

	public ChatApplication ( JSONObject json ) {
		// Run super constructor and set background color
		super ( "Ninja - Chat Application", 700, 600 );
		super.setDefaultCloseOperation ( JFrame.DO_NOTHING_ON_CLOSE );
		this.panel.setBackground ( Color.WHITE );
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
		// Attach a close window listener, to logout
		this.addWindowListener ( new WindowAdapter () {
			public void windowClosing ( WindowEvent e ){
				// Initialize dialog to ask user if they want to logout
				int a = JOptionPane.showConfirmDialog ( null, "Are you sure you want to logout?" );
				// Check if the user choose yes
				if ( a == 0 ) {
					// Send logout packet to server
					System.out.println ( "Logging out..." );
					// Exit the application
					System.exit ( 0 );
				}
			}
		});
	}

}