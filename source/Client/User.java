package Client;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import javax.swing.JLabel;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileReader;
import java.io.File;
import java.lang.StringBuilder;

@SuppressWarnings ( "serial" )
public class User extends JLabel implements MouseListener {

	private String username;

	private boolean online;

	private boolean selected;

	private Color selectedColor;

	private Color unSelectedColor;

	private String onlineColor;

	private String offlineColor;

	private String html;

	public User ( String username, boolean online ) {
		super ( "" );

		this.unSelectedColor = new Color ( 0xF3F2F3 );
		this.selectedColor = new Color ( 0xD3D3D3 );
		this.offlineColor = "#6D6D6D";
		this.onlineColor = "#B0D35D";

		this.html = "";

		try {
			Scanner scanner = new Scanner ( new File ( "./assets/templates/user.tpl" ) );
			this.html  = scanner.useDelimiter("\\A").next();
			scanner.close();
		}
		catch ( Exception ex ) {
			System.exit ( 0 );
		}

		this.html = this.html.replace ( "_USERNAME_", username );
		this.html = this.html.replace ( "_ONLINE_", this.offlineColor );

		super.setText ( this.html );
		super.setCursor ( new Cursor ( Cursor.HAND_CURSOR ) );


		this.username = username;
		this.setOnline ( online );
		this.selected = false;

		super.setOpaque ( true );
		super.setBackground ( this.unSelectedColor );
		super.setForeground ( new Color ( 0x6D6D6D ) );

		// Add itself to the event listener
		this.addMouseListener ( this );
	}

	protected void setOnline ( boolean online ) {
		this.online = online;
		if ( online ) {
			this.html = this.html.replace ( this.offlineColor, this.onlineColor );
			this.setText ( this.html );
		}
		else {
			this.html = this.html.replace ( this.onlineColor, this.offlineColor );
			this.setText ( this.html );
		}
	}

	protected boolean getSelected () {
		return this.selected;
	}

	protected void setSelected ( boolean state ) {
		if ( state ) {
			this.setBackground ( this.selectedColor );
			this.selected = true;
		}
		else {
			this.setBackground ( this.unSelectedColor );
			this.selected = false;
		}
	}

	protected String getUsername () {
		return this.username;
	}


	public void mouseClicked ( MouseEvent event ) {
		// Since this listener is only for this label, we don't have to check the source
		if ( this.selected ) {
			this.setBackground ( this.unSelectedColor );
			this.selected = false;
		}
		else {
			this.setBackground ( this.selectedColor );
			this.selected = true;
		}
	}

	/**
	 * This is a required function that is required to implement the mouse listener, but has no
	 * purpose in this application.
	 * @param   MouseEvent      event               The mouse event that occurred
	 * @return  void
	 */
	public void mousePressed ( MouseEvent event ) {}

	/**
	 * This is a required function that is required to implement the mouse listener, but has no
	 * purpose in this application.
	 * @param   MouseEvent      event               The mouse event that occurred
	 * @return  void
	 */
	public void mouseReleased ( MouseEvent event ) {}

	/**
	 * This is a required function that is required to implement the mouse listener, but has no
	 * purpose in this application.
	 * @param   MouseEvent      event               The mouse event that occurred
	 * @return  void
	 */
	public void mouseEntered ( MouseEvent event ) {}

	/**
	 * This is a required function that is required to implement the mouse listener, but has no
	 * purpose in this application.
	 * @param   MouseEvent      event               The mouse event that occurred
	 * @return  void
	 */
	public void mouseExited ( MouseEvent event ) {}


}