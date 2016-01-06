package net.ericcson.iotaas;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Activity;
import android.os.Bundle;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		try {
			String url="http://129.192.210.50:38080/rest/utilityData/meterReadings";
			URL object=new URL(url);
			AttempReadData(object,"16714748");
		} catch (Exception e) {
		}

	}

	public boolean AttempReadData(URL object,String objname)
	{

		try {
			HttpURLConnection con = (HttpURLConnection) object.openConnection();

			con.setDoOutput(true);
			con.setDoInput(true);
			con.setInstanceFollowRedirects(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/xml");
			//			con.setDoInput(true);
			String userpass ="utilityService1:test";
			//String userpass ="utilityService1:test";
			String basicAuth = "Basic " + new String(new org.apache.commons.codec.binary.Base64().encode(userpass.getBytes()));
			con.setRequestProperty ("Authorization", basicAuth);

			con.setRequestProperty("X-ECE-PARTNER-ID", "Utility1");
			con.setRequestProperty("X-ECE-SERVICE-ID", "utilityService1");
			con.setRequestProperty("ECE-Suppress-AA", "true");

			OutputStream out = con.getOutputStream();
			OutputStreamWriter wout = new OutputStreamWriter(out, "UTF-8");

			wout.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><tns:CreateMeterReadings xmlns:tns=\"http://iec.ch/TC57/2011/MeterReadingsMessage\" xmlns:msg=\"http://iec.ch/TC57/2011/schema/message\" xmlns:obj=\"http://iec.ch/TC57/2011/MeterReadings#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://iec.ch/TC57/2011/MeterReadingsMessage ../xsd/MeterReadingsMessage.xsd\"><tns:Header><msg:Verb>create</msg:Verb><msg:Noun>MeterReadings</msg:Noun><msg:Revision>0.1</msg:Revision><msg:Timestamp>2014-12-17T09:30:41.0Z</msg:Timestamp><msg:ReplyAddress>http://129.192.210.36:8080/Thingworx/Things/IntegrationMasterThing/Services/handleDataPull?postParameter=body</msg:ReplyAddress><msg:CorrelationID>123123</msg:CorrelationID></tns:Header><tns:Payload><obj:MeterReadings><obj:MeterReading><obj:Meter><obj:Names><obj:name>"+objname+"</obj:name></obj:Names></obj:Meter></obj:MeterReading><obj:ReadingType><obj:Names><obj:name>0.0.0.1.1.1.12.0.0.0.0.0.0.0.0.3.72.0</obj:name></obj:Names></obj:ReadingType></obj:MeterReadings></tns:Payload></tns:CreateMeterReadings>");

			wout.flush();
			out.close();

			String result=readInputStreamToString(con);
			System.out.println(result);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	static public String readInputStreamToString(HttpURLConnection connection) {
		String result = null;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;

		try {
			is = new BufferedInputStream(connection.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String inputLine = "";
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			result = sb.toString();
		}
		catch (Exception e) {
			result = null;
		}
		finally {
			if (is != null) {
				try { 
					is.close(); 
				} 
				catch (IOException e) {
				}
			}   
		}

		return result;
	}
}
