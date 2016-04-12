package com.weilerhaus.productKeys.impl;

import com.weilerhaus.productKeys.ProductKeyGenerator;
import com.weilerhaus.productKeys.beans.ProductKeyEncodingData;
import com.weilerhaus.productKeys.impl.beans.BasicProductKeyEncodingData;
import com.weilerhaus.productKeys.impl.workers.BasicChecksumWorker;
import com.weilerhaus.productKeys.impl.workers.BasicProductKeySectionWorker;
import com.weilerhaus.productKeys.impl.workers.BasicProductKeyStylingWorker;
import com.weilerhaus.productKeys.workers.BlacklistWorker;
import com.weilerhaus.productKeys.workers.ChecksumWorker;
import com.weilerhaus.productKeys.workers.ProductKeySectionWorker;
import com.weilerhaus.productKeys.workers.ProductKeyStylingWorker;
import com.weilerhaus.productKeys.workers.SeedAvailabilityWorker;

/**
 * This class can be used to generate basic product-keys.
 * <br />
 * <br />
 * The bytes used to create the key sections must be passed-in which will make the keys application
 * specific.
 * 
 * @author Matthew Weiler
 */
public class BasicProductKeyGenerator extends ProductKeyGenerator<BasicProductKeyEncodingData>
{
	
	
	/* CONSTRUCTORS */
	/**
	 * This will create a new instance of a {@link BasicProductKeyGenerator}.
	 * 
	 * @param productKeyEncodingData
	 * The array of {@link ProductKeyEncodingData} elements that will be used to generate/verify
	 * product-keys.
	 * <br />
	 * <i>
	 * If a {@link ProductKeyEncodingData} element is <code>null</code>, that section of the
	 * product-key will not be validated.
	 * <br />
	 * However, it should be noted that the encoding will only work if all
	 * {@link ProductKeyEncodingData} elements are present.
	 * <br />
	 * <br />
	 * When including this in the client application code, omit some {@link ProductKeyEncodingData}
	 * elements by setting them to <code>null</code>.
	 * <br />
	 * This will ensure that the application code running on the clients device never has the entire
	 * logic for creating a product-key; if reverse engineered, they will only be able to create a
	 * product-key that will work on that build/version of your application.
	 * <br />
	 * Each new build/version of your client application should/could change the omitted
	 * {@link ProductKeyEncodingData} elements thus ensuring that if a previously cracked key
	 * worked, it won't in the new build/version.
	 * </i>
	 */
	public BasicProductKeyGenerator(final BasicProductKeyEncodingData...productKeyEncodingData)
	{
		super(8, productKeyEncodingData);
	}
	
	/* PROTECTED METHDOS */
	@Override
	protected ProductKeySectionWorker<BasicProductKeyEncodingData> buildProductKeySectionWorker()
	{
		return new BasicProductKeySectionWorker();
	}
	
	@Override
	protected ChecksumWorker buildChecksumWorker()
	{
		return new BasicChecksumWorker();
	}
	
	@Override
	protected BlacklistWorker buildBlacklistWorker()
	{
		return null;
	}
	
	@Override
	protected ProductKeyStylingWorker buildProductKeyStylingWorker()
	{
		return new BasicProductKeyStylingWorker();
	}
	
	@Override
	protected SeedAvailabilityWorker buildSeedAvailabilityWorker()
	{
		return null;
	}
	
}
