package sprint2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import concord.ChatListing;
import concord.GroupData;
import concord.MsgData;
import concord.Role;
import concord.UserData;

public interface Server extends Remote
{
	public UserData login(String Name, String Password, ClientObject User) throws RemoteException;
	public GroupData getGroupData(long UserID, long GroupID) throws RemoteException;
	public GroupData sendMsg(long UserID, long GroupID, long ChatID, MsgData msg) throws RemoteException;
	public GroupData makeGroup(long UserID, GroupData newGroup) throws RemoteException;
	public GroupData makeRole(long UserID, long GroupID, Role newRole) throws RemoteException;
	public GroupData makeChatListing(long UserID, long GroupID, ChatListing newChat) throws RemoteException;
	public GroupData inviteUser(long UserID, long GroupID, long InvitedUserID) throws RemoteException;
	public GroupData deleteMsg(long UserID, long GroupID, long ChatID, long MsgID) throws RemoteException;
	public GroupData deleteChatListing(long UserID, long GroupID, long ChatID) throws RemoteException;
	public boolean deleteGroup(long UserID, long GroupID) throws RemoteException;
	public GroupData deleteRole(long UserID, long GroupID, String roleName) throws RemoteException;
	public boolean deleteUser(long UserID, String Password) throws RemoteException;
	public UserData addUser(UserData newUser) throws RemoteException;
	public GroupData giveTakeRole(long UserID, long groupID, long TargetUser, String RoleName, boolean giveOrTake) throws RemoteException;
	public void alertStatus(long UserID, long Status) throws RemoteException;
}