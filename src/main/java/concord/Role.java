package concord;
import java.util.ArrayList;


public class Role
{
	public String Name;
	public ArrayList<Pair> Perms;
	
	public Role()
	{
		
	}
	
	public Role(String n, ArrayList<Pair> perms)
	{
		this.Name = n;
		this.Perms = perms;
	}
}