package com.weilerhaus.productKeys.workers;

public interface SeedAvailabilityWorker
{
	
	
	/**
	 * This method will determine if the HEXADECIMAL seed is not currently being used by another
	 * key.
	 * 
	 * @param seedHex
	 * The seed in HEXADECIMAL form.
	 * @return
	 * The boolean flag to denote if the HEXADECIMAL seed is not currently being used by another
	 * key.
	 */
	boolean isSeedAvailable(String seedHex);
	
}
