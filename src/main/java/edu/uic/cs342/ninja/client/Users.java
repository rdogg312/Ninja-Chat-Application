package edu.uic.cs342.ninja.client;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import edu.uic.cs342.ninja.graphic.ScrollPanel;

/**
 * This class initializes the users scroll panel and also has methods that will search and act upon
 * the internally stored users list array.  These methods are used heavily outside of this class so
 * it is very important.
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
class Users extends ScrollPanel {

	/**
	 * This data member holds the width of the users panel, and is used within the setPostion
	 * function.
	 * @var     int             width               The width of the users panel
	 */
	private int width;

	/**
	 * This data member holds the height of the users panel, and is used within the setPostion
	 * function.
	 * @var     int             height              The height of the users panel
	 */
	private int height;

	/**
	 * This internal data member holds a list of users inside an array list structure.
	 * @var     ArrayList <User>    list            An array of User objects
	 */
	private ArrayList <User> list;

	/**
	 * This constructor sets up the panel and initializes the users array list
	 * @param   int             width               The width of the users panel
	 * @param   int             height              The height of the users panel
	 */
	public Users ( int width, int height ) {
		// Run the super constructor and make the panel vertically scrollable
		super ( width, height, BoxLayout.Y_AXIS );
		// Set the background color of the users panel
		super.getContentPanel ().setBackground ( new Color ( 0xF3F2F3 ) );
		// Save the width and the height internally
		this.width = width;
		this.height = height;
		// Initialize the array list
		this.list = new ArrayList <User> ();
	}

	/**
	 * This function simply finds a User object from the list of users based on the username that is
	 * passed to the method.
	 * @param   String          username            The username of the target user for searching
	 * @return  User                                The user object matching the passed username
	 */
	protected User findUser ( String username ) {
		// Loop through all the users in the user list
		for ( User user : this.list ) {
			// Check to see if the username matches the iterative one's
			if ( user.getUsername ().toLowerCase ().equals ( username.toLowerCase () ) ) {
				// If it does, then return that user
				return user;
			}
		}
		// Otherwise, return null
		return null;
	}

	/**
	 * This function appends a user object to the users list if it doesn't exist within it already.
	 * It also re-renders the panel again to show the changes.
	 * @param   User            user                The user instance to append to list and panel
	 * @return  void
	 */
	protected void append ( User user ) {
		// Check to see if user object is already in list
		if ( !this.list.contains ( user ) ) {
			// If it isn't then add it to the list
			this.list.add ( user );
		}
		// Append the user to the panel and render changes
		super.append ( user );
		super.update ();
	}

	/**
	 * This function traverses the users list and returns a new list that contains only users that
	 * are selected.
	 * @return  ArrayList <User>                    A list containing all selected users
	 */
	protected ArrayList <User> getSelected () {
		// Initialize a new array list
		ArrayList <User> selected = new ArrayList <User> ();
		// Traverse through the users list
		for ( User user : this.list ) {
			// Check to see that the user is selected
			if ( user.getSelected () ) {
				// Add to the new array list
				selected.add ( user );
			}
		}
		// Return the resulting list
		return selected;
	}

	/**
	 * This function returns a new array list containing users that match the passed search term.
	 * If the search term is an empty string, then all users are returned.
	 * @param   String          term                The target search term
	 * @return  ArrayList <User>                    A list containing all users matching search term
	 */
	protected ArrayList <User> search ( String term ) {
		// First check to see if the search term is empty
		if ( term.equals ( "" ) ) {
			// If it is, then return the whole list
			return this.list;
		}
		// Otherwise, initialize a new result array list
		ArrayList <User> results = new ArrayList <User> ();
		// Traverse through the list
		for ( User user : this.list ) {
			// Check to see if search term is a substring of the username
			if ( user.getUsername ().toLowerCase ().contains ( term.toLowerCase () ) ) {
				// If it is then add it to the new array list
				results.add ( user );
			}
		}
		// Return the resulting array list
		return results;
	}

	/**
	 * This function resets all users in list to be deselected.
	 * @return  void
	 */
	protected void resetSelection () {
		// Traverse through the users list
		for ( User user : this.list ) {
			// Set selection to false, rest is handled within User class
			user.setSelected ( false );
		}
	}

}
