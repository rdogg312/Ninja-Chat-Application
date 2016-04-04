import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Dimension;
import java.io.File;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.awt.Adjustable;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileReader;
import java.lang.StringBuilder;

import Graphic.Button;
import Graphic.TextField;
import Graphic.ScrollPanel;

@SuppressWarnings ( "serial" )
public class MessageArea extends JPanel {

	private ChatApplication parent;

	private JLabel information;

	protected Groups groups;

	public MessageArea ( ChatApplication frame ) {
		// Call the super constructor and don't use a manager
		super ( null );
		// Save the parent internally
		this.parent = frame;
		// Set the background color, dimensions, and position
		this.setBackground ( Color.WHITE );
		this.setBounds ( 200, 0, 580, 540 );
		// Create a group information area and set properties
		this.information = new JLabel ( "Users: Everybody" );
		this.information.setOpaque ( true );
		this.information.setForeground ( new Color ( 0xBABABA ) );
		this.information.setBackground ( new Color ( 0xF3F2F3 ) );
		this.information.setBorder ( BorderFactory.createCompoundBorder (
			null, BorderFactory.createEmptyBorder ( 15, 15, 15, 15 )
		));
		this.information.setBounds ( 0, 40, 500, 40 );
		// Create the group tabs area and set properties
		this.groups = new Groups ( this, 500, 40, BoxLayout.X_AXIS );
		this.groups.setPosition ( 0, 0 );
		this.groups.getContentPanel ().setBackground ( new Color ( 0xF3F2F3 ) );


		for ( int i = 0; i < 3; i++ ) {
			this.groups.append ( new Group ( "Group #" + i, "000" + i, false ) );
		}

		this.groups.setCurrentMessage ( "0000" );

		// Append elements to messages panel
		this.add ( this.information );
		this.add ( this.groups.getCurrentMessages () );
		this.add ( this.groups );
	}

	protected void append ( String text, String username, String timestamp, boolean self ) {
		if ( this.groups.getCurrentMessages () == null ) {
			return;
		}
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

		String content = "";

		try {
			Scanner scanner = new Scanner ( new File ( "./assets/templates/message.tpl" ) );
			content  = scanner.useDelimiter("\\A").next();
			scanner.close();
		}
		catch ( Exception ex ) {
			System.exit ( 0 );
		}

		text = text.replace ( "<", "&lt;" );
		text = text.replace ( ">", "&gt;" );
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
		this.groups.getCurrentMessages ().append ( message );
		this.groups.getCurrentMessages ().scrollToBottom ();
	}

}