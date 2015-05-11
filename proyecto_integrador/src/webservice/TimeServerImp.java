package webservice;

import interfaces.TimeatServer;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 *
 * @author Vasanth Raja Chittampally
 */
public class TimeServerImp extends UnicastRemoteObject implements TimeatServer{
    public TimeServerImp() throws Exception {

    }

    public String showTime() throws RemoteException {
    Calendar calendar = new GregorianCalendar();
    String am_pm;
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    if(calendar.get(Calendar.AM_PM) == 0)
      am_pm = "AM";
    else
      am_pm = "PM";
    return "Current Time : " + hour + ":"
+ minute + ":" + second + " " + am_pm;
    }

    public Calendar getTime() throws RemoteException {
        Calendar calendar = new GregorianCalendar();
        return calendar;
    }
}
