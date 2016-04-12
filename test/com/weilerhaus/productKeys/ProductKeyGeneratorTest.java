package com.weilerhaus.productKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.weilerhaus.productKeys.enums.ProductKeyState;
import com.weilerhaus.productKeys.impl.BasicProductKeyGenerator;
import com.weilerhaus.productKeys.impl.beans.BasicProductKeyEncodingData;

public class ProductKeyGeneratorTest
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
	
}
