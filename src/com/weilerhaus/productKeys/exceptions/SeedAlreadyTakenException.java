package com.weilerhaus.productKeys.exceptions;


public class SeedAlreadyTakenException extends ProductKeyGenerationException
{
	
	
	/* PRIVATE CONSTANTS */
	private static final long serialVersionUID = 1L;
	
	/* CONSTRUCTORS */
	/**
	 * This will create a new instance of a {@link SeedAlreadyTakenException}.
	 */
	public SeedAlreadyTakenException()
	{
		super("Seed is already taken.");
	}
	
}
