package com.download.protocols;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.download.helper.FileHelper;
import com.download.helper.ParseURLHelper;
import com.download.helper.Utility;
import com.download.main.ConfigurationSingleton;


/**
 * The Class HttpProtocol.
 */
public class HttpProtocol implements Protocol {
	
	/* (non-Javadoc)
	 * @see com.download.protocols.Protocol#downloadData(java.lang.String)
	 */
	@Override
	public void downloadData(String URL) {
		// TODO Auto-generated method stub
		
		boolean downloaded = false;
		
		//read external file size using FileHelper helper class
		long fileSizeInServer=FileHelper.getFileSizeFromHttpURL(URL);
		
		//read available free memory using Utility helper class
		long availableFreeMemory=Utility.getAvailableMemory();
		
		//get useful information from http url using
		//ParseURLHelper helper class
		String[] httpInfo=ParseURLHelper.getHTTPInfo(URL);
		
		//get the correct file name from parsed url
		String fileName=httpInfo[1];
		
		//get paths from system configuration
		String tempLocalFilePath = ConfigurationSingleton
				.getInstance().getTempPath()+fileName;
		
		String finalLocalFilePath = ConfigurationSingleton
				.getInstance().getFinalPath()+fileName;
		
		
		//when external file has some content
		//it will be downloaded as a regular file 
		//or as a large file based on its size
		//where large files are files having size 
		//greater than available memory
		if(fileSizeInServer != -1){
		
			if(fileSizeInServer<availableFreeMemory){
				
				try {
			            downloadUsingStream(URL, tempLocalFilePath);
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			}else{
		
				try {
		            downloadLargeFileUsingStream(URL, tempLocalFilePath);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			}
			
			//check the downloaded file size using FileHelper helper class
			//to determine if the file is fully downloaded
			long fileSizeInDisk=FileHelper.getLocalFileSize(tempLocalFilePath);
			if(fileSizeInDisk != -1)
				if(fileSizeInDisk==fileSizeInServer)
					downloaded = true;
		}
		
		//if the file is fully downloaded, save it to the final location
		//and remove it from the temporary location
		//and sends appropriate message to console
		if(downloaded == true){
			try {
				FileHelper.copyFileUsingStream(tempLocalFilePath,finalLocalFilePath);
				FileHelper.deleteFile(tempLocalFilePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("File downloaded through http");
		}else
			System.out.println("Unable to download file through http");
		
	}
	
	/**
	 * Download using stream.
	 *
	 * @param downloadUrl the download url
	 * @param downloadPath the download path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void downloadUsingStream(String downloadUrl, String downloadPath) throws IOException{
        
		long downloadedLength = 0;

	    File file = new File(downloadPath);
	    	    
	    //open connection for the given download url
	    URL url = new URL(downloadUrl);
	    URLConnection connection = url.openConnection();

	    BufferedInputStream inputStream = null;
	    BufferedOutputStream outputStream = null;

	    //if destination file exists due to any interruption during previous download
	    //it will resume download from the point it got interrupted
	    //and will append rest of the data to the same file
	    if(file.exists()){
	        downloadedLength = file.length();
	        
	        //skipping the data which is already downloaded
	        //and open a stream to write on the same file in append mode
	        connection.setRequestProperty("Range", "bytes=" + downloadedLength + "-");
	        outputStream = new BufferedOutputStream(new FileOutputStream(file, true));

	    }else{
	    	//create stream to write on the destination file from beginning
	        outputStream = new BufferedOutputStream(new FileOutputStream(file));

	    }

	    connection.connect();

	    inputStream = new BufferedInputStream(connection.getInputStream());


	    byte[] buffer = new byte[1024];
	    int byteCount;
	    
	    //reading from external file and writing to local file
	    while ((byteCount = inputStream.read(buffer)) != -1){
	        outputStream.write(buffer, 0, byteCount);
	    }

	    //closing and clearing the streams
	    inputStream.close();
	    outputStream.flush();
	    outputStream.close();
    }
	
	/**
	 * Download large file using stream.
	 *
	 * @param downloadUrl the download url
	 * @param downloadPath the download path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void downloadLargeFileUsingStream(String downloadUrl, String downloadPath) throws IOException{
		// multi-part download
		
		//creating an array list to store the list of partial downloaded files
		ArrayList<File> list= new ArrayList<File>();
		
		//setting maximum bytes of data that can be saved to a partial file
		final int SECTION_SIZE=1024;
		
		try {
			
			URL url=new URL(downloadUrl);
	        
			//get the size of external file
			long fileSize = FileHelper.getFileSizeFromHttpURL(downloadUrl);
	        
			//define how many part file needed(part count+1)
			//and determine the size of last part file
	        int part= (int)fileSize/SECTION_SIZE;
	        int lastPartSize=(int)fileSize%SECTION_SIZE;
	        
	        //set initial begin and end bytes for first part file
	        int startByte=0;
	        int endByte=SECTION_SIZE-1;
	        int i;
	        for(i=0;i<part;i++){
	        	//read the part file,save it and add it to the file list
	        	//update the begin and end bytes for the next part file
	        	FileHelper.readPartFromHttpURL(url, startByte, endByte,downloadPath+".part"+i,list);
	        	startByte+=SECTION_SIZE;
	        	endByte+=SECTION_SIZE;
	        }
	        
	        //adding the last chunk of bytes to the last part file
	        endByte+=(lastPartSize-SECTION_SIZE);
	        FileHelper.readPartFromHttpURL(url, startByte, endByte,downloadPath+".part"+i,list);
	        
	        //merge all the part file to the target file
	        FileHelper.mergeFiles(new File(downloadPath), list);

	    }catch(MalformedURLException mue) {
	        mue.printStackTrace();
	    }
		
	}

}
