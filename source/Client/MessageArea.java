package Client;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

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
import java.util.ArrayList;

import Graphic.Button;
import Graphic.TextField;
import Graphic.ScrollPanel;

@SuppressWarnings ( "serial" )
public class MessageArea extends JPanel {

	private ChatApplication parent;

	protected JLabel information;

	protected Groups groups;

	public MessageArea ( ChatApplication frame, JSONArray json ) {
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


		for ( int i = 0; i < json.size (); i++ ) {
			JSONObject group = ( JSONObject ) json.get ( i );
			ArrayList <String> users = new ArrayList <String> ();
			JSONArray usersList = ( JSONArray ) group.get ( "users" );
			for ( int u = 0; u < usersList.size (); u++ ) {
				users.add ( usersList.get ( u ).toString () );
			}
			Group newGroup = new Group (
				group.get ( "name" ).toString (),
				group.get ( "hash" ).toString (),
				users,
				true
			);
			JSONArray messages = ( JSONArray ) group.get ( "messages" );
			for ( int j = 0; j < messages.size (); j++ ) {
				JSONObject message = ( JSONObject ) messages.get ( j );
				newGroup.getMessages ().addMessage (
					message.get ( "message" ).toString (),
					message.get ( "from" ).toString (),
					message.get ( "timestamp" ).toString (),
					this.parent.username.equals ( message.get ( "from" ).toString () )
				);
			}
			this.groups.append ( newGroup );
		}

		this.groups.setCurrentMessage ( "0" );

		// Append elements to messages panel
		this.add ( this.information );
		this.add ( this.groups.getCurrentMessages () );
		this.add ( this.groups );
	}

}