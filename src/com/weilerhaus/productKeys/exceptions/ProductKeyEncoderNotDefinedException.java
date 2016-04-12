package com.weilerhaus.productKeys.exceptions;

public class ProductKeyEncoderNotDefinedException extends ProductKeyGenerationException
{
	
	
	/* PRIVATE CONSTANTS */
	private static final long serialVersionUID = 1L;
	
	/* CONSTRUCTORS */
	/**
	 * This will create a new instance of a {@link ProductKeyEncoderNotDefinedException}.
	 */
	public ProductKeyEncoderNotDefinedException()
	{
		super("Product-key encoder is not defined.");
	}
	
}
