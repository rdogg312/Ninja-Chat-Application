import java.awt.Cursor;
import java.awt.Color;
import java.io.File;
import javax.swing.JLabel;
import java.util.Scanner;
import java.util.ArrayList;

@SuppressWarnings ( "serial" )
public class Group extends JLabel {

	private String html;

	private String hash;

	private String groupName;

	private Messages messages;

	private ArrayList <String> users;

	private boolean read;

	private boolean selected;

	private Color selectedColor;

	private Color unSelectedColor;

	private String readBackground;

	private String unreadBackground;

	public Group ( String name, String hash, boolean read ) {
		super ( "" );
		this.groupName = name;
		this.hash = hash;
		this.readBackground = "#6D6D6D";
		this.unreadBackground = "#B0D35D";
		this.selectedColor = new Color ( 0xF3F2F3 );
		this.unSelectedColor = new Color ( 0xD3D3D3 );
		this.selected = false;
		this.html = this.loadHTML ( "./assets/templates/group.tpl" );
		this.setRead ( read );

		this.messages = new Messages ();

		super.setText ( this.setRead ( read ) );
		super.setCursor ( new Cursor ( Cursor.HAND_CURSOR ) );
		super.setOpaque ( true );
		super.setBackground ( this.unSelectedColor );
		super.setForeground ( new Color ( 0x6D6D6D ) );
	}

	private String setRead ( boolean read ) {
		// Save the read state internally
		this.read = read;
		// Copy the contents of the html file
		String contents = new String ( this.html );
		// Load in the group name
		contents = contents.replace ( "_GROUPNAME_", this.groupName );
		// If the state is marked as true ( read all messages )
		if ( read ) {
			// Set background of read box to be transparent
			contents = contents.replace ( "_READ_", this.readBackground );
		}
		// Otherwise assign a color to the read notification box
		else {
			// Set the color to the notification box
			contents = contents.replace ( "_READ_", this.unreadBackground );
		}
		// Save this into the label
		this.setText ( contents );
		// Return the resulting content
		return contents;
	}


	protected Messages setSelected ( boolean selected ) {
		this.selected = selected;
		if ( selected ) {
			this.setBackground ( this.selectedColor );
			this.setRead ( true );
			return this.messages;
		}
		else {
			this.setBackground ( this.unSelectedColor );
			return null;
		}
	}

	protected String getHash () {
		return this.hash;
	}
	protected Messages getMessages () {
		return this.messages;
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

}