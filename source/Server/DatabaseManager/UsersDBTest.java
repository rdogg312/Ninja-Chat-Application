import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;

public class UsersDBTest
{
    String existent = "../../assets/databases/users/users.db";
    String nonexistent = "../../assets/testDatabases/users/users.db";

    @Test // Tests createFileAndPath()
    public void testCreateFileAndPath()
    {
        // File exists, should return false
        assertFalse("Create existent: ", UsersDB.createFileAndPath(existent));

        // File doesn't exist, should return true;
        // If run more than once this fails, delete fakeDatabases folder and try again
        assertFalse("Create nonexistent: ", UsersDB.createFileAndPath(nonexistent));
    }

    @Test // Tests load()
    public void testLoad()
    { 
        UsersDB temp = new UsersDB(existent);
        assertEquals("BennyS: ", "BennyS", temp.USERS.get(0).getUsername());
        assertEquals("Password Bennys: ", temp.hash("blahblah"), temp.USERS.get(0).getPassword_hash());
        
        assertEquals("NULL: ", "NULL", temp.USERS.get(1).getUsername());
        assertEquals("Password NULL: ", temp.hash("heeelloo"), temp.USERS.get(1).getPassword_hash());
        
        assertEquals("HypeBeast: ", "HypeBeast", temp.USERS.get(2).getUsername());
        assertEquals("Password HypeBeast: ", temp.hash("yelloww"), temp.USERS.get(2).getPassword_hash());
        
        assertEquals("Size of Array: ", 3, temp.USERS.size());
    }

    @Test // Tests userExists();
    public void testUserExists()
    {
        UsersDB temp = new UsersDB(existent);
        assertTrue("BennyS: ", temp.userExists("BennyS"));
        assertTrue("BennyS: ", temp.userExists("bennys"));
        assertTrue("BennyS: ", temp.userExists("bEnNyS"));

        assertTrue("NULL: ", temp.userExists("Null"));
        assertTrue("NULL: ", temp.userExists("NULL"));
        assertTrue("NULL: ", temp.userExists("nuLl"));

        assertTrue("HypeBeast: ", temp.userExists("HypeBeast"));
        assertTrue("HypeBeast: ", temp.userExists("hypebeast"));
        assertTrue("HypeBeast: ", temp.userExists("HypeBEAsT"));

        assertFalse("Doesn't Exist: ", temp.userExists("BennySS"));
        assertFalse("Doesn't Exist: ", temp.userExists("NULLY"));
        assertFalse("Doesn't Exist: ", temp.userExists("HYPEBAST"));
        assertFalse("Doesn't Exist: ", temp.userExists("HELLO"));
        assertFalse("Doesn't Exist: ", temp.userExists("MIKE"));
    }

    @Test
    public void testHash()
    {
        UsersDB temp = new UsersDB(nonexistent);
        assertEquals(temp.hash("HELLOWORRRRLLLLDDD"), temp.hash("HELLOWORRRRLLLLDDD"));
        assertNotEquals(temp.hash("HELLOWORD"), temp.hash("HELLOWORRRRLLLLDDD"));
    }

    @Test // Tests userAdd() which tests hash() and update()
    public void testUserAdd()
    {
        // Tests
        UsersDB temp = new UsersDB(nonexistent);
        assertFalse("BennyS: ", temp.userExists("BennyS"));
        assertFalse("NULL: ", temp.userExists("NULL"));
        assertFalse("HypeBeast: ", temp.userExists("HypeBeast"));

        assertTrue("Added BennyS: ", temp.userAdd("BennyS", "blahblah"));
        assertTrue("Added NULL: ", temp.userAdd("NULL", "heeelloo"));
        assertTrue("Added HypeBeast: ", temp.userAdd("HypeBeast", "yelloww"));

        assertTrue("BennyS: ", temp.userExists("BennyS"));
        assertTrue("NULL: ", temp.userExists("NULL"));
        assertTrue("HypeBeast: ", temp.userExists("HypeBeast"));
    }

    @Test
    public void testUserLogin()
    {
        UsersDB temp = new UsersDB(nonexistent);
        assertTrue("Login BennyS: ", temp.userLogin("BennyS", "blahblah"));
        assertFalse("Login BennySS: ", temp.userLogin("Bennyss", "blahblah"));
        assertTrue("Login HypeBeast: ", temp.userLogin("HypeBeast", "yelloww"));
        assertFalse("Login null: ", temp.userLogin("null", "heeelloo"));
    }


}