package com.weilerhaus.productKeys.workers;

public interface ChecksumWorker
{
	
	
	/**
	 * This method will build the checksum section of the product-key.
	 * 
	 * @param productKey
	 * The product-key, everything up-to the checksum.
	 * @return
	 * The checksum section of the product-key.
	 */
	String buildProductKeyChecksum(String productKey);
	
	/**
	 * This method will verify if the checksum from the specified product-key is correct.
	 * 
	 * @param productKey
	 * The product-key to verify.
	 * @return
	 * The boolean flag to denote if the checksum from the specified product-key is correct.
	 */
	boolean verifyProductKeyChecksum(String productKey);
	
}
