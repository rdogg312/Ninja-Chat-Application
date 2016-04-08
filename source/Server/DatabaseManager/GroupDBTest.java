import static org.junit.Assert.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.Test;

public class GroupDBTest
{

    @Test
    public void testConstructor()
    {
        JSONArray  users = new JSONArray();
        users.add("BennyS");
        users.add("NULL");
        GroupDB temp = new GroupDB("0", "Everybody", users);
        String from = "BennyS";
        String timestamp = "NIGGA WHAAT";
        String content = "NIGGA HELL YEEAAAHH";

        temp.addMessage(from, timestamp, content);

        users = new JSONArray();
        users.add("NULL");
        users.add("TheHolyBeast");

        GroupDB temp2 = new GroupDB("1", "TEMP1", users );

        from = "NULL";
        timestamp = "YOOOO";
        content = "WASSSUPPP";

        temp2.addMessage(from, timestamp, content);

        users = new JSONArray();
        users.add("CloudS");
        users.add("TamerS");

        GroupDB temp3 = new GroupDB("2", "TEMP2", users );

        from = "CloudS";
        timestamp = "YOOOO";
        content = "WASSSUPPP";

        temp3.addMessage(from, timestamp, content);

        System.out.println("\n\n\n\n\n\n\n\ngetGroups(BennyS): " + GroupDB.getGroups("BennyS").toString() + "\n");
        System.out.println("getGroups(NULL): " + GroupDB.getGroups("NULL").toString() + "\n");
        System.out.println("getGroups(TheHolyBeast): " + GroupDB.getGroups("TheHolyBeast").toString() + "\n");
        System.out.println("getGroups(CloudS): " + GroupDB.getGroups("CloudS").toString() + "\n");
        System.out.println("getGroups(TamerS): " + GroupDB.getGroups("TamerS").toString() + "\n");
        System.out.println("getGroups(HypeBeast): " + GroupDB.getGroups("HypeBeast").toString() + "\n");

    }
}
