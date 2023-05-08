package sprint2;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import concord.ChatListing;
import concord.Pair;
import concord.Role;
import concord.UserData;
import concord.GroupData;
import concord.MsgData;

class ServerObjectTest
{
	ServerObject server;
	ClientObject client;
	ClientObject mockClient;
	Registry registry;
	UserData dummy1;
	UserData dummy2;
	ArrayList<Long> dummyList;
	Role dummyRole;
	Pair perm;
	ArrayList<Pair> permList;
	ChatListing chatList;
	
	@BeforeEach
	void setUp() throws Exception
	{
	}

	@Test
	void test() throws Exception
	{
		//setting up rmi
		server = new ServerObject();
		registry = LocateRegistry.createRegistry(1099);
		registry.rebind("concord", server);
		client = new ClientObject(registry);
		
		//creating mock
		mockClient = new ClientObject(registry);
				
		//creating dummy users
		dummyList = new ArrayList<Long>();
				
		dummy1 = new UserData();
		dummy1.UserID = 101;
		dummy1.JoinedGroupIDs = dummyList;
		dummy1.Status = 1;
		dummy1.DisplayName = "bot";
		dummy1.Email = "fake@email.com";
		dummy1.Password = "passwordlol";
				
		dummy2 = new UserData();
		dummy2.UserID = 202;
		dummy2.JoinedGroupIDs = dummyList;
		dummy2.Status = 1;
		dummy2.DisplayName = "bot2";
		dummy2.Email = "fake2@email.com";
		dummy2.Password = "passwordlol2";
		
		//adding users
		assertEquals(client.addUser(dummy1), "add user success");
		assertEquals(client.addUser(dummy2), "add user success");
		assertEquals(client.addUser(null), "add user failed");
				
		//logging in
		assertEquals(client.login("falseUser", "falsepassword"), "login failed");
		assertEquals(client.login("bot", "falsepassword"), "login failed");
		//registering client
		assertEquals(client.login("bot", "passwordlol"), "login successful");
		//registering mock
		assertEquals(mockClient.login("bot2", "passwordlol2"), "login successful");
		
		assertEquals(server.RMIClientListing.get(dummy1.UserID), client);
		assertEquals(server.RMIClientListing.get(dummy2.UserID), mockClient);
		
		//make group
		GroupData group = new GroupData();
		group.GroupID = 99;
		assertEquals(client.addUser(dummy1), "add user success");
		assertEquals(client.makeGroup(32131234, group), "group make failed");
		assertEquals(client.makeGroup(client.User.UserID, group), "group make success");
				
		//get group data
		assertEquals(client.getGroupData(client.User.UserID, 5), "group retrieval failed");
		assertEquals(client.getGroupData(client.User.UserID, 99), "group retrieval success");
				
		//make role
		perm = new Pair("can_kick", true);
		permList = new ArrayList<Pair>();
		permList.add(perm);
		dummyRole = new Role("can_kick", permList);
		assertEquals(client.makeRole(client.User.UserID, client.Group.GroupID, dummyRole), "role make success");
		assertEquals(client.makeRole(31234, client.Group.GroupID, dummyRole), "role make failed");
		assertEquals(client.makeRole(client.User.UserID, 234543654, dummyRole), "role make failed");
				
		//inviting dummy2
		assertEquals(client.inviteUser(client.User.UserID, client.Group.GroupID, dummy2.UserID), "invite user success");
		assertEquals(client.inviteUser(34254366, client.Group.GroupID, dummy2.UserID), "invite user failed");
		assertEquals(client.inviteUser(client.User.UserID, 123124, dummy2.UserID), "invite user failed");

		//give or take role
		assertEquals(client.giveTakeRole(client.User.UserID, client.Group.GroupID, 303, "can_kick", true), "give take failed");
		assertEquals(client.giveTakeRole(client.User.UserID, client.Group.GroupID, 202, "can_kick", true), "give take success");
				
		//make chatlist
		chatList = new ChatListing();
		chatList.ChatID = 300;
		assertEquals(client.makeChatListing(client.User.UserID, client.Group.GroupID, chatList), "chat make success");
		assertEquals(client.makeChatListing(31232131, client.Group.GroupID, chatList), "chat make failed");
		assertEquals(client.makeChatListing(client.User.UserID, 2312312, chatList), "chat make failed");
				
		//sending a message
		MsgData message = new MsgData();
		message.MsgIndex = 0;
		message.Text = "hi";
		message.deleted = false;
		assertEquals(client.sendMsg(client.User.UserID, client.Group.GroupID, 300, message), "message send success");
		assertEquals(client.sendMsg(client.User.UserID, client.Group.GroupID, 1, message), "message send failed");
		
		//observer pattern test
		assertEquals(server.GroupDataRepository.findGroupByID(99), client.Group);
		
		//update the group data to all listeners
		server.alertUpdate(client.Group.GroupID);
		
		//if user status is offline, user is taken off of RMI listener
		client.alertStatus(client.User.UserID, 1);
		client.alertStatus(client.User.UserID, 0);
		
		//update the group data to all listeners
		server.alertUpdate(client.Group.GroupID);

		//delete role
		assertEquals(client.deleteRole(client.User.UserID, client.Group.GroupID, "can_kick"), "delete role success");
		assertEquals(client.deleteRole(client.User.UserID, client.Group.GroupID, "can_kick"), "delete role failed");
		assertEquals(client.deleteRole(client.User.UserID, client.Group.GroupID, "fake_role"), "delete role failed");
				
		//delete message
		assertEquals(client.deleteMsg(client.User.UserID, client.Group.GroupID, 300, 0), "delete msg success");
		assertEquals(client.deleteMsg(client.User.UserID, client.Group.GroupID, 300, 0), "delete msg failed");
				
		//delete chatlist
		assertEquals(client.deleteChatListing(client.User.UserID, client.Group.GroupID, 300), "delete chat success");
		assertEquals(client.deleteChatListing(client.User.UserID, client.Group.GroupID, 300), "delete chat failed");
		assertEquals(client.deleteChatListing(client.User.UserID, client.Group.GroupID, 43534534), "delete chat failed");
				
		//delete group and delete user
		assertEquals(client.deleteGroup(client.User.UserID, client.Group.GroupID), "delete group success");
		assertEquals(client.deleteGroup(client.User.UserID, 3211234), "delete group failed");
		assertEquals(client.deleteUser(client.User.UserID, "incorrectPW"), "delete user failed");
		assertEquals(client.deleteUser(client.User.UserID, "passwordlol"), "delete user success");
				

	}

}