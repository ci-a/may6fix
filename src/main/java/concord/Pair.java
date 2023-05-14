
package concord;

public class Pair
{
	public String PermName;
	public boolean set;
	
	public Pair()
	{
		
	}
	
	public Pair(String PermName, boolean set)
	{
		this.PermName = PermName;
		this.set = set;
	}
	
	public String getName()
	{
		return PermName;
	}
	
	public boolean getSet()
	{
		return set;
	}
}