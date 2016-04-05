import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.nio.file.Paths;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Graphic.Display;
import Graphic.TextField;
import Graphic.Button;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

@SuppressWarnings ( "serial" )
public class ChatApplication extends Display {

	protected MessageArea messageArea;

	protected ChatArea chatArea;

	protected MenuArea menuArea;

	protected String username;

	public ChatApplication ( JSONObject json ) {
		// Run super constructor and set background color
		super ( "Ninja - Chat Application", 700, 600 );
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
		    AudioInputStream audioInputStream =
		        AudioSystem.getAudioInputStream(
		            this.getClass().getResource("./assets/audio/Opening.wav"));
		    Clip clip = AudioSystem.getClip();
		    clip.open(audioInputStream);
		    clip.start();
		}
		catch ( Exception exception ) {
			exception.printStackTrace();
		}
	}

}