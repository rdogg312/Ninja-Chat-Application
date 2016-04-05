import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import Graphic.ScrollPanel;

@SuppressWarnings ( "serial" )
public class Groups extends ScrollPanel implements MouseListener {

	private ArrayList <Group> groups;

	private Messages currentMessages = null;

	private MessageArea parent;

	public Groups ( MessageArea parent, int width, int height, int direction ) {
		// Run the super constructor
		super ( width, height, direction );

		this.parent = parent;

		// Initialize the groups array list
		this.groups = new ArrayList <Group> ();
	}

	protected void append ( Group group ) {
		if ( !this.groups.contains ( group ) ) {
			this.groups.add ( group );
			group.addMouseListener ( this );
		}
		super.append ( group );
		super.update ();
	}

	protected Group findDuplicate ( ArrayList <String> list ) {
		Boolean found = true;
		for ( int i = 0; i < this.groups.size (); i++ ) {
			Group target = this.groups.get ( i );
			if ( target.users.size () == list.size () ) {
				found = true;
				for ( int j = 0; j < list.size (); j++ ) {
					if ( !target.users.contains ( list.get ( j ) ) ) {
						found = false;
						break;
					}
				}
				if ( found ) {
					return target;
				}
			}
		}
		return null;
	}

	protected void setCurrentMessage ( String hash ) {
		for ( Group group : this.groups ) {
			if ( group.getHash ().equals ( hash ) ) {
				String users = "";
				String seperator = "";
				for ( String user : group.users ) {
					users += seperator + user;
					seperator = ", ";
				}
				for ( Component component : this.parent.getComponents () ) {
					if ( component == this.currentMessages ) {
						this.parent.remove ( component );
					}
				}
				this.currentMessages = group.setSelected ( true );
				this.parent.add ( this.currentMessages );
				this.parent.information.setText ( "Users: " + users );
			}
			else {
				group.setSelected ( false );
			}
		}
	}

	protected Messages getCurrentMessages () {
		return this.currentMessages;
	}

	protected void addGroup ( String name, String hash, ArrayList <String> users ) {
		Group newGroup = new Group ( name, hash, users, true );
		newGroup.addMouseListener ( this );
		this.groups.add ( newGroup );
		this.append ( newGroup );
	}

	public void mouseClicked ( MouseEvent event ) {
		if ( event.getSource () instanceof Group ) {
			for ( Group group : this.groups ) {
				if ( group == event.getSource () ) {

					for ( Component component : this.parent.getComponents () ) {
						if ( component == this.currentMessages ) {
							this.parent.remove ( component );
						}
					}
					this.setCurrentMessage ( group.getHash () );
					this.parent.add ( this.currentMessages );
					this.currentMessages.update ();
					this.currentMessages.scrollToBottom ();
				}
				else {
					group.setSelected ( false );
				}
			}
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