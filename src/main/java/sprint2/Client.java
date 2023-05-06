package sprint2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import concord.GroupData;

public interface Client extends Remote
{
	public void processUpdate(GroupData Update) throws RemoteException;
}