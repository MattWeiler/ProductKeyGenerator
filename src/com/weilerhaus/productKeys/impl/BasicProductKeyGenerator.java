package com.weilerhaus.productKeys.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.weilerhaus.productKeys.ProductKeyGenerator;
import com.weilerhaus.productKeys.beans.ProductKeyEncodingData;
import com.weilerhaus.productKeys.enums.ProductKeyState;
import com.weilerhaus.productKeys.impl.beans.BasicProductKeyEncodingData;
import com.weilerhaus.productKeys.impl.workers.BasicChecksumWorker;
import com.weilerhaus.productKeys.impl.workers.BasicHyphenWorker;
import com.weilerhaus.productKeys.impl.workers.BasicProductKeySectionWorker;
import com.weilerhaus.productKeys.workers.BlacklistWorker;
import com.weilerhaus.productKeys.workers.ChecksumWorker;
import com.weilerhaus.productKeys.workers.HyphenWorker;
import com.weilerhaus.productKeys.workers.ProductKeySectionWorker;
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
	
	
	public static void main(final String[] args)
	{
		String tmpKey;
		ProductKeyState tmpGeneratedKeyState;
		final List<String> generatedProductKeys = new ArrayList<String>();
		
		// @formatter:off
		ProductKeyGenerator<BasicProductKeyEncodingData> productKeyGenerator = new BasicProductKeyGenerator(
				new BasicProductKeyEncodingData((byte) 24, (byte) 3, (byte) 101),
				new BasicProductKeyEncodingData((byte) 10, (byte) 4, (byte) 56),
				new BasicProductKeyEncodingData((byte) 1, (byte) 2, (byte) 91),
				new BasicProductKeyEncodingData((byte) 7, (byte) 1, (byte) 100),
				new BasicProductKeyEncodingData((byte) 2, (byte) 36, (byte) 45),
				new BasicProductKeyEncodingData((byte) 13, (byte) 5, (byte) 54),
				new BasicProductKeyEncodingData((byte) 21, (byte) 67, (byte) 25),
				new BasicProductKeyEncodingData((byte) 3, (byte) 76, (byte) 12),
				new BasicProductKeyEncodingData((byte) 31, (byte) 22, (byte) 34),
				new BasicProductKeyEncodingData((byte) 15, (byte) 72, (byte) 65)
		);
		// @formatter:on
		
		int tmpTryCount;
		
		System.out.println("**** BUILDING KEYS ****");
		
		final StringBuilder tmpMaxSeedHexSb = new StringBuilder();
		
		for (int n = 0; n < productKeyGenerator.getSeedCharLength(); n++ )
		{
			tmpMaxSeedHexSb.append('F');
		}
		
		final Random randomGenerator = new Random(System.nanoTime());
		
		for (int n = 0; n < 25; n++ )
		{
			tmpKey = null;
			tmpTryCount = 0;
			
			while ((tmpKey == null) && (tmpTryCount < 10))
			{
				try
				{
					tmpKey = productKeyGenerator.generateProductKey(randomGenerator.nextLong());
				}
				catch (Exception e)
				{
					
				}
				
				tmpTryCount++ ;
			}
			
			if ((tmpKey != null) && (tmpKey.trim().length() > 0))
			{
				tmpGeneratedKeyState = productKeyGenerator.verifyProductKey(tmpKey);
				
				if (ProductKeyState.KEY_GOOD == tmpGeneratedKeyState)
				{
					generatedProductKeys.add(tmpKey);
					
					System.out.println((n + 1) + ") " + ((n < 9) ? " " : "") + "Product Key (GOOD): " + tmpKey);
				}
				else
				{
					System.out.println((n + 1) + ")  " + ((n < 9) ? " " : "") + "Product Key (BAD): " + tmpKey + ":  Key State: " + tmpGeneratedKeyState.name());
				}
			}
			else
			{
				System.out.println("Failed to generate product keys.");
				
				break;
			}
		}
		
		System.out.println();
		System.out.println();
		System.out.println("**** VERIFYING KEYS ****");
		
		// @formatter:off
		BasicProductKeyGenerator basicProductKeyGenerator = new BasicProductKeyGenerator(
			new BasicProductKeyEncodingData((byte) 24, (byte) 3, (byte) 101),
			null,
			new BasicProductKeyEncodingData((byte) 1, (byte) 2, (byte) 91),
			new BasicProductKeyEncodingData((byte) 7, (byte) 1, (byte) 100),
			null,
			null,
			new BasicProductKeyEncodingData((byte) 21, (byte) 67, (byte) 25),
			null,
			new BasicProductKeyEncodingData((byte) 31, (byte) 22, (byte) 34),
			null
		);
		// @formatter:on
		
		int keyIndex = 0;
		
		for (String productKey : generatedProductKeys)
		{
			if ((productKey != null) && (productKey.trim().length() > 0))
			{
				tmpGeneratedKeyState = basicProductKeyGenerator.verifyProductKey(productKey);
				
				if (ProductKeyState.KEY_GOOD == tmpGeneratedKeyState)
				{
					System.out.println((keyIndex + 1) + ") " + ((keyIndex < 9) ? " " : "") + "Product Key (GOOD): " + productKey);
				}
				else
				{
					System.out.println((keyIndex + 1) + ")  " + ((keyIndex < 9) ? " " : "") + "Product Key (BAD): " + productKey + ":  Key State: " + tmpGeneratedKeyState.name());
				}
				
				keyIndex++ ;
			}
			else
			{
				System.out.println("Unable to validate empty product key.");
			}
		}
	}
	
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
	protected HyphenWorker buildHyphenWorker()
	{
		return new BasicHyphenWorker();
	}
	
	@Override
	protected SeedAvailabilityWorker buildSeedAvailabilityWorker()
	{
		return null;
	}
	
}
