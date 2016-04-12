package com.weilerhaus.productKeys.workers;

public interface ProductKeyStylingWorker
{
	
	
	/**
	 * This method will add styling to the specified product-key.
	 * 
	 * @param productKey
	 * The product-key.
	 * @return
	 * The product-key with styling added.
	 */
	String addStyling(String productKey);
	
	/**
	 * This method will remove styling to the specified product-key.
	 * 
	 * @param productKey
	 * The product-key.
	 * @return
	 * The product-key with styling removed.
	 */
	String removeStyling(String productKey);
	
}
