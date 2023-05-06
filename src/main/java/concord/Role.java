package concord;
import java.util.ArrayList;


public class Role
{
	public String Name;
	ArrayList<Pair> Perms;
	
	public Role(String n, ArrayList<Pair> perms)
	{
		this.Name = n;
		this.Perms = perms;
	}
}