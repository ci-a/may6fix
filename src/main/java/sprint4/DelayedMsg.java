package sprint4;

import java.rmi.RemoteException;

import concord.MsgData;
import sprint2.ClientObject;

public class DelayedMsg implements Command
{
	long UserID;
	long GroupID;
	long ChatID;
	MsgData Msg;
	long seconds;
	ClientObject ClientProxy;
	
	public DelayedMsg(long UserID, long GroupID, long ChatID, MsgData Msg, long seconds, ClientObject ClientProxy)
	{
		this.UserID = UserID;
		this.GroupID = GroupID;
		this.ChatID = ChatID;
		this.Msg = Msg;
		this.seconds = seconds;
		this.ClientProxy = ClientProxy;
	}
	
	@Override
	public void SendMsg()
	{
		long ms = seconds * 1000;
		try
		{
			Thread.sleep(ms);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			ClientProxy.sendMsg(UserID,GroupID, ChatID, Msg);
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
