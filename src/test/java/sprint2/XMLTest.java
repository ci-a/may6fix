package sprint2;


import static org.junit.jupiter.api.Assertions.*;


import java.rmi.RemoteException;

import java.rmi.registry.LocateRegistry;

import java.rmi.registry.Registry;

import java.util.ArrayList;


import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;


import concord.ChatListing;
import concord.GroupData;
import concord.MsgData;
import concord.Pair;

import concord.Role;

import concord.UserData;


class XMLTest
{
	ServerObject server1;
	ServerObject server2;
	ClientObject client1;
	ClientObject client2;
	Registry registry;
	UserData dummy1;
	UserData dummy2;
	ArrayList<Long> dummyList;
	Role adminRole;
	Pair perm;
	ArrayList<Pair> permList;
	ChatListing chatList;
	
	@BeforeEach
	void setUp() throws Exception
	{
	}
	@Test
	void test() throws RemoteException
	{
		//setting up server 1
		server1 = new ServerObject();
		registry = LocateRegistry.createRegistry(1099);
		registry.rebind("concord", server1);
		client1 = new ClientObject(registry);
		
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
		assertEquals(client1.addUser(dummy1), "add user success");
		assertEquals(client1.addUser(dummy2), "add user success");
		
		//logging in 
		assertEquals(client1.login("bot", "passwordlol"), "login successful");
		
		//make group
		GroupData group = new GroupData();
		group.GroupID = 99;
		assertEquals(client1.makeGroup(client1.User.UserID, group), "group make success");
		
		//test make role ***
		Pair perm1 = new Pair("invite_user", true);
		Pair perm2 = new Pair("can_kick", true);
		Pair perm3 = new Pair("give_take_role", true);
		Pair perm4 = new Pair("make_chat", true);
		Pair perm5 = new Pair("send_msg", true);
		Pair perm6 = new Pair("delete_msg", true);
		Pair perm7 = new Pair("delete_role", true);
		Pair perm8 = new Pair("delete_group", true);
				
		permList = new ArrayList<Pair>();
		permList.add(perm1);
		permList.add(perm2);
		permList.add(perm3);
		permList.add(perm4);
		permList.add(perm5);
		permList.add(perm6);
		permList.add(perm7);
		permList.add(perm8);
		adminRole = new Role("admin", permList);
		assertEquals(client1.makeRole(client1.User.UserID, client1.Group.GroupID, adminRole), "role make success");
		
		//make chatlist
		chatList = new ChatListing();
		chatList.ChatID = 300;
		assertEquals(client1.makeChatListing(client1.User.UserID, client1.Group.GroupID, chatList), "chat make success");
		
		//sending a message
		MsgData message = new MsgData();
		message.MsgIndex = 0;
		message.Text = "hi";
		message.deleted = false;
		assertEquals(client1.sendMsg(client1.User.UserID, client1.Group.GroupID, 300, message), "message send success");
		
		
		
		//creating server 2 and checking if data is identical ***
		
		//server objects are coded to automatically run disk.xml
		server2 = new ServerObject();
		
		//registered users
		long server1Bot1 = server1.CurrentUserRepository.Names.get("bot");
		long server2Bot1 = server2.CurrentUserRepository.Names.get("bot");
		long server1Bot2 = server1.CurrentUserRepository.Names.get("bot2");
		long server2Bot2 = server2.CurrentUserRepository.Names.get("bot2");
		
		//registered groups
		GroupData server1Group = server1.getGroupData(101, 99);
		GroupData server2Group = server2.getGroupData(101, 99);
		
		//registered role
		Role server1Role = server1Group.Roles.get(0);
		Role server2Role = server2Group.Roles.get(0);

		//registered chat
		ChatListing server1Chat = server1Group.findChatByID(300);
		ChatListing server2Chat = server2Group.findChatByID(300);
		
		//registered message
		MsgData server1Msg = server1Chat.findMsgByID(0);
		MsgData server2Msg = server2Chat.findMsgByID(0);
		
		//xml checking
		assertEquals(server1Bot1, server2Bot1);
		assertEquals(server1Bot2, server2Bot2);
		assertEquals(server1Group.GroupID, server2Group.GroupID);
		assertEquals(server1Role.Name, server2Role.Name);
		assertEquals(server1Chat.ChatID, server2Chat.ChatID);
		assertEquals(server1Msg.Text, server2Msg.Text);
		
	}
	
}