package com.download.main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import com.download.protocols.*;

/**
 * The Class DownloadDemo.
 */
public class DownloadDemo {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//reading system configuration file 
		//and populate ConfigurationSingleton instance
		try {
			File file = new File("configuration.xml");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.loadFromXML(fileInput);
			fileInput.close();
			
			String tempPath=properties.getProperty("temp_loc");
			String finalPath=properties.getProperty("final_loc");
			
			ConfigurationSingleton.getInstance().setTempPath(tempPath);
			ConfigurationSingleton.getInstance().setFinalPath(finalPath);
		
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
			
		
		//reading the input file with test urls
		//and invoking the downloads of different protocols
		try {
			File file = new File("input.xml");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.loadFromXML(fileInput);
			fileInput.close();
			
			Enumeration<Object> enumKeys = properties.keys();
			while (enumKeys.hasMoreElements()) {
				String key = (String) enumKeys.nextElement();
				String url = properties.getProperty(key);
				Protocol protocol=ProtocolFactory.getProtocolInstance(key);
				Context context= new Context(protocol);
				context.download(url);
			}
		
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e){
			e.getMessage();
		}

	}

}
