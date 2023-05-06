package concord;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupDataTest
{
	GroupData gdTest;
	ArrayList<GroupUserData> usersTest;
	ArrayList<ChatListing> chatsTest;
	ChatListing cl1;
	ChatListing cl2;
	UserData u1;
	UserData u2;
	
	@BeforeEach
	void setUp() throws Exception
	{
		//initializing group
		gdTest = new GroupData();
		usersTest = new ArrayList<GroupUserData>();
		chatsTest = new ArrayList<ChatListing>();
		gdTest.Users = usersTest;
		gdTest.Chats = chatsTest;
				
		//initializing users and chats, i only did ID's because program is checking for ID's
		cl1 = new ChatListing();
		cl1.ChatID = 111;
		cl2 = new ChatListing();
		cl2.ChatID = 222;
		u1 = new UserData();
		u1.UserID = 999;
		u1.DisplayName = "user1";
		u2 = new UserData();
		u2.UserID = 888;
		u2.DisplayName = "user2";
				
		gdTest.addUser(u1);
		gdTest.addUser(u2);
		
	}

	@Test
	void test()
	{
		//testing chat functionality
		assertEquals(gdTest.addChat(cl1), 111);
		assertEquals(gdTest.addChat(cl2), 222);
		assertEquals(gdTest.deleteChat(222), true);
		assertEquals(gdTest.deleteChat(222), false);
				
		//testing search chat by id functionality
		assertEquals(gdTest.findChatByID(111), cl1);
		assertEquals(gdTest.findChatByID(100), null);
				
		//testing if add user to group worked
		assertEquals(gdTest.Users.get(0).Nickname, "user1");
		assertEquals(gdTest.Users.get(0).User, 999);
				
		//testing if remove user works
		GroupUserData uu1 = gdTest.Users.get(0);
		assertEquals(gdTest.removeUserFromGroup(uu1), true);
		assertEquals(gdTest.removeUserFromGroup(uu1), false);
				
		//testing groupUserData
		GroupUserData uu2 = gdTest.Users.get(0);
		assertEquals(gdTest.findGroupUserByID(888), uu2);
		assertEquals(gdTest.findGroupUserByID(4543), null);
	}

}