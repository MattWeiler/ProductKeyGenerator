package com.weilerhaus.productKeys.workers;

public interface BlacklistWorker
{
	
	
	/**
	 * This method will check if the specified product-key has been blacklisted.
	 * 
	 * @param productKey
	 * The product-key to check.
	 * @return
	 * The boolean flag to denote if the specified product-key has been blacklisted.
	 */
	boolean isKeyBlackListed(String productKey);
	
	/**
	 * This method will check if the specified product-key has been blacklisted.
	 * 
	 * @param seedHex
	 * The HEXADECIMAL format of the seed.
	 * @return
	 * The boolean flag to denote if the specified seed has been blacklisted.
	 */
	boolean isSeedBlackListed(String seedHex);
	
}
