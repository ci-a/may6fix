package concord;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupDataRepoTest
{
	GroupDataRepo gdrTest;
	HashMap<Long,GroupData> hashTest;
	GroupData gd1;
	GroupData gd2;

	@BeforeEach
	void setUp() throws Exception
	{
		gdrTest = new GroupDataRepo();
		hashTest = new HashMap<Long,GroupData>();
		gd1 = new GroupData();
		gd2 = new GroupData();
		gdrTest.Groups = hashTest;
		
		gd1.GroupID = 1111;
		gd2.GroupID = 2222;
	}

	@Test
	void test()
	{
		gdrTest.addGroup(gd1);
		gdrTest.addGroup(gd2);
		assertEquals(gdrTest.findGroupByID(1111), gd1);
		gdrTest.deleteGroup(gd1);
		assertEquals(gdrTest.findGroupByID(1111), null);
	}

}