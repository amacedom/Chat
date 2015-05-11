package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;

public interface TimeatServer extends Remote{
	 public String showTime() throws RemoteException;
	 public Calendar getTime() throws RemoteException;
}
