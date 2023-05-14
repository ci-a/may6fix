package concord;
import java.util.HashMap;

public class GroupDataRepo extends GroupData
{
	public HashMap<Long,GroupData> Groups = new HashMap<Long, GroupData>();
	
	public GroupData findGroupByID(long ID)
	{
		return Groups.get(ID);
	}
	public void deleteGroup(GroupData a)
	{
		Groups.remove(a.GroupID);
	}
	public void addGroup(GroupData a)
	{
		Groups.put(a.GroupID, a);
	}
}