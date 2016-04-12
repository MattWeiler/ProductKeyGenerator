package com.weilerhaus.productKeys.impl.workers;

import com.weilerhaus.productKeys.impl.beans.BasicProductKeyEncodingData;
import com.weilerhaus.productKeys.workers.ProductKeySectionWorker;

public class BasicProductKeySectionWorker implements ProductKeySectionWorker<BasicProductKeyEncodingData>
{
	
	
	@Override
	public byte buildProductKeySection(final long seed, final BasicProductKeyEncodingData encodingData)
	{
		byte a = (byte) (((int) encodingData.getA()) % 25);
		byte b = (byte) (((int) encodingData.getB()) % 3);
		
		if (((int) a) % 2 == 0)
		{
			return (byte) (((seed >> a) & 0x000000FF) ^ ((seed >> b) | encodingData.getC()));
		}
		else
		{
			return (byte) (((seed >> a) & 0x000000FF) ^ ((seed >> b) & encodingData.getC()));
		}
	}
	
}
