package com.weilerhaus.productKeys.exceptions;

public class ProductKeyGenerationException extends Exception
{
	
	
	/* PRIVATE CONSTANTS */
	private static final long serialVersionUID = 1L;
	
	/* CONSTRUCTORS */
	/**
	 * This will create a new instance of a {@link ProductKeyGenerationException}.
	 */
	public ProductKeyGenerationException()
	{
		super("Failed to generate a product-key.");
	}
	
	/**
	 * This will create a new instance of a {@link ProductKeyGenerationException}.
	 * 
	 * @param detailedMessage
	 * The details for this {@link ProductKeyGenerationException}.
	 */
	public ProductKeyGenerationException(final String detailedMessage)
	{
		super("Failed to generate a product-key: " + detailedMessage);
	}
	
}
