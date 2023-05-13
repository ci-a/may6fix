package concord;
import java.util.ArrayList;


public class GroupUserData
{
	long User;
	String Nickname;
	public ArrayList<Role> Roles = new ArrayList<Role>();
	
	public boolean checkPerm(String Perm) 
	{
		for(int i = 0; i < Roles.size(); i++)
		{
			for(int j = 0; j < Roles.get(i).Perms.size(); j++)
			{
				if(Roles.get(i).Perms.get(j).getName().equals(Perm))
				{
					if(Roles.get(i).Perms.get(j).getSet())
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}
		return false;
	}
}