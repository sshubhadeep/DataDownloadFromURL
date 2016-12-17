package com.download.protocols;

import com.download.helper.ParseURLHelper;
import com.download.main.ConfigurationSingleton;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * The Class SftpProtocol.
 * 
 * this class uses Jsch library as added in referenced library
 */
public class SftpProtocol implements Protocol {

	/* (non-Javadoc)
	 * @see com.download.protocols.Protocol#downloadData(java.lang.String)
	 */
	@Override
	public void downloadData(String URL) {
		// TODO Auto-generated method stub
		
		//parse the url and get useful information
		//into local variables
		String sftpInfo[] = ParseURLHelper.getSFTPInfo(URL);
		
		String protocol=sftpInfo[0];
		String username=sftpInfo[1];
		String password=sftpInfo[2];
		String host=sftpInfo[3];
		int port=Integer.parseInt(sftpInfo[4]);
		String fileName=sftpInfo[5];
		
		JSch jsch = new JSch();
        Session session = null;
        try {
        	
        	//connect through a session to the host server
        	//using the parsed information from sftp url
            session = jsch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            //create a secure sftp channel
            //to download file from source to local destination
            Channel channel = session.openChannel(protocol);
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.get(fileName, ConfigurationSingleton
            		.getInstance().getFinalPath()+fileName);
            
            //close the channel and disconnect the session to
            //sftp host server
            sftpChannel.exit();
            session.disconnect();
            
          //notify once downloading is complete
            System.out.println("File downloaded through sftp");
        } catch (JSchException e) {
            e.printStackTrace();  
        } catch (SftpException e) {
            e.printStackTrace();
        }

	}

}
