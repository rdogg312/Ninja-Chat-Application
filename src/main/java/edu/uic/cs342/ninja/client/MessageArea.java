package edu.uic.cs342.ninja.client;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.awt.Color;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.util.ArrayList;
import edu.uic.cs342.ninja.graphic.Button;
import edu.uic.cs342.ninja.graphic.TextField;
import edu.uic.cs342.ninja.graphic.ScrollPanel;

/**
 * This class initializes the GUI elements in the messages area.  This class also gets passed a JSON
 * array that is filled with the users groups and messages.  This is passed in login or create
 * account time.  This info is pulled from the server and is populated into the GUI elements.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @category    Project #04 - Ninja: Chat Application
 * @package     Client
 * @author      Rafael Grigorian
 * @author      Byambasuren Gansukh
 * @license     The MIT License, see LICENSE.md
 */
@SuppressWarnings ( "serial" )
public class MessageArea extends JPanel {

	/**
	 * This holds a reference to the parent calling char application.  Since this is an area in the
	 * whole GUI, it needs a reference to the parent to communicate with the rest of the GUI.
	 * @var     ChatApplication     parent          The parent calling instance
	 */
	private ChatApplication parent;

	/**
	 * This data member holds a reference to the information area in the messages are part of the
	 * GUI.  This area shows all the users in the currently selected group.
	 * @var     JLabel          information         The information area of the messages area
	 */
	protected JLabel information;

	/**
	 * This holds an instance to the groups panel that is scrollable.  This holds all group tabs.
	 * @var     Groups          groups              The groups scrollable panel
	 */
	protected Groups groups;

	/**
	 * This constructor initializes the GUI elements and loads all the messages that are passed in
	 * as a parameter.  This info is saved on the server and then send to the user on login.  All
	 * conversations and groups are populated on login or create time.
	 * @param   ChatApplication     frame           The calling parent instance
	 * @param   JSONArray           json            The json array with groups and messages
	 */
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
		// Loop though the JSON array that was passed
		for ( int i = 0; i < json.size (); i++ ) {
			// Each interval have it be casted as a JSONObject
			JSONObject group = ( JSONObject ) json.get ( i );
			// Initialize the Array lists
			ArrayList <String> users = new ArrayList <String> ();
			JSONArray usersList = ( JSONArray ) group.get ( "users" );
			// Loop though the array list of users and add then to the users array list
			for ( int u = 0; u < usersList.size (); u++ ) {
				users.add ( usersList.get ( u ).toString () );
			}
			// Create a new group based on findings
			Group newGroup = new Group (
				group.get ( "name" ).toString (),
				group.get ( "hash" ).toString (),
				users,
				true
			);
			// Get the array of messages for this group
			JSONArray messages = ( JSONArray ) group.get ( "messages" );
			// Loop though each message in the messages array
			for ( int j = 0; j < messages.size (); j++ ) {
				// Cast message as a JSONObject
				JSONObject message = ( JSONObject ) messages.get ( j );
				// Add the message to the made messages scroll panel
				newGroup.getMessages ().addMessage (
					message.get ( "message" ).toString (),
					message.get ( "from" ).toString (),
					message.get ( "timestamp" ).toString (),
					this.parent.username.equals ( message.get ( "from" ).toString () )
				);
			}
			// Append this group to the groups panel and groups array list
			this.groups.append ( newGroup );
		}
		// Set the current char room to the public "Everybody one"
		this.groups.setCurrentMessage ( "0" );
		// Append elements to messages panel
		this.add ( this.information );
		this.add ( this.groups.getCurrentMessages () );
		this.add ( this.groups );
	}

}
