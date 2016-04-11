package Server;

/**
 * This class simply stores a user by that it stores the username and the hashed password
 * Provides simple getters and no setters. 
 * 
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @category    Project #04 - Ninja: Chat Application
 * @package     Server
 * @author      Rafael Grigorian
 * @author      Byambasuren Gansukh
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
public class User
{
    /**
     * This internal data member saves the username of a User
     * @var     String           username               Holds the username of this User
     */
    private String username;

    /**
     * This internal data member saves the hashed password of a User
     * @var     String           password_hash          Holds the hashed password of this User
     */
    private String password_hash;

    /**
     * This constructor simply instantiates the data members
     * @param   String          username                Value to init data member username
     * @param   String          password_hash           Value to init data member password_hash
     */
    public User(String username, String password_hash)
    {
        // Init data members
        this.username = username;
        this.password_hash = password_hash;
    }

    /**
     * This function is a Getter for the username of this User
     * @return  String          this.username           Data member username         
     */
    public String getUsername()
    {
        return this.username;
    }

    /**
     * This function is a Getter for the hashed password of this User
     * @return  String          this.password_hash      Data member password_hash         
     */
    public String getPassword_hash()
    {
        return this.password_hash;
    }
}