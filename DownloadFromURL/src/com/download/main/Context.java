package com.download.main;

import com.download.protocols.Protocol;

/**
 * The Class Context.
 */
public class Context {
	
	/** The protocol. */
	private Protocol protocol;
	
	/**
	 * Instantiates a new context.
	 *
	 * @param protocol the protocol
	 */
	public Context(Protocol protocol){
		this.protocol=protocol;
	}
	
	/**
	 * Download.
	 *
	 * @param URL the source url to download file
	 */
	public void download(String URL){
		protocol.downloadData(URL);
	}

}
