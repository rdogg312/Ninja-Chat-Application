package Server;

public class User
{
    private String username;
    private String password_hash;

    public User(String username, String password_hash)
    {
        this.username = username;
        this.password_hash = password_hash;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword_hash()
    {
        return this.password_hash;
    }



}