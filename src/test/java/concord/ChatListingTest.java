package concord;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatListingTest
{
	ChatListing clTest;
	MsgData msgA;
	MsgData msgB;
	ArrayList<MsgData> chatList;
	
	@BeforeEach
	void setUp() throws Exception
	{
		//creating synthetic conversation, used little HasA's because just testing functions
		clTest = new ChatListing();
		chatList = new ArrayList<MsgData>();
		clTest.Chat = chatList;
		msgA = new MsgData();
		msgB = new MsgData();
		msgA.MsgIndex = 8181;
		msgB.MsgIndex = 1818;
		msgA.deleted = false;
		msgB.deleted = false;
		msgA.Text = "hi b!";
		msgB.Text = "hello there a!";
	}

	@Test
	void test()
	{
		//testing to see if messages can be added and removed from chatlisting
		assertEquals(clTest.addMsg(msgA), 8181);
		assertEquals(clTest.addMsg(msgB), 1818);
		assertEquals(clTest.deleteMsg(8181), true);
				
		//testing to see if delete will fail if msg already deleted
		assertEquals(clTest.deleteMsg(8181), false);
				
		//test for finding msg by ID
		assertEquals(clTest.findMsgByID(1818), msgB);
		assertEquals(clTest.findMsgByID(100), null);
	}

}
