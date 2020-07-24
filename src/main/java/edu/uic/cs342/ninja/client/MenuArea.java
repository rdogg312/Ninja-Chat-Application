package edu.uic.cs342.ninja.client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import edu.uic.cs342.ninja.graphic.Button;
import edu.uic.cs342.ninja.graphic.TextField;
import edu.uic.cs342.ninja.graphic.ScrollPanel;

/**
 * This class initializes the menu area and populates the users based on the JSON array that is
 * passed to the constructor.  It also handler ands listens to the create a chartroom button and the
 * search feature is implemented all here.
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
public class MenuArea extends JPanel implements ActionListener {

	/**
	 * This holds the instance of the calling class.  It is stored internally in order to
	 * communicate with the other GUI elements in other areas of the application.
	 * var      ChatApplication     parent          The parent calling instance
	 */
	private ChatApplication parent;

	/**
	 * This is a reference to the search box on the top left of the menu area.
	 * @var     TextField       search              This is the search text box
	 */
	private TextField search;

	/**
	 * This data member is a reference to the scrolling panel of users on the side menu.
	 * @var     Users           users               The users scrolling panel
	 */
	protected Users users;

	/**
	 * This is the create a chat room button that is clicked when users are selected.
	 * @var     Button          create              The create a chat room button
	 */
	private Button create;

	/**
	 * This constructor initializes all the elements of the GUI in the side menu, and it populates
	 * all the users based on the JSON array that is passed as a parameter.
	 * @var     ChatApplication     frame           The parent caller instance
	 * @var     JSONArray           json            The JSON array with login data
	 */
	public MenuArea ( ChatApplication frame, JSONArray json ) {
		// Call the super constructor and don't use a manager
		super ( null );
		// Save the parent internally
		this.parent = frame;
		// Set the background color, dimensions, and position
		this.setBackground ( new Color ( 0xF3F2F3 ) );
		this.setBounds ( 0, 0, 200, 520 );
		// Create the search panel and set properties
		JPanel searchPanel = new JPanel ( null );
		searchPanel.setBackground ( new Color ( 0xEBECEC ) );
		searchPanel.setBounds ( 0, 0, 200, 80 );
		// Create the search box and set properties
		this.search = new TextField ( "Search...", 170, 50, 15 );
		this.search.setPosition ( 15, 15 );
		this.search.setFont ( new Font ( "Muli", Font.PLAIN, 19 ) );
		// Create the users panel
		this.users = new Users ( 200, 370 );
		this.users.setPosition ( 0, 80 );
		// Loop though every element in the JSON array
		for ( int i = 0; i < json.size (); i++ ) {
			// Cast a JSON object from every entry in the array
			JSONObject user = ( JSONObject ) json.get ( i );
			// Extract the username from that object
			String username = user.get ( "username" ).toString ();
			// Check if the username matches the logged in username
			if ( !username.equals ( this.parent.username ) ) {
				// If it doesn't then append that user to the users menu
				users.append ( new User (
					username,
					Boolean.parseBoolean ( user.get ( "online" ).toString () )
				));
			}
		}
		// Create and add the create char room button
		this.create = new Button ( "Create Chat Room", 200, 50 );
		this.create.setPosition ( 0, 450 );
		this.create.setBackground ( new Color ( 0x6D6D6D ) );
		this.create.setHighlight ( Color.WHITE, new Color ( 0x565656 ) );
		// Attach event listeners
		this.create.addActionListener ( this );
		this.search.addActionListener ( this );
		// Add elements appropriately
		searchPanel.add ( this.search );
		this.add ( searchPanel );
		this.add ( this.users );
		this.add ( this.create );
	}

	/**
	 * This function creates a new group and displays it in the groups scroll panel if a duplicate
	 * does not exist already with the same users.  If a group already exists it will bring that tab
	 * up in the foreground.
	 * @return  void
	 */
	protected void createChatRoom () {
		// Create a new list of strings for user names
		ArrayList <String> userList = new ArrayList <String> ();
		// Traverse through the list of users that are selected
		for ( User user : this.users.getSelected () ) {
			// Append username to said list
			userList.add ( user.getUsername () );
		}
		// Add the parent username to that list
		userList.add ( this.parent.username );
		// Check to see that the list is at least you and someone else
		if ( userList.size () < 2 ) {
			// If it isn't then return
			return;
		}
		// Check to see if that type of group already exists
		Group exists = this.parent.messageArea.groups.findDuplicate ( userList );
		// If it doesn't exist
		if ( exists == null ) {
			// Prompt user for a name
			String name = "";
			while ( name.replaceAll ( "\\s+", "" ).equals ( "" ) ) {
				// Spawn an input box to enter group name
				name = JOptionPane.showInputDialog ( this.parent, "Please enter group name:", null );
				// If we choose to cancel then dip the fuck out
				if ( name == null ) {
					return;
				}
			}
			// Create a new hash for the group and append it as a group in the groups scroll panel
			String hash = Group.createHash ( 36 );
			this.parent.messageArea.groups.addGroup ( name.trim (), hash, userList );
			this.parent.messageArea.groups.setCurrentMessage ( hash );
		}
		// Otherwise if the group exists
		else {
			// Show that group in the foreground
			this.parent.messageArea.groups.setCurrentMessage ( exists.getHash () );
		}
	}

	/**
	 * This method takes in a search term and populates the users menu based on that search term.
	 * @param   String          term                The search term to search users
	 * @return  void
	 */
	protected void populateSearch ( String term ) {
		// Remove all users from the users menu
		this.users.getContentPanel ().removeAll ();
		// Loop through each user in list array bases on search term
		for ( User user : this.users.search ( term ) ) {
			// Append that user to the menu
			this.users.append ( user );
		}
		// Scroll the menu to the top
		this.users.scrollToTop ();
	}

	/**
	 * This function is here because this class implements the ActionListener.  It checks if the
	 * create a chat room button is clicked or if a search on the users list is done.
	 * @param   ActionEvent     event               The event that was called
	 * @return  void
	 */
	public void actionPerformed ( ActionEvent event ) {
		// Check to see if it was the create chat room button
		if ( event.getSource () == this.create ) {
			// Create a chat room and reset all input from text box
			this.createChatRoom ();
			this.populateSearch ( "" );
			this.search.empty ();
			this.users.resetSelection ();
			this.parent.chatArea.textbox.requestFocus ();
		}
		// If the search box changed value
		else if ( event.getSource () == this.search ) {
			// Create a search on term and populate users menu
			this.populateSearch ( this.search.getText ().trim () );
		}
	}

}
