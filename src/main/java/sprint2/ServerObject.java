package sprint2;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


import concord.ChatListing;
import concord.GroupData;
import concord.MsgData;
import concord.Role;
import concord.Pair;
import concord.UserData;
import concord.GroupDataRepo;
import concord.GroupUserData;
import concord.UserDataRepo;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerObject extends UnicastRemoteObject implements Server
{

	private static final long serialVersionUID = -8811065592251308624L;
	GroupDataRepo GroupDataRepository = new GroupDataRepo();
	UserDataRepo CurrentUserRepository = new UserDataRepo();
	HashMap<Long, ClientObject> RMIClientListing = new HashMap<Long,ClientObject>();
	boolean changed;

	protected ServerObject() throws RemoteException
	{
		XMLDecoder decoder;
		try
		{
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream("disk.xml")));
			GroupDataRepository = (GroupDataRepo)decoder.readObject();
			CurrentUserRepository = (UserDataRepo)decoder.readObject();
		} catch (FileNotFoundException e)
		{
			decoder = null;
		}
	}
	
	public void saveDisk(String fileName)
	{
		XMLEncoder encoder = null;
		try
		{
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
		}
		catch(FileNotFoundException fileNotFound)
		{
				System.out.println("file not found");
		}
		encoder.writeObject(GroupDataRepository);
		encoder.writeObject(CurrentUserRepository);
		encoder.close();
	}
	

	public UserData login(String Name, String Password, ClientObject User) throws RemoteException
	{
		if (CurrentUserRepository.findUserByName(Name) != null)
		{
			UserData account = CurrentUserRepository.findUserByName(Name);
			if (account.Password == Password)
			{
				RMIClientListing.put(account.UserID, User);

				// save to disk
				this.saveDisk("disk.xml");
				
				return account;
			}
		}
		return null;
	}

	public GroupData getGroupData(long UserID, long GroupID) throws RemoteException
	{
		UserData account = CurrentUserRepository.findUserByID(UserID);
		GroupData group = GroupDataRepository.findGroupByID(GroupID);
		for (Long id : account.JoinedGroupIDs)
		{
			if (id == GroupID)
			{
				return group;
			}
		}
		return null;
	}

	public GroupData sendMsg(long UserID, long GroupID, long ChatID, MsgData msg) throws RemoteException
	{
		UserData account = CurrentUserRepository.findUserByID(UserID);
		GroupData group = GroupDataRepository.findGroupByID(GroupID);
		for (Long id : account.JoinedGroupIDs)
		{
			if (id == GroupID)
			{
				if (group.findChatByID(ChatID) != null)
				{
					if (checkPerm(UserID, GroupID, "send_msg") == true)
					{
						ChatListing chatlist = group.findChatByID(ChatID);
						chatlist.addMsg(msg);
						
						//save to disk & update listeners
						this.saveDisk("disk.xml");
						this.alertUpdate(GroupID);
						
						return group;
					}
				}
			}
		}
		return null;
	}

	public GroupData makeGroup(long UserID, GroupData newGroup) throws RemoteException
	{
		if (CurrentUserRepository.findUserByID(UserID) != null)
		{
			UserData account = CurrentUserRepository.findUserByID(UserID);
			GroupDataRepository.addGroup(newGroup);
			account.JoinedGroupIDs.add(newGroup.GroupID);
			newGroup.addUser(account);

			// save to disk + new group
			this.saveDisk("disk.xml");
			
			return newGroup;
		}
		else
		{
			return null;
		}
	}

	public GroupData makeRole(long UserID, long GroupID, Role newRole) throws RemoteException
	{
		if (CurrentUserRepository.findUserByID(UserID) != null)
		{
			UserData account = CurrentUserRepository.findUserByID(UserID);
			if (GroupDataRepository.findGroupByID(GroupID) != null)
			{
				GroupData group = GroupDataRepository.findGroupByID(GroupID);
				for (Long id : account.JoinedGroupIDs)
				{
					if (id == GroupID)
					{
						group.Roles.add(newRole);
						GroupUserData guData = group.findGroupUserByID(UserID);
						guData.Roles.add(newRole);
							
						//save to disk & update listeners
						this.saveDisk("disk.xml");
						this.alertUpdate(GroupID);
							
						return group;
					}
				}
			}
		}
		return null;
	}

	public GroupData makeChatListing(long UserID, long GroupID, ChatListing newChat) throws RemoteException
	{
		if (CurrentUserRepository.findUserByID(UserID) != null)
		{
			UserData account = CurrentUserRepository.findUserByID(UserID);
			if (GroupDataRepository.findGroupByID(GroupID) != null)
			{
				GroupData group = GroupDataRepository.findGroupByID(GroupID);
				for (Long id : account.JoinedGroupIDs)
				{
					if (id == GroupID)
					{
						if (checkPerm(UserID, GroupID, "make_chat") == true)
						{
							group.addChat(newChat);
							
							//save to disk & update listeners
							this.saveDisk("disk.xml");
							this.alertUpdate(GroupID);
							
							return group;
						}
					}
				}
			}
		}
		return null;
	}

	public GroupData inviteUser(long UserID, long GroupID, long InvitedUserID) throws RemoteException
	{
		if (CurrentUserRepository.findUserByID(UserID) != null)
		{
			UserData account = CurrentUserRepository.findUserByID(UserID);
			if (CurrentUserRepository.findUserByID(InvitedUserID) != null)
			{
				UserData invitedAccount = CurrentUserRepository.findUserByID(InvitedUserID);
				if (GroupDataRepository.findGroupByID(GroupID) != null)
				{
					GroupData group = GroupDataRepository.findGroupByID(GroupID);
					for (Long id : account.JoinedGroupIDs)
					{
						if (id == GroupID)
						{
							if (checkPerm(UserID, GroupID, "invite_user") == true)
							{
								group.addUser(invitedAccount);
								invitedAccount.JoinedGroupIDs.add(GroupID);
	
								// save to disk
								this.saveDisk("disk.xml");
								
								return group;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public GroupData deleteMsg(long UserID, long GroupID, long ChatID, long MsgID) throws RemoteException
	{
		UserData account = CurrentUserRepository.findUserByID(UserID);
		GroupData group = GroupDataRepository.findGroupByID(GroupID);
		for (Long id : account.JoinedGroupIDs)
		{
			if (id == GroupID)
			{
				for (ChatListing chat : group.Chats)
				{
					if (chat == group.findChatByID(ChatID))
					{
						if (checkPerm(UserID, GroupID, "delete_msg") == true)
						{
							boolean results = chat.deleteMsg(MsgID);
							if (results == true)
							{
								//save to disk & update listeners
								this.saveDisk("disk.xml");
								this.alertUpdate(GroupID);
								
								return group;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public GroupData deleteChatListing(long UserID, long GroupID, long ChatID) throws RemoteException
	{
		UserData account = CurrentUserRepository.findUserByID(UserID);
		GroupData group = GroupDataRepository.findGroupByID(GroupID);
		for (Long id : account.JoinedGroupIDs)
		{
			if (id == GroupID)
			{
				if (checkPerm(UserID, GroupID, "send_msg") == true)
				{
					boolean results = group.deleteChat(ChatID);
					if (results == true)
					{
						//save to disk & update listeners
						this.saveDisk("disk.xml");
						this.alertUpdate(GroupID);
						
						return group;
					}
				}
			}
		}
		return null;
	}

	public boolean deleteGroup(long UserID, long GroupID) throws RemoteException
	{
		//UserData account = CurrentUserRepository.findUserByID(UserID);
		if (GroupDataRepository.findGroupByID(GroupID) != null)
		{
			if (checkPerm(UserID, GroupID, "delete_group") == true)
			{
				GroupData group = GroupDataRepository.findGroupByID(GroupID);
				GroupDataRepository.deleteGroup(group);
				
				//save to disk & update listeners
				this.saveDisk("disk.xml");
				this.alertUpdate(GroupID);
				
				return true;
			}
		}
		return false;
	}

	public GroupData deleteRole(long UserID, long GroupID, String roleName) throws RemoteException
	{
		if (CurrentUserRepository.findUserByID(UserID) != null)
		{
			//UserData account = CurrentUserRepository.findUserByID(UserID);
			if (GroupDataRepository.findGroupByID(GroupID) != null)
			{
				GroupData group = GroupDataRepository.findGroupByID(GroupID);
				int i = 0;
				for (Role role : group.Roles)
				{
					if (role.Name == roleName)
					{
						if (checkPerm(UserID, GroupID, "delete_role") == true)
						{
							group.Roles.remove(i);
							
							//save to disk & update listeners
							this.saveDisk("disk.xml");
							this.alertUpdate(GroupID);
							
							return group;
						}
					}
					i++;
				}
			}
		}
		return null;
	}
	

	public boolean deleteUser(long UserID, String Password) throws RemoteException
	{
		UserData user = CurrentUserRepository.findUserByID(UserID);
		if (user.Password == Password)
		{
			CurrentUserRepository.deleteUser(user);
			
			// save to disk
			this.saveDisk("disk.xml");
			
			return true;
		}
		return false;
	}

	public UserData addUser(UserData newUser) throws RemoteException
	{
		
		CurrentUserRepository.addUser(newUser);
		
		//save to disk
		this.saveDisk("disk.xml");
		
		return newUser;
	}

	public GroupData giveTakeRole(long UserID, long GroupID, long TargetUser, String RoleName, boolean giveOrTake)
			throws RemoteException
	{
		GroupUserData guData = null;
		GroupData group = GroupDataRepository.findGroupByID(GroupID);
		if (group.findGroupUserByID(TargetUser) != null)
		{
			guData = group.findGroupUserByID(TargetUser);
			if (giveOrTake == true)
			{
				if (checkPerm(UserID, GroupID, "give_take_role") == true)
				{
					ArrayList<Role> roles = guData.Roles;
					ArrayList<Pair> perms = new ArrayList<Pair>();
					Role nRole = new Role(RoleName, perms);
					roles.add(nRole);
					
					//save to disk & update listeners
					this.saveDisk("disk.xml");
					this.alertUpdate(GroupID);
					
					return group;
				}
			}
		}
		return null;
	}
	
	public void alertUpdate(long GroupId) 
	{
		for(long key: RMIClientListing.keySet())
		{
			try 
			{
				RMIClientListing.get(key).processUpdate(GroupDataRepository.findGroupByID(GroupId));
			} 
			catch (RemoteException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
	}

	public void alertStatus(long UserID, long Status) 
	{
		if (Status == 0)
		{
			RMIClientListing.remove(UserID);
		}
	}
	
	public boolean checkPerm(long UserID, long GroupID, String Perm)
	{
		GroupData group = GroupDataRepository.findGroupByID(GroupID);
		if (group != null)
		{
			GroupUserData groupUser = group.findGroupUserByID(UserID);
			if (groupUser != null)
			{
				return groupUser.checkPerm(Perm);
			}
		}
		return false;
	}
	
}