package com.download.protocols;

import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.net.URL ;
import java.net.URLConnection ;

import com.download.helper.ParseURLHelper;
import com.download.main.ConfigurationSingleton;


public class FtpProtocol implements Protocol {
	
	private static final int BUFFER_SIZE = 4096 ;

	@Override
	public void downloadData(String URL) {
		// TODO Auto-generated method stub
		
		//read useful information from url
		String[] ftpInfo=ParseURLHelper.getFTPInfo(URL);
		
		String fileName=ftpInfo[1];
				
		try {
			
			//create connection and open stream to read from source location
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            InputStream inputStream = conn.getInputStream();
 
            //create stream to write to destination file location
            FileOutputStream outputStream = new FileOutputStream(ConfigurationSingleton
            		.getInstance().getFinalPath()+fileName);
 
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            
            //reading from external file and writing to local file
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            //closing streams
            outputStream.close();
            inputStream.close();
 
            //notify once downloading is complete
            System.out.println("File downloaded through ftp");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

	}

}
