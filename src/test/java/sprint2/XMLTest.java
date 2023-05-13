package sprint2;


import static org.junit.jupiter.api.Assertions.*;


import java.rmi.RemoteException;

import java.rmi.registry.LocateRegistry;

import java.rmi.registry.Registry;

import java.util.ArrayList;


import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;


import concord.ChatListing;

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
	Role dummyRole;
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
		
		//printing list of added users for server 1
		System.out.println(server1.CurrentUserRepository.Users);
		System.out.println(server1.CurrentUserRepository.Names);
		
		//server 2
		server2 = new ServerObject();
		registry = LocateRegistry.createRegistry(1089);
		registry.rebind("concord", server2);
		client2 = new ClientObject(registry);
		
		System.out.println(server2.CurrentUserRepository.Users);
		System.out.println(server2.CurrentUserRepository.Names);
		
		//setting values to variables
		long server1Bot1 = server1.CurrentUserRepository.Names.get("bot");
		long server2Bot1 = server2.CurrentUserRepository.Names.get("bot");
		long server1Bot2 = server1.CurrentUserRepository.Names.get("bot2");
		long server2Bot2 = server2.CurrentUserRepository.Names.get("bot2");
		
		//xml checking
		assertEquals(server1Bot1, server2Bot1);
		assertEquals(server1Bot2, server2Bot2);
	}
	
}