package com.weilerhaus.productKeys.workers;

import com.weilerhaus.productKeys.beans.ProductKeyEncodingData;

public interface ProductKeySectionWorker<ED extends ProductKeyEncodingData>
{
	
	
	/**
	 * This method will build a product-key section.
	 * 
	 * @param seed
	 * The seed that is being used by this product-key.
	 * @param productKeyEncodingDate
	 * The {@link ProductKeyEncodingData} to be used for encoding.
	 * @return
	 * The product-key section.
	 */
	byte buildProductKeySection(final long seed, ED productKeyEncodingDate);
	
}
