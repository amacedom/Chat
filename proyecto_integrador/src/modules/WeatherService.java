package modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import jdk.nashorn.internal.parser.JSONParser;

import com.mysql.jdbc.Connection;

import twitter4j.JSONException;
import twitter4j.JSONObject;
import ui.UserInterface;

public class WeatherService extends UserInterface {
	String ipString;
	URL url;
	HttpURLConnection conn;
	JSONObject json;
	
	public WeatherService() {
		this.ipString = getIp();
		this.url = getURL();
		try {
			this.conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			this.json = getWeatherJSON();
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private JSONObject getWeatherJSON() throws JSONException {
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = conn.getInputStream();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		int status = -1;
		try {
			status = conn.getResponseCode();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (status != 200) {
		    return null;
		}

		reader = new BufferedReader(new InputStreamReader(is));
		JSONObject json = new JSONObject();
		
		try {
			for (String line; (line = reader.readLine()) != null;) {
			    //this API call will return something like:
			    System.out.println(line);
			    json = new JSONObject(line);
			    
			    // you can extract whatever you want from it
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	private URL getURL() {
		URL url = null;
		try {
			url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Guadalajara,mx");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	
	private String getIp(){
		String ipString = "no address";
		try {
			ipString = InetAddress.getLocalHost().getHostAddress();
			System.out.println(ipString);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ipString;
	}
	
}
