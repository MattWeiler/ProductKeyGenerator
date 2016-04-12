package com.weilerhaus.productKeys.workers;

public interface HyphenWorker
{
	
	
	/**
	 * This method will add hyphens to the specified product-key.
	 * 
	 * @param productKey
	 * The product-key.
	 * @return
	 * The product-key with hyphens added.
	 */
	String addHyphens(String productKey);
	
}
