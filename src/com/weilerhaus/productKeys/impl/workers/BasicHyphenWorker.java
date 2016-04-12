package com.weilerhaus.productKeys.impl.workers;

import com.weilerhaus.productKeys.workers.HyphenWorker;

public class BasicHyphenWorker implements HyphenWorker
{
	
	
	@Override
	public String addHyphens(final String productKey)
	{
		if ((productKey != null) && (productKey.trim().length() > 0))
		{
			final StringBuilder productKeySb = new StringBuilder(productKey.trim().toUpperCase());
			
			int tmpCharsSinceDash = 0;
			
			for (int n = (productKeySb.length() - 1); n >= 0; n-- )
			{
				if (n >= 6)
				{
					if (tmpCharsSinceDash >= 6)
					{
						productKeySb.insert(n, '-');
						tmpCharsSinceDash = 0;
					}
					else
					{
						tmpCharsSinceDash++ ;
					}
				}
				else
				{
					break;
				}
			}
			
			return productKeySb.toString();
		}
		
		return null;
	}
	
}
