package concord;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupUserDataTest
{
	GroupUserData guTest;
	ArrayList<Role> toAdd;
	ArrayList<Pair> permList;
	
	@BeforeEach
	void setUp() throws Exception
	{
		//creation of an admin role with all capabilities
		Pair kick = new Pair("kick", true);
				
		Pair ban = new Pair("ban", true);
				
		Pair texts = new Pair("controlUserMessaging", true);
			
		Pair groupName = new Pair("changeGroupName", true);
				
		Pair changePerms = new Pair("changeUsersPerms", true);
				
		permList = new ArrayList<Pair>();
		permList.add(kick);
		permList.add(ban);
		permList.add(texts);
		permList.add(groupName);
		permList.add(changePerms);
		Role testRole = new Role("admin", permList);
				
				
				
		//adding admin role into array of roles for user 101
		guTest = new GroupUserData();
		toAdd = new ArrayList<Role>();
		toAdd.add(testRole);
		guTest.User = 101;
		guTest.Nickname = "bot";
		guTest.Roles = toAdd;
	}

	@Test
	void test()
	{
		//testing to see if perms can be found by checkPerm
		assertEquals(guTest.checkPerm("changeUsersPerms"), true);
		assertEquals(guTest.checkPerm("ban"), true);
		assertEquals(guTest.checkPerm("kick"), true);
				
		//hackUser doesn't exist so it returns false if perm doesn't exist.
		assertEquals(guTest.checkPerm("hackUser"), false);
		assertEquals(guTest.checkPerm("fakePermission"), false);
	}

}