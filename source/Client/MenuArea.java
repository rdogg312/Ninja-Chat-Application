package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.ArrayList;
import Graphic.Button;
import Graphic.TextField;
import Graphic.ScrollPanel;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings ( "serial" )
public class MenuArea extends JPanel implements ActionListener {

	private ChatApplication parent;

	private TextField search;

	protected Users users;

	private Button create;

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


		for ( int i = 0; i < json.size (); i++ ) {
			JSONObject user = ( JSONObject ) json.get ( i );
			String username = user.get ( "username" ).toString ();
			if ( !username.equals ( this.parent.username ) ) {
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

	protected void createChatRoom () {
		ArrayList <String> userList = new ArrayList <String> ();
		for ( User user : this.users.getSelected () ) {
			userList.add ( user.getUsername () );
		}
		userList.add ( this.parent.username );
		if ( userList.size () < 2 ) {
			return;
		}
		Group exists = this.parent.messageArea.groups.findDuplicate ( userList );
		if ( exists == null ) {
			String name = "";
			while ( name.replaceAll ( "\\s+", "" ).equals ( "" ) ) {
				name = JOptionPane.showInputDialog ( this.parent, "Please enter group name:", null );
				if ( name == null ) {
					return;
				}
			}
			String hash = Group.createHash ( 36 );
			this.parent.messageArea.groups.addGroup ( name.trim (), hash, userList );
			this.parent.messageArea.groups.setCurrentMessage ( hash );
		}
		else {
			this.parent.messageArea.groups.setCurrentMessage ( exists.getHash () );
		}
	}

	protected void populateSearch ( String term ) {
		this.users.getContentPanel ().removeAll ();
		for ( User user : this.users.search ( term ) ) {
			this.users.append ( user );
		}
		this.users.scrollToTop ();
	}

	public void actionPerformed ( ActionEvent event ) {
		// Check to see if it was the create chat room button
		if ( event.getSource () == this.create ) {
			this.createChatRoom ();
			this.populateSearch ( "" );
			this.search.empty ();
			this.users.resetSelection ();
			this.parent.chatArea.textbox.requestFocus ();
		}
		// If the search box changed value
		else if ( event.getSource () == this.search ) {
			this.populateSearch ( this.search.getText ().trim () );
		}
    }

}