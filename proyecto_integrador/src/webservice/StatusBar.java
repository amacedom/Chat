package webservice;

import interfaces.TimeatServer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JLabel;

public class StatusBar implements Runnable  {
	Calendar time;
	JLabel timeLabel;
	Thread clock;
	String url;
	TimeatServer ts;
	
	public StatusBar() throws NotBoundException {
		this.url = "rmi://45.55.251.74/time";
		try {
			this.ts = (TimeatServer) Naming.lookup(url);
			this.time = ts.getTime();
			this.timeLabel = new JLabel("hi");
			this.clock = new Thread(this);
			clock.start();
		} catch (MalformedURLException | RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while( true ) {
			String hour = String.valueOf(time.get(Calendar.HOUR));
			String min = String.valueOf(time.get(Calendar.MINUTE));
			String sec = String.valueOf(time.get(Calendar.SECOND));
			System.out.println(hour + ":" + min + ":" + sec);
			this.timeLabel.setText(hour + ":" + min + ":" + sec);
		}
	}

}
