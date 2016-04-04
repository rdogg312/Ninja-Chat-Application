package Graphic;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings ( "serial" )
public class Display extends JFrame {

	protected Container panel;

	public Display ( String title, int width, int height ) {
		super ( title );
		super.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
		super.setResizable ( false );
		super.setSize ( width, height );
		this.panel = super.getContentPane ();
		this.panel.setLayout ( null );
	}

	protected void render () {
		// Get current screen size
		Dimension dimension = Toolkit.getDefaultToolkit ().getScreenSize ();
		// Center frame in the middle of the screen
		super.setLocation (
			dimension.width / 2 - getSize ().width / 2,
			dimension.height / 2 - getSize ().height / 2
		);
		// Set the frame to be visible
		super.setVisible ( true );
	}

	protected static JPanel createPanel ( Color background ) {
		JPanel panel = new JPanel ( null );
		panel.setBackground ( background );
		return panel;
	}

}