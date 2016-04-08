import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class JUnitRunner
{

    public static void main(String[] args)
    {
        Result[] results = new Result[1];
        results[0] = JUnitCore.runClasses(GroupDBTest.class);
        //results[1] = JUnitCore.runClasses(UsersDBTest.class);
        
        for(Result result : results)
        {
            for(Failure fail : result.getFailures())
                System.out.println(fail.toString());

            if(result.wasSuccessful())
                System.out.println("All tests finished successfully...");
        }
    }
}



