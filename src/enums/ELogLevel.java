package enums;



public enum ELogLevel {
	
	/**
	 * Specific methods logs
	 */
	debug,
	
	/**
	 * General messages of start / end of flow
	 */
	verbose,
	
	/**
	 * Warning level will be used for errors that are not stopping the program. 
	 */
	warning,
	
	/**
	 * In case of an exception / unexpected behavior
	 */
	error,
	
	/**
	 * Exception that can lead to program termination
	 */
	critical,
}
