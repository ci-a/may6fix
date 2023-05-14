package sprint2;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import concord.ChatListing;
import concord.GroupData;
import concord.MsgData;
import concord.Role;
import concord.UserData;
import sprint4.DelayedMsg;

public class ClientObject implements Client, Serializable
{

	private static final long serialVersionUID = 1125584032478499740L;
	public GroupData Group;
	public UserData User;
	public Server ServerProxy;
	Registry Registry;

	public ClientObject(Registry registry)
	{
		Registry = registry;
		try 
		{
			ServerProxy = (Server)registry.lookup("concord");
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processUpdate(GroupData Update) throws RemoteException
	{
		Group = Update;

	}

	public String login(String Name, String Password) throws RemoteException{
		try 
		{
			User = ServerProxy.login(Name, Password, this);
			if(User != null)	
				return "login successful";
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "login failed";
	}
	
	public String getGroupData(long UserId, long GroupID){
		try 
		{             
			GroupData dummyGroup = ServerProxy.getGroupData(UserId, GroupID);
			if(dummyGroup != null)
			{
				return "group retrieval success";
			}
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "group retrieval failed";
	}
	
	public String sendMsg(long UserId, long GroupId, long ChatId, MsgData msg) throws RemoteException 
	{
		try 
		{             
			GroupData dummyGroup = ServerProxy.sendMsg(UserId, GroupId, ChatId, msg);
			if(dummyGroup != null)
			{
				return "message send success";
			}
		} 
		catch (RemoteException e) 
		{                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "message send failed";
	} 
	
	public String sendDelayedMsg(long UserId, long GroupId, long ChatId, MsgData msg, long seconds) throws RemoteException 
	{
		try 
		{          
			DelayedMsg delayedMsgObj = new DelayedMsg(UserId, GroupId, ChatId, msg, seconds, this);
			delayedMsgObj.SendMsg();
			GroupData dummyGroup = ServerProxy.getGroupData(UserId, GroupId);
			if(dummyGroup != null)
			{
				return "message send success";
			}
		} 
		catch (RemoteException e) 
		{                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "message send failed";
	} 
	
	public String makeGroup(long UserId, GroupData newGroup) throws RemoteException 
	{
		try 
		{             
			GroupData dummyGroup = ServerProxy.makeGroup(UserId, newGroup);
			if(dummyGroup != null)
			{	
				Group = dummyGroup;
				return "group make success";  
			}
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "group make failed";
	}         
	
	public String makeRole(long UserId, long GroupId, Role newRole) throws RemoteException 
	{
		try 
		{             
			GroupData dummyGroup = ServerProxy.makeRole(UserId, GroupId, newRole);
			if(dummyGroup != null)
			{
				Group = dummyGroup;
				return "role make success";
			}
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "role make failed";
	}
	
	public String makeChatListing(long UserId, long GroupId, ChatListing newChat) throws RemoteException {
		try 
		{             
			GroupData dummyGroup = ServerProxy.makeChatListing(UserId, GroupId, newChat);
			if(dummyGroup != null)
			{
				Group = dummyGroup;
				return "chat make success";
			}
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "chat make failed";
	}    
	
	public String inviteUser(long UserId, long GroupId, long InvitedUserId) throws RemoteException {
		try 
		{             
			GroupData dummyGroup = ServerProxy.inviteUser(UserId, GroupId, InvitedUserId);
			if(dummyGroup != null)
			{
				Group = dummyGroup;
				return "invite user success";
			}

		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "invite user failed";
	}                      
	
	public String deleteMsg(long UserId, long GroupId, long ChatId, long MsgId) throws RemoteException {
		try {             
			GroupData dummyGroup = ServerProxy.deleteMsg(UserId, GroupId, ChatId, MsgId);
			if(dummyGroup != null)
			{
				Group = dummyGroup;
				return "delete msg success"; 
			} 
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "delete msg failed";
	}         
	
	public String deleteChatListing(long UserId, long GroupId, long ChatId) throws RemoteException {
		try {             
			GroupData dummyGroup = ServerProxy.deleteChatListing(UserId, GroupId, ChatId);
			if(dummyGroup != null)
			{
				Group = dummyGroup;
				return "delete chat success"; 
			} 
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "delete chat failed";
	}      
	
	public String deleteGroup(long UserId, long GroupId) throws RemoteException {
		try 
		{             
			if(ServerProxy.deleteGroup(UserId, GroupId))
				Group = null;
			else
				return "delete group failed";  
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "delete group success";
	}       
	
	public String deleteRole(long UserId, long GroupId, String RoleName) throws RemoteException {
		try 
		{             
			GroupData dummyGroup = ServerProxy.deleteRole(UserId, GroupId, RoleName);
			if(dummyGroup != null)
			{
				Group = dummyGroup;
				return "delete role success";  
			}
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "delete role failed";
	}    
	
	public String deleteUser(long UserId, String Password) throws RemoteException {
		try 
		{             
			if(ServerProxy.deleteUser(UserId, Password))
				User = null;
			else
				return "delete user failed";  
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "delete user success";
	}      
	
	public String addUser(UserData newUser) throws RemoteException {
		try 
		{             
			UserData dummyUser = ServerProxy.addUser(newUser);
			if(dummyUser == null)
				return "add user failed";  
			else
				User = dummyUser;
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "add user success";
	}       
	
	public String giveTakeRole(long UserId, long GroupId, long TargetUser, String RoleName, boolean giveOrTake) throws RemoteException {
		try {             
			GroupData dummyGroup = ServerProxy.giveTakeRole(UserId, GroupId, TargetUser, RoleName, giveOrTake);
			if(dummyGroup == null)
				return "give take failed";
			else
				Group = dummyGroup;
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "give take success";
	} 	
	
	public String alertStatus(long UserID, long Status) {
		try {             
			ServerProxy.alertStatus(UserID, Status);
		} catch (RemoteException e) {                                    
			// TODO Auto-generated catch block                           
			e.printStackTrace();                                         
		}
		return "alert success";
	}
	
}