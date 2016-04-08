package Client;

import java.awt.Color;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.util.Scanner;
import Graphic.ScrollPanel;

@SuppressWarnings ( "serial" )
public class Messages extends ScrollPanel {

	protected String html;

	protected Group group;
 
	public Messages ( Group parent ) {
		// Create the message panel that is scrollable and set the default properties
        super ( 500, 420, BoxLayout.Y_AXIS );
        super.setPosition ( 0, 80 );
        super.getContentPanel ().setBorder (
        	BorderFactory.createEmptyBorder ( 0, 0, 15, 0 )
        );
        this.group = parent;
	}
 
	private String loadHTML ( String filepath ) {
		// Initialize contents variable
		String contents = "";
		// Try to open and read the target file
		try {
			// Create a scanner instance and open the target file
			Scanner scanner = new Scanner ( new File ( filepath ) );
			// Read in the whole file into string
			contents = scanner.useDelimiter ( "\\A" ).next ();
			// Close the file descriptor
			scanner.close ();
		}
		// Attempt to catch any exception
		catch ( Exception exception ) {
			// If there is an error, then return empty string
			return "";
		}
		// Return the contents of the file
		return contents;
	}

	public void addMessage ( String text, String username, String timestamp, boolean self ) {
		// Set the default values
		int labelFloat = JLabel.LEFT;
		String backgroundColor = "#6D6D6D";
		String fontColor = "#FFFFFF";
		String textAlign = "left";
		// Check if the flag was raised to change the float side and style properties
		if ( self ) {
			// Change then so they reflected the style of the user
			labelFloat = JLabel.RIGHT;
			backgroundColor = "#F3F2F3";
			fontColor = "#6D6D6D";
			textAlign = "right";
		}
		// replace HTML entities and load into HTML template
		text = text.replace ( "<", "&lt;" );
		text = text.replace ( ">", "&gt;" );
		String content = this.loadHTML ( "./assets/templates/message.tpl" );
		content = content.replace ( "_ALIGN_", textAlign );
		content = content.replace ( "_COLOR_", fontColor );
		content = content.replace ( "_BACKGROUND_", backgroundColor );
		content = content.replace ( "_CONTENT_", text );
		content = content.replace ( "_INFORMATION_", username + " - " + timestamp );
		// Create the message label using HTML
		JLabel message = new JLabel ( content );
	    message.setOpaque ( true );
	   	message.setBackground ( Color.WHITE );
	   	message.setForeground ( Color.WHITE );
		message.setBorder ( BorderFactory.createEmptyBorder ( 15, 15, 0, 15 ) );
		message.setHorizontalAlignment ( labelFloat );
		this.append ( message );
		this.scrollToBottom ();
	}

}