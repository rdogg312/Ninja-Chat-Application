package edu.uic.cs342.ninja.client;

import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import edu.uic.cs342.ninja.graphic.ScrollPanel;

/**
 * This class exists to handle and store all the group instances, and it provides methods of
 * extracting and searching between them.  This class also implements the MouseListener class so it
 * watches mouse clicks on the group object tabs and context switches the messages on click time.
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
public class Groups extends ScrollPanel implements MouseListener {

	/**
	 * This data member saves the instance of the calling parent.  It is used to communicate across
	 * multiple GUI areas.
	 * @var     MessageArea     parent              The parent calling instance
	 */
	private MessageArea parent;

	/**
	 * This data member is an array list that is a collection of group element instances.  This is
	 * used to collect all the group tabs in one scrollable area.
	 * @var     ArrayList <Group>   groups          Array list of groups instances
	 */
	private ArrayList <Group> groups;

	/**
	 * This data member saves the current instance of the messages area.  It represents which group
	 * the user is interacting with currently and this can be changed programmatically and reflected
	 * upon in the GUI interface.  Initially this is null.
	 * @var     Messages        currentMessages     The current message area displayed
	 */
	private Messages currentMessages = null;

	/**
	 * This constructor saves the past information, and initializes the super constructor.  Not much
	 * styling or preference setting is done here because it is handled in the inherited ScrollPanel
	 * class.
	 * @param   MessageArea     parent              The calling parent instance
	 * @param   int             width               The width of the GUI element
	 * @param   int             height              The height of the GUI element
	 * @param   int             direction           This is the direction of the inline appending
	 */
	public Groups ( MessageArea parent, int width, int height, int direction ) {
		// Run the super constructor
		super ( width, height, direction );
		// Save the parent caller
		this.parent = parent;
		// Initialize the groups array list
		this.groups = new ArrayList <Group> ();
	}

	/**
	 * This function appends a group to the groups array if it is already not there, and it adds it
	 * to the scroll panel that consists of the group tabs.
	 * @param   Group           group               The group instance to append to the panel
	 * @return  void
	 */
	protected void append ( Group group ) {
		// Check if the group already exists in the groups array
		if ( !this.groups.contains ( group ) ) {
			// If it doesn't then add it to the array and attach the mouse listener to it
			this.groups.add ( group );
			group.addMouseListener ( this );
		}
		// Append it to the GUI element and update the scroll panel
		super.append ( group );
		super.update ();
	}

	/**
	 * This function tries to see if the passed list of users matches any groups, this is used to
	 * not create a duplicate group with the same people.  Instead it will just return a the group
	 * that those users belong to.
	 * @param   ArrayList <String>      list        A list of usernames
	 * @return  Group                               The group with the users in it
	 */
	protected Group findDuplicate ( ArrayList <String> list ) {
		// Initially the found flag is true
		Boolean found = true;
		// Loop though all the groups
		for ( int i = 0; i < this.groups.size (); i++ ) {
			// Initialize the current target group
			Group target = this.groups.get ( i );
			// Check to see if the elements within both the list and passed group is not the same
			if ( target.users.size () == list.size () ) {
				// Mark boolean as true
				found = true;
				// Loop through all the usernames in the groups
				for ( int j = 0; j < list.size (); j++ ) {
					// Check to see if they exist as a subset of the other list
					if ( !target.users.contains ( list.get ( j ) ) ) {
						// If they are not there, then turn flag to false and break
						found = false;
						break;
					}
				}
				// If we found something
				if ( found ) {
					// Return the target group
					return target;
				}
			}
		}
		// Otherwise by default return null
		return null;
	}

	/**
	 * This function goes through every group and if it doesn't match the passed hash id, then it is
	 * unselected.  Otherwise it is selected and it's message area is shown in the GUI.
	 * @param   String          hash                The hash id of the group to set as current
	 * @return  void
	 */
	protected void setCurrentMessage ( String hash ) {
		// Loop though all the groups in the list of group instances
		for ( Group group : this.groups ) {
			// Check if the hashes match
			if ( group.getHash ().equals ( hash ) ) {
				// Initialize variables for string joining of users array
				String users = "";
				String seperator = "";
				// Loop though the users of the group
				for ( String user : group.users ) {
					// Append to list and add a separator
					users += seperator + user;
					seperator = ", ";
				}
				// Loop though each component in the parent
				for ( Component component : this.parent.getComponents () ) {
					// If the component matches the current message
					if ( component == this.currentMessages ) {
						// Remove is from the GUI
						this.parent.remove ( component );
					}
				}
				// Now set the current to the selected group and add it to the GUI
				this.currentMessages = group.setSelected ( true );
				this.parent.add ( this.currentMessages );
				this.currentMessages.scrollToBottom ();
				this.parent.information.setText ( "Users: " + users );
			}
			// If the hashes dont match
			else {
				// Set selected of the current group to false
				group.setSelected ( false );
			}
		}
	}

	/**
	 * This function finds a group by it's hash ID and it returns it to the caller.
	 * @param   String          hash                The group hash to search with
	 * @return  Group                               The group matching hash ID
	 */
	protected Group getGroupByHash ( String hash ) {
		// Loop through all the groups in the group array
		for ( Group group : this.groups ) {
			// if the hashes match the passed hash
			if ( group.getHash ().equals ( hash ) ) {
				// Return the group
				return group;
			}
		}
		// Otherwise, by default return null
		return null;
	}

	/**
	 * This function adds a group to the scrollable panel in the GUI and also adds it to the group
	 * array.  Then resulting group is then returned to the called
	 * @param   String                  name                The name of the group
	 * @param   String                  hash                The hash of the group
	 * @param   ArrayList <String>      users               The users in this group
	 * @return  Group                                       The resulting group that was made
	 */
	protected Group addGroup ( String name, String hash, ArrayList <String> users ) {
		// Create a new group with the passed information
		Group newGroup = new Group ( name, hash, users, true );
		// Attach to the mouse listener, and append to the GUI and array list of groups
		newGroup.addMouseListener ( this );
		this.groups.add ( newGroup );
		this.append ( newGroup );
		// return the new group to the caller
		return newGroup;
	}

	/**
	 * This is a simple getter function that returns the current messages instance, since it is set
	 * as private.
	 * @return  Messages                                    The currently set message area
	 */
	protected Messages getCurrentMessages () {
		// Return the current messages instance handle
		return this.currentMessages;
	}

	/**
	 * This function is ran whenever the mouse is clicked on one of the group tabs.  It handles
	 * switching between which groups are selected and which message area to render on the screen.
	 * @param   MouseEvent      event                       The caught mouse event to evaluate
	 * @return  void
	 */
	public void mouseClicked ( MouseEvent event ) {
		// Make sure the instance is of the Group type
		if ( event.getSource () instanceof Group ) {
			// Traverse through each group in the groups array
			for ( Group group : this.groups ) {
				// If the source matches the current group in traversal
				if ( group == event.getSource () ) {
					// Then traverse through each component in the tabs scrollable panel
					for ( Component component : this.parent.getComponents () ) {
						// If the current is equal to the traversed
						if ( component == this.currentMessages ) {
							// Remove form the GUI
							this.parent.remove ( component );
						}
					}
					// Set the new one to be the matched group add it to the GUI and set as current
					this.setCurrentMessage ( group.getHash () );
					this.parent.add ( this.currentMessages );
					this.currentMessages.update ();
					this.currentMessages.scrollToBottom ();
				}
				// Otherwise it isn't
				else {
					// Set the traversed group to not be selected
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
