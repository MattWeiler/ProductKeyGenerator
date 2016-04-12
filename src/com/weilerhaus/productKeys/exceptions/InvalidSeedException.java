package com.weilerhaus.productKeys.exceptions;

public class InvalidSeedException extends ProductKeyGenerationException
{
	
	
	/* PRIVATE CONSTANTS */
	private static final long serialVersionUID = 1L;
	
	/* CONSTRUCTORS */
	/**
	 * This will create a new instance of a {@link InvalidSeedException}.
	 */
	public InvalidSeedException()
	{
		super("Seed is invalid.");
	}
	
}
