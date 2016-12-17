package com.download.protocols;

/**
 * A factory for creating Protocol objects.
 */
public class ProtocolFactory {
	
	/**
	 * Gets the protocol instance.
	 *
	 * @param protocol the protocol
	 * @return the protocol instance
	 * @throws Exception for invalid protocol name 
	 */
	public static Protocol getProtocolInstance(String protocol) throws Exception{
		if(protocol.equals("http"))
			return new HttpProtocol();
		else if (protocol.equals("ftp"))
			return new FtpProtocol();
		else if (protocol.equals("sftp"))
			return new SftpProtocol();
		else
			throw new Exception("Invalid Protocol Used");
			
	}

}
