package concord;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDataRepoTest
{
	UserData elvis;
	UserData dummy;
	ArrayList<Long> elvisList;
	ArrayList<Long> dummyList;
	UserDataRepo testUserRepo;
	HashMap<Long,UserData> test;
	HashMap<Long,UserData> testNoDummy;
	
	@BeforeEach
	void setUp() throws Exception
	{
		long a = 0;
		long b = 1;
		long c = 2;
		elvisList = new ArrayList<Long>();
		elvisList.add(a);
		elvisList.add(b);
		elvisList.add(c);
		elvis = new UserData();
		elvis.UserID = 909;
		elvis.JoinedGroupIDs = elvisList;
		elvis.Status = 1;
		elvis.DisplayName = "beans";
		elvis.Email = "elvis@email.com";
		elvis.Password = "securePassword123";
		
		dummyList = new ArrayList<Long>();
		dummyList.add(b);
		dummy = new UserData();
		dummy.UserID = 101;
		dummy.JoinedGroupIDs = dummyList;
		dummy.Status = 0;
		dummy.DisplayName = "bot";
		dummy.Email = "fake@email.com";
		dummy.Password = "passwordlol";
		
		testUserRepo = new UserDataRepo();
		test = new HashMap<Long,UserData>();
		testNoDummy = new HashMap<Long,UserData>();
	}

	@Test
	void test()
	{
		//test to see if data is saved
		assertEquals(elvis.UserID, 909);
		assertEquals(elvis.JoinedGroupIDs, elvisList);
		assertEquals(elvis.Status, 1);
		assertEquals(elvis.DisplayName, "beans");
		assertEquals(elvis.Email, "elvis@email.com");
		assertEquals(elvis.Password, "securePassword123");
				
		//test to see if add user works
		testUserRepo.addUser(elvis);
		testUserRepo.addUser(dummy);
		test.put(elvis.UserID, elvis);
		test.put(dummy.UserID, dummy);
		testNoDummy.put(elvis.UserID, elvis);
				
		//test to see if remove user works
		assertEquals(testUserRepo.Users, test);
		assertEquals(testUserRepo.findUserByName("bot"), dummy);
		testUserRepo.deleteUser(dummy);
		assertEquals(testUserRepo.findUserByName("bot"), null);
		assertEquals(testUserRepo.Users, testNoDummy);
				
		//test to see if find user by ID works
		assertEquals(testUserRepo.findUserByID(909), elvis);
		
		//test find user by name
		assertEquals(testUserRepo.findUserByName("beans"), elvis);
		assertEquals(testUserRepo.findUserByName("nonExistent"), null);
	}

}