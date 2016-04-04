import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Graphic.Display;
import Graphic.TextField;
import Graphic.Button;

@SuppressWarnings ( "serial" )
public class ChatApplication extends Display {

	protected MessageArea messageArea;

	protected ChatArea chatArea;

	protected MenuArea menuArea;

	public ChatApplication () {
		// Run super constructor and set background color
		super ( "Ninja - Chat Application", 700, 600 );
		this.panel.setBackground ( Color.WHITE );
		// Create all the panels
		this.messageArea = new MessageArea ( this );
		this.chatArea = new ChatArea ( this );
		this.menuArea = new MenuArea ( this );
		// Add all the panels to the main panel
		this.panel.add ( this.chatArea );
		this.panel.add ( this.menuArea );
		this.panel.add ( this.messageArea );
		// Render the frame and request focus to the message text box
		super.render ();
		this.chatArea.textbox.requestFocus ();
	}

}