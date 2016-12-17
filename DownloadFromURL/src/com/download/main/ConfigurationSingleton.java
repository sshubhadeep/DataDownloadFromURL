package com.download.main;

/**
 * The Class ConfigurationSingleton.
 */
public class ConfigurationSingleton {
	
	/** The instance. */
	private static ConfigurationSingleton instance = null;
	
	/** The temp path. */
	private String tempPath;
	
	/** The final path. */
	private String finalPath;
	   
   	/**
   	 * Gets the temp path.
   	 *
   	 * @return the temp path
   	 */
   	public String getTempPath() {
		return tempPath;
	}

	/**
	 * Sets the temp path.
	 *
	 * @param tempPath the new temp path
	 */
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	/**
	 * Gets the final path.
	 *
	 * @return the final path
	 */
	public String getFinalPath() {
		return finalPath;
	}

	/**
	 * Sets the final path.
	 *
	 * @param finalPath the new final path
	 */
	public void setFinalPath(String finalPath) {
		this.finalPath = finalPath;
	}

	/**
	 * Instantiates a new configuration singleton.
	 */
	private ConfigurationSingleton() {
	      
	}

	/**
	 * Gets the single instance of ConfigurationSingleton.
	 *
	 * @return single instance of ConfigurationSingleton
	 */
	public static ConfigurationSingleton getInstance() {
	    if(instance == null) {
	         instance = new ConfigurationSingleton();
	    }
	    return instance;
	}

}
