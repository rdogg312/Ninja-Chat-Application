import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Graphic.Button;
import Graphic.TextField;
import Graphic.ScrollPanel;

@SuppressWarnings ( "serial" )
public class MenuArea extends JPanel implements ActionListener {

	private ChatApplication parent;

	private TextField search;

	private Users users;

	private Button create;

	public MenuArea ( ChatApplication frame ) {
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


		this.users = new Users ( 200, 370 );
		this.users.setPosition ( 0, 80 );

		for ( int i = 0; i < 16; i++ ) {
			User u = new User ( "User #" + i, true );
			if ( i % 3 == 0 ) {
				u.setOnline ( true );
			}
			users.append ( u );
		}

		
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
		this.parent.messageArea.groups.addGroup ( "new room", "newhash" );
		System.out.print ( "Creating chat room with: " );
		for ( User user : this.users.getSelected () ) {
			System.out.print ( user.getUsername () + ", " );
		}
		System.out.println ( "and thats all!" );
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
			this.populateSearch ( this.search.getText () );
		}
    }

}