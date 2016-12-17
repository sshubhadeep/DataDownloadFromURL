package com.download.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The Class FileHelper.
 */
public class FileHelper {
	
	/**
	 * Gets the local file size.
	 *
	 * @param path the local path of the file
	 * @return the local file size
	 */
	public static long getLocalFileSize(String path){
		
		File file = new File(path);
		
		//if file exists then return it's size else return -1
		if(file.exists()){
			return file.length();
		}
		return -1;
	}
	
	/**
	 * Gets the file size from http url.
	 *
	 * @param stringUrl the string url
	 * @return the file size from http url
	 */
	public static long getFileSizeFromHttpURL(String stringUrl) {
		
		URL url;
	    HttpURLConnection conn = null;
	    //return the file size from content length using "HEAD" request, otherwise return -1
	    try {
	    	url = new URL(stringUrl);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("HEAD");
	        conn.getInputStream();
	        return conn.getContentLength();
	    } catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return -1;
		} catch (IOException e) {
	        return -1;
	    } finally {
	    //    conn.disconnect();
	    }
	}
	
	/**
	 * Delete file.
	 *
	 * @param path the path
	 */
	public static void deleteFile(String path){
		Path fileToDeletePath = Paths.get(path);
		
		//Delete the file as specified in the path else it will inform to delete the file manually
		try {
			Files.delete(fileToDeletePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println("Delete file manualy at " + path);
		}
	}
	
	/**
	 * Copy file using stream.
	 *
	 * @param sourcePath the source path
	 * @param destPath the dest path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copyFileUsingStream(String sourcePath, String destPath) throws IOException {
	    File source = new File(sourcePath);
		File dest = new File(destPath);
		InputStream is = null;
	    OutputStream os = null;
	    
	    //file copy using streams (the fastest file copy mechanism)
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
	/**
	 * Merge files.
	 *
	 * @param dest the destination file
	 * @param list the list
	 */
	public static void mergeFiles(File dest,ArrayList<File> list){
		//if the final destination file exists delete it
		if(dest.exists())
			dest.delete();
        FileOutputStream fos;
        FileInputStream fis;
        byte[] fileBytes;
        int bytesRead = 0;
        try {
        				//open output stream to the destination file in append mode
        				//and add all part file data one by one from the file list
        	            fos = new FileOutputStream(dest,true);
        	            for (File file : list) {
        	                fis = new FileInputStream(file);
        	                fileBytes = new byte[(int) file.length()];
        	                bytesRead = fis.read(fileBytes, 0,(int)  file.length());
        	                assert(bytesRead == fileBytes.length);
        	                assert(bytesRead == (int) file.length());
        	                fos.write(fileBytes);
        	                fos.flush();
        	                fileBytes = null;
        	                fis.close();
        	                fis = null;
        	            }
        	            fos.close();
        	            fos = null;
        	        }catch (Exception exception){
        	            exception.printStackTrace();
        	        }
	}
	
	/**
	 * Read part from http url.
	 *
	 * @param url the url
	 * @param startByte the start byte
	 * @param endByte the end byte
	 * @param filePath the file path
	 * @param list the list of files
	 */
	public static void readPartFromHttpURL(URL url, int startByte, int endByte, String filePath,ArrayList<File> list){
		//read the file from startByte to endByte
		//write it to a new part file
		//add the file to the file list
		try{
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();	
	        urlConnection.setRequestProperty("Range", "Bytes="+startByte+"-"+endByte);
	        urlConnection.connect();
	               
	        InputStream inputStream = urlConnection.getInputStream();
	        FileOutputStream outputStream = new FileOutputStream(filePath);
	        
	        byte[] buffer = new byte[2096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
	        
	        urlConnection.disconnect();
	        
	        File file=new File(filePath);
	        list.add(file);
		}catch(MalformedURLException mue) {
	        mue.printStackTrace();
	    }catch(IOException ioe) {
	        ioe.printStackTrace();
	    }
	}

}
