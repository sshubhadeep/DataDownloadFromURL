package com.download.helper;

/**
 * The Class Utility.
 */
public class Utility {
	
	/**
	 * Gets the available memory.
	 *
	 * @return the available memory
	 */
	public static long getAvailableMemory(){
		
		long allocatedMemory = 
				  (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
			
		long presumableFreeMemory = Runtime.getRuntime().maxMemory() - allocatedMemory;
		
		return presumableFreeMemory;
	}

}
