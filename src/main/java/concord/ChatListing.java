package concord;
import java.util.ArrayList;

public class ChatListing
{
	public long ChatID;
	public ArrayList<MsgData> Chat = new ArrayList<MsgData>();
	public String ChatName;
	
	public boolean deleteMsg(long a)
	{
		for (int i = 0; i < Chat.size(); i++)
		{
			if (Chat.get(i).MsgIndex == a)
			{
				if (Chat.get(i).deleted != true)
				{
					Chat.get(i).deleted = true;
					return true;
				}
			}
		}
		return false;
	}
	public long addMsg(MsgData a)
	{
		Chat.add(a);
		return a.MsgIndex;
	}
	public MsgData findMsgByID(long a)
	{
		for (int i = 0; i < Chat.size(); i++)
		{
			if (Chat.get(i).MsgIndex == a)
			{
				return Chat.get(i);
			}
		}
		return null;
	}
}