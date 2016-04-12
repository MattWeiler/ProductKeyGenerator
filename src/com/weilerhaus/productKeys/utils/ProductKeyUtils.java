package com.weilerhaus.productKeys.utils;

public class ProductKeyUtils
{
	
	
	/**
	 * This method will build a HEXADECIMAL {@link String} that will be of the specified length
	 * using the specified content.
	 * 
	 * @param expectedLength
	 * The expected length of the string... if the HEXADECIMAL {@link String} is longer than this,
	 * the leading characters will be removed.
	 * @param content
	 * The content to be used when building the HEXADECIMAL {@link String}.
	 * @return
	 * The HEXADECIMAL {@link String}.
	 */
	public static String buildHexStr(final int expectedLength, final long content)
	{
		String hexStr = String.format("%0" + expectedLength + "X", content);
		
		if (hexStr.length() > expectedLength)
		{
			hexStr = hexStr.substring(hexStr.length() - expectedLength);
		}
		
		while (hexStr.length() < expectedLength)
		{
			hexStr = "0" + hexStr;
		}
		
		return hexStr;
	}
	
}
