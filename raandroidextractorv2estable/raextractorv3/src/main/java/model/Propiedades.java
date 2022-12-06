package main.java.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Propiedades {
	
	public Propiedades() {
		
	}

	public static String getPropiedades(String prop) throws IOException
	{
		Process p = Runtime.getRuntime().exec("adb shell getprop " + prop);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuffer stringBuffer = new StringBuffer(); 
		String s;
		s = stdInput.readLine();
		
		while(s!=null)
		{
			stringBuffer.append(s);
			//stringBuffer.append("\n");
			s = stdInput.readLine();
		}
		s = stringBuffer.toString();
		return s;
	}
}
