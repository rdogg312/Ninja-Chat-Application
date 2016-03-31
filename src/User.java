import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;

import Graphic.Display;
import Graphic.TextField;
import Graphic.Button;

@SuppressWarnings ( "serial" )
public class User extends Display {



	public User () {
		super ( "Ninja - Chat Application", 700, 600 );
		this.panel.setBackground ( Color.WHITE );
		this.panel.add ( createChatArea () );
		this.panel.add ( createSideArea () );
		this.panel.add ( createTabArea () );
		super.render ();
	}

	private JPanel createChatArea () {
		JPanel chatArea = Display.createPanel ( new Color ( 0xEBECEC ) );
		chatArea.setBounds ( 0, 500, 700, 80 );
		

		TextField chat = new TextField ( "Send Message...", 555, 50, 15 );
		chat.setPosition ( 15, 15 );
		chat.setFont ( new Font ( "Muli", Font.PLAIN, 19 ) );
		chatArea.add ( chat );

		Button send = new Button ( "Send", 100, 50 );
		send.setPosition ( 585, 15 );
		send.setFont ( new Font ( "Muli", Font.PLAIN, 19 ) );
		chatArea.add ( send );
		
		return chatArea;
	}

	private JPanel createSideArea () {
		JPanel sideArea = Display.createPanel ( new Color ( 0xF3F2F3 ) );
		sideArea.setBounds ( 0, 0, 200, 520 );

		JPanel searchPanel = Display.createPanel ( new Color ( 0xEBECEC ) );
		searchPanel.setBounds ( 0, 0, 200, 80 );
		sideArea.add ( searchPanel );

		TextField search = new TextField ( "Search...", 170, 50, 15 );
		search.setPosition ( 15, 15 );
		search.setFont ( new Font ( "Muli", Font.PLAIN, 19 ) );
		searchPanel.add ( search );
		return sideArea;
	}

	private JPanel createTabArea () {
		JPanel tabArea = Display.createPanel ( new Color ( 0xF3F2F3 ) );
		tabArea.setBounds ( 200, 0, 500, 80 );
		return tabArea;
	}

}