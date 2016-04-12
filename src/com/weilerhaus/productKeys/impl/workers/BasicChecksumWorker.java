package com.weilerhaus.productKeys.impl.workers;

import com.weilerhaus.productKeys.utils.ProductKeyUtils;
import com.weilerhaus.productKeys.workers.ChecksumWorker;

public class BasicChecksumWorker implements ChecksumWorker
{
	
	
	@Override
	public String buildProductKeyChecksum(String productKey)
	{
		if ((productKey != null) && (productKey.trim().length() > 0))
		{
			productKey = productKey.trim().replace("-", "").toUpperCase();
			
			int left = 0x0056;
			int right = 0x00AF;
			
			for (int n = 0; n < productKey.length(); n++ )
			{
				right = right + (byte) productKey.charAt(n);
				
				if (right > 0x00FF)
				{
					right -= 0x00FF;
				}
				
				left += right;
				
				if (left > 0x00FF)
				{
					left -= 0x00FF;
				}
			}
			
			return ProductKeyUtils.buildHexStr(4, (left << 8) + right);
		}
		
		return null;
	}
	
	@Override
	public boolean verifyProductKeyChecksum(String productKey)
	{
		if ((productKey != null) && (productKey.trim().length() > 0))
		{
			productKey = productKey.trim().replace("-", "").toUpperCase();
			
			if (productKey.length() > 4)
			{
				// Extract the last four characters as they are the checksum.
				final String checkSum = productKey.substring(productKey.length() - 4);
				
				productKey = productKey.substring(0, productKey.length() - 4);
				
				// Compare the extracted checksum against the generated checksum for the key string.
				return checkSum.equals(buildProductKeyChecksum(productKey));
			}
		}
		
		return false;
	}
	
}
