package concord;
import java.util.ArrayList;

public class GroupData
{
	public ArrayList<GroupUserData> Users = new ArrayList<GroupUserData>();
	public ArrayList<ChatListing> Chats = new ArrayList<ChatListing>();
	public ArrayList<Role> Roles = new ArrayList<Role>();
	public long GroupID;
	
	public long addChat(ChatListing a)
	{
		Chats.add(a);
		return a.ChatID;
	}
	public boolean deleteChat(long ChatID)
	{
		for (int i = 0; i < Chats.size(); i++)
		{
			if (Chats.get(i).ChatID == ChatID)
			{
				Chats.remove(i);
				return true;
			}
		}
		return false;
	}
	public ChatListing findChatByID(long ChatID)
	{
		for (int i = 0; i < Chats.size(); i++)
		{
			if (Chats.get(i).ChatID == ChatID)
			{
				ChatListing foundByID = Chats.get(i);
				return foundByID;
			}
		}
		return null;
	}
	public GroupUserData addUser(UserData a)
	{
		GroupUserData dummy = new GroupUserData();
		dummy.User = a.UserID;
		dummy.Nickname = a.DisplayName;
		Users.add(dummy);
		return dummy;
	}
	public boolean removeUserFromGroup(GroupUserData a)
	{
		for (int i = 0; i < Users.size(); i++)
		{
			if (Users.get(i) == a)
			{
				Users.remove(i);
				return true;
			}
		}
		return false;
	}
	public GroupUserData findGroupUserByID(long GroupUserID)
	{
		for (int i = 0; i < Users.size(); i++)
		{
			if (Users.get(i).User == GroupUserID)
			{
				GroupUserData foundByID = Users.get(i);
				return foundByID;
			}
		}
		return null;
	}
}
