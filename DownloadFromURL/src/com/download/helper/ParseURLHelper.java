package com.download.helper;

/**
 * The Class ParseURLHelper.
 */
public class ParseURLHelper {
	
	/**
	 * Gets the SFTP info.
	 *
	 * @param url the url
	 * @return the SFTP info array with list of useful information
	 */
	public static String[] getSFTPInfo(String url){
		
		String[] sftpInfo = new String[6];
		
		String [] sa=url.split(":");
		
		sftpInfo[0]=sa[0];
		
		String username = sa[1];
		
		while(username.startsWith("/")){
			username=username.substring(1, username.length());
		}
		
		sftpInfo[1]=username;
		
		String [] part1URL = sa[2].split("@");
		
		String password = part1URL[0];
		String host = part1URL[1];
		
		sftpInfo[2]=password;
		sftpInfo[3]=host;
		
		String [] part2URL=sa[3].split("/");
		
		String port;
		if(part2URL[0].length()==0)
			port="22";
		else
			port=part2URL[0];
		
		sftpInfo[4]=port;
		
		String [] file=part2URL[1].split(";");
		
		String fileName=file[0];
		
		sftpInfo[5]=fileName;
		
		return sftpInfo;
		
	}
	
	
	/**
	 * Gets the HTTP info.
	 *
	 * @param url the url
	 * @return the HTTP info array with list of useful information
	 */
	public static String[] getHTTPInfo(String url){
		String[] httpInfo=new String[2];
		
		httpInfo[0]="http";
		
		String[] part1URL=url.split("/");
		
		String[] file = part1URL[part1URL.length-1].split(";");
		
		String fileName= file[0];
		
		httpInfo[1]= fileName;
		
		return httpInfo;
	}
	
	/**
	 * Gets the FTP info.
	 *
	 * @param url the url
	 * @return the FTP info array with list of useful information
	 */
	public static String[] getFTPInfo(String url){
		String[] ftpInfo=new String[2];
		
		ftpInfo[0]="ftp";
		
		String[] part1URL=url.split("/");
		
		String[] file = part1URL[part1URL.length-1].split(";");
		
		String fileName= file[0];
		
		ftpInfo[1]= fileName;
		
		return ftpInfo;
	}

}
