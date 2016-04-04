import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.text.SimpleDateFormat;
import java.util.Date;
import Graphic.Button;
import Graphic.TextField;

@SuppressWarnings ( "serial" )
public class ChatArea extends JPanel implements ActionListener {

	private ChatApplication parent;

	protected TextField textbox;

	protected Button send;

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

	protected static String timestamp () {
		// Initialize a date format using a string
		SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy - kk:mm:ss");
		// Format current time / date into specified string
		return dateFormat.format ( new Date () );
	}

	public void actionPerformed ( ActionEvent event ) {
		// Check to see that the action was on the send button, even though thats all we binded
		if ( event.getSource () == this.send || event.getSource () == this.textbox ) {
			String value = this.textbox.getText ();
			// Max 57 chars
			if ( !value.equals ( "" ) ) {
				this.parent.messageArea.append ( "What's up!", "Tobin", ChatArea.timestamp (), false );
				this.parent.messageArea.append ( value, "NULL", ChatArea.timestamp (), true );
				this.textbox.setText ("");
			}
			this.textbox.requestFocus ();
		}
    }

}