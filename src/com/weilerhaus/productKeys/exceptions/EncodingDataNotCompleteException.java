package com.weilerhaus.productKeys.exceptions;

public class EncodingDataNotCompleteException extends ProductKeyGenerationException
{
	
	
	/* PRIVATE CONSTANTS */
	private static final long serialVersionUID = 1L;
	
	/* CONSTRUCTORS */
	/**
	 * This will create a new instance of a {@link EncodingDataNotCompleteException}.
	 */
	public EncodingDataNotCompleteException()
	{
		super("Encoding-data must be complete for generating product-keys.");
	}
	
}
