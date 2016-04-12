package com.weilerhaus.productKeys.impl.beans;

import com.weilerhaus.productKeys.beans.ProductKeyEncodingData;

public class BasicProductKeyEncodingData implements ProductKeyEncodingData
{
	
	
	/* PRIVATE VARIABLES */
	/**
	 * This will store the 1st byte.
	 */
	private final byte a;
	/**
	 * This will store the 2nd byte.
	 */
	private final byte b;
	/**
	 * This will store the 3rd byte.
	 */
	private final byte c;
	
	/* CONSTRUCTORS */
	/**
	 * This will create a new instance of a {@link BasicProductKeyEncodingData}.
	 * 
	 * @param a
	 * The 1st byte.
	 * @param b
	 * The 2nd byte.
	 * @param c
	 * The 3rd byte.
	 */
	public BasicProductKeyEncodingData(final byte a, final byte b, final byte c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	/* PUBLIC METHODS */
	/**
	 * This will get the 1st byte.
	 * 
	 * @return
	 * The 1st byte.
	 */
	public byte getA()
	{
		return this.a;
	}
	
	/**
	 * This will get the 2nd byte.
	 * 
	 * @return
	 * The 2nd byte.
	 */
	public byte getB()
	{
		return this.b;
	}
	
	/**
	 * This will get the 3rd byte.
	 * 
	 * @return
	 * The 3rd byte.
	 */
	public byte getC()
	{
		return this.c;
	}
	
}
