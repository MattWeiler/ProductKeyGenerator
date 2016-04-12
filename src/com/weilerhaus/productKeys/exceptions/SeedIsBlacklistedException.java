package com.weilerhaus.productKeys.exceptions;

public class SeedIsBlacklistedException extends ProductKeyGenerationException
{
	
	
	/* PRIVATE CONSTANTS */
	private static final long serialVersionUID = 1L;
	
	/* CONSTRUCTORS */
	/**
	 * This will create a new instance of a {@link SeedIsBlacklistedException}.
	 */
	public SeedIsBlacklistedException()
	{
		super("Seed provided is black-listed.");
	}
	
}
