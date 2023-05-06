package concord;
import java.util.HashMap;

public class UserDataRepo
{
	HashMap<Long, UserData> Users = new HashMap<Long,UserData>();
	HashMap<String, Long> Names = new HashMap<String,Long>();
	
	public UserData findUserByID(long ID)
	{
		return Users.get(ID);
	}
	public UserData findUserByName(String input)
	{
		return Users.get(Names.get(input));
	}
	public void deleteUser(UserData a)
	{
		Users.remove(a.UserID);
	}
	public void addUser(UserData a)
	{
		if (a != null)
		{
			Users.put(a.UserID, a);
			Names.put(a.DisplayName, a.UserID);
		}
	}
}