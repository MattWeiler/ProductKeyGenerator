package com.weilerhaus.productKeys;

import com.weilerhaus.productKeys.beans.ProductKeyEncodingData;
import com.weilerhaus.productKeys.enums.ProductKeyState;
import com.weilerhaus.productKeys.exceptions.EncodingDataNotCompleteException;
import com.weilerhaus.productKeys.exceptions.InvalidSeedException;
import com.weilerhaus.productKeys.exceptions.ProductKeyEncoderNotDefinedException;
import com.weilerhaus.productKeys.exceptions.ProductKeyGenerationException;
import com.weilerhaus.productKeys.exceptions.SeedAlreadyTakenException;
import com.weilerhaus.productKeys.exceptions.SeedIsBlacklistedException;
import com.weilerhaus.productKeys.utils.ProductKeyUtils;
import com.weilerhaus.productKeys.workers.BlacklistWorker;
import com.weilerhaus.productKeys.workers.ChecksumWorker;
import com.weilerhaus.productKeys.workers.ProductKeySectionWorker;
import com.weilerhaus.productKeys.workers.ProductKeyStylingWorker;
import com.weilerhaus.productKeys.workers.SeedAvailabilityWorker;

/**
 * This class can be extended to provide an application specific product-key generator.
 * 
 * @author Matthew Weiler
 */
public abstract class ProductKeyGenerator<ED extends ProductKeyEncodingData>
{
	
	
	/* PRIVATE VARIABLES */
	/**
	 * This will store the number of HEXADECIMAL characters, based on the provided seed at
	 * generation time, that will appear at the beginning of the generated product-key thus
	 * partially defining its total length.
	 * <br />
	 * <br />
	 * This value must be positive and cannot exceeds 15; if it exceeds these bounds, it will be
	 * assigned a number between 1 and 15 inclusive.
	 * <br />
	 * <i>If this value were to be allowed to exceed 15, the HEXADECIMAL value will be too large for
	 * {@link Long} to parse it.</i>
	 */
	private final int seedCharLength;
	/**
	 * This will store the array of {@link ProductKeyEncodingData} elements that will be used to
	 * generate/verify product-keys.
	 * <br />
	 * <i>
	 * If a {@link ProductKeyEncodingData} element is <code>null</code>, that section of the
	 * product-key will not be validated.
	 * <br />
	 * However, it should be noted that the encoding will only work if all
	 * {@link ProductKeyEncodingData} elements are present.
	 * <br />
	 * <br />
	 * When including this in the client application code, omit some {@link ProductKeyEncodingData}
	 * elements by setting them to <code>null</code>.
	 * <br />
	 * This will ensure that the application code running on the clients device never has the entire
	 * logic for creating a product-key; if reverse engineered, they will only be able to create a
	 * product-key that will work on that build/version of your application.
	 * <br />
	 * Each new build/version of your client application should/could change the omitted
	 * {@link ProductKeyEncodingData} elements thus ensuring that if a previously cracked key
	 * worked, it won't in the new build/version.
	 * </i>
	 */
	private final ED[] productKeyEncodingData;
	/**
	 * This will store the {@link ProductKeySectionWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 */
	private ProductKeySectionWorker<ED> productKeySectionWorker = null;
	/**
	 * This will store the {@link ChecksumWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	private ChecksumWorker checksumWorker = null;
	/**
	 * This will store the {@link BlacklistWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	private BlacklistWorker blacklistWorker = null;
	/**
	 * This will store the {@link ProductKeyStylingWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 */
	private ProductKeyStylingWorker productKeyStylingWorker = null;
	/**
	 * This will store the {@link SeedAvailabilityWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 */
	private SeedAvailabilityWorker seedAvailabilityWorker = null;
	
	/* CONSTRUCTORS */
	/**
	 * This will create a new instance of a {@link ProductKeyGenerator}.
	 * 
	 * @param seedCharLength
	 * The number of HEXADECIMAL characters, based on the provided seed at generation time, that
	 * will appear at the beginning of the generated product-key thus partially defining its total
	 * length.
	 * <br />
	 * <br />
	 * This value must be positive and cannot exceeds 15; if it exceeds these bounds, it will be
	 * assigned a number between 1 and 15 inclusive.
	 * <br />
	 * <i>If this value were to be allowed to exceed 15, the HEXADECIMAL value will be too large for
	 * {@link Long} to parse it.</i>
	 * @param productKeyEncodingData
	 * The array of {@link ProductKeyEncodingData} elements that will be used to generate/verify
	 * product-keys.
	 * <br />
	 * <i>
	 * If a {@link ProductKeyEncodingData} element is <code>null</code>, that section of the
	 * product-key will not be validated.
	 * <br />
	 * However, it should be noted that the encoding will only work if all
	 * {@link ProductKeyEncodingData} elements are present.
	 * <br />
	 * <br />
	 * When including this in the client application code, omit some {@link ProductKeyEncodingData}
	 * elements by setting them to <code>null</code>.
	 * <br />
	 * This will ensure that the application code running on the clients device never has the entire
	 * logic for creating a product-key; if reverse engineered, they will only be able to create a
	 * product-key that will work on that build/version of your application.
	 * <br />
	 * Each new build/version of your client application should/could change the omitted
	 * {@link ProductKeyEncodingData} elements thus ensuring that if a previously cracked key
	 * worked, it won't in the new build/version.
	 * </i>
	 */
	@SafeVarargs
	public ProductKeyGenerator(final int seedCharLength, final ED...productKeyEncodingData)
	{
		this.seedCharLength = ((seedCharLength > 0) && (seedCharLength <= 15)) ? seedCharLength : 10;
		
		if ((productKeyEncodingData != null) && (productKeyEncodingData.length > 0))
		{
			this.productKeyEncodingData = productKeyEncodingData;
		}
		else
		{
			this.productKeyEncodingData = null;
		}
	}
	
	/* PUBLIC METHODS */
	/**
	 * This method will get the number of HEXADECIMAL characters, based on the provided seed at
	 * generation time, that will appear at the beginning of the generated product-key thus
	 * partially defining its total length.
	 * <br />
	 * <br />
	 * This value must be positive and cannot exceeds 15; if it exceeds these bounds, it will be
	 * assigned a number between 1 and 15 inclusive.
	 * <br />
	 * <i>If this value were to be allowed to exceed 15, the HEXADECIMAL value will be too large for
	 * {@link Long} to parse it.</i>
	 * 
	 * @return
	 * The number of HEXADECIMAL characters, based on the provided seed at generation time, that
	 * will appear at the beginning of the generated product-key thus partially defining its total
	 * length.
	 * <br />
	 * <br />
	 * This value must be positive and cannot exceeds 15; if it exceeds these bounds, it will be
	 * assigned a number between 1 and 15 inclusive.
	 * <br />
	 * <i>If this value were to be allowed to exceed 15, the HEXADECIMAL value will be too large for
	 * {@link Long} to parse it.</i>
	 */
	public int getSeedCharLength()
	{
		return this.seedCharLength;
	}
	
	/**
	 * This method will verify the product-key specified.
	 * 
	 * @param productKey
	 * The product-key to be verified.
	 * @return
	 * The {@link ProductKeyState} that best describes the specified product-key.
	 */
	public ProductKeyState verifyProductKey(final String productKey)
	{
		if ((productKey != null) && (productKey.trim().length() > 0))
		{
			// Remove styling.
			final String cleanedProductKey;
			
			if (this.getProductKeyStylingWorker() != null)
			{
				cleanedProductKey = this.getProductKeyStylingWorker().removeStyling(productKey).toUpperCase();
			}
			else
			{
				cleanedProductKey = productKey.toUpperCase();
			}
			
			// Verify that the format of the product-key is valid.
			if ((this.getChecksumWorker() != null) && ( !this.getChecksumWorker().verifyProductKeyChecksum(cleanedProductKey)))
			{
				return ProductKeyState.KEY_INVALID;
			}
			
			// Test against blacklist.
			if ((this.getBlacklistWorker() != null) && this.getBlacklistWorker().isKeyBlackListed(cleanedProductKey))
			{
				return ProductKeyState.KEY_BLACKLISTED;
			}
			
			// If the product-key section bytes are present and valid in length, then verify the
			// product-key sections.
			if ((this.productKeyEncodingData != null) && (this.productKeyEncodingData.length > 0))
			{
				if (this.getProductKeySectionWorker() != null)
				{
					try
					{
						// Extract the seed from the product-key.
						long seed = Long.parseLong(cleanedProductKey.substring(0, this.getSeedCharLength()), 16);
						
						int currentKeyCharIndex = this.getSeedCharLength();
						String tmpKeySection;
						
						for (int n = 0; n < this.productKeyEncodingData.length; n++ )
						{
							// If the first byte of the current section is zero (0), then skip
							// validating
							// this section.
							if (this.productKeyEncodingData[n] != null)
							{
								tmpKeySection = cleanedProductKey.substring(currentKeyCharIndex, currentKeyCharIndex + 2);
								
								if ( !tmpKeySection.equals(ProductKeyUtils.buildHexStr(2, this.getProductKeySectionWorker().buildProductKeySection(seed, this.productKeyEncodingData[n]))))
								{
									return ProductKeyState.KEY_PHONY;
								}
							}
							
							currentKeyCharIndex += 2;
						}
						
						// If we get this far, then it means the key is either good, or was made
						// with a keygen derived from "this" release.
						return ProductKeyState.KEY_GOOD;
					}
					catch (NumberFormatException nfe)
					{
						// TODO: log this ...
						
					}
				}
			}
		}
		
		return ProductKeyState.KEY_PHONY;
	}
	
	/**
	 * This method will generate a new product-key for the specified seed.
	 * 
	 * @param seed
	 * The seed to use to ensure that the product-key is unique.
	 * @return
	 * The generated product-key.
	 * <br />
	 * <i>This will be <code>null</code> if the seed is not valid or this
	 * {@link ProductKeyGenerator}
	 * is not capable of creating a product key.</i>
	 * @throws ProductKeyGenerationException
	 * If the generation of the product-key failed.
	 */
	public String generateProductKey(long seed) throws ProductKeyGenerationException
	{
		// Build the HEXADECIMAL string representing the seed.
		String seedHex = ProductKeyUtils.buildHexStr(this.getSeedCharLength(), seed);
		
		try
		{
			// Try to parse the HEXADECIMAL string representing the seed and use that for the seed.
			seed = Long.parseLong(seedHex, 16);
			
			if ((this.productKeyEncodingData != null) && (this.productKeyEncodingData.length > 0))
			{
				if (this.getProductKeySectionWorker() != null)
				{
					final StringBuilder keySb = new StringBuilder();
					
					seedHex = ProductKeyUtils.buildHexStr(this.getSeedCharLength(), seed);
					
					if ((this.getBlacklistWorker() != null) && (this.getBlacklistWorker().isSeedBlackListed(seedHex)))
					{
						throw new SeedIsBlacklistedException();
					}
					
					if ((this.getSeedAvailabilityWorker() != null) && ( !this.getSeedAvailabilityWorker().isSeedAvailable(seedHex)))
					{
						throw new SeedAlreadyTakenException();
					}
					
					// The key string begins with a HEXADECIMAL string of the seed.
					keySb.append(seedHex);
					
					// Build the byte for the key-section derived from the seed.
					for (int n = 0; n < this.productKeyEncodingData.length; n++ )
					{
						if (this.productKeyEncodingData[n] == null)
						{
							throw new EncodingDataNotCompleteException();
						}
						
						keySb.append(ProductKeyUtils.buildHexStr(2, this.getProductKeySectionWorker().buildProductKeySection(seed, this.productKeyEncodingData[n])));
					}
					
					// Add checksum to key string.
					if (this.getChecksumWorker() != null)
					{
						keySb.append(this.getChecksumWorker().buildProductKeyChecksum(keySb.toString()));
					}
					
					// Add dashes to the product-key and return it.
					if (this.getProductKeyStylingWorker() != null)
					{
						return this.getProductKeyStylingWorker().addStyling(keySb.toString());
					}
					
					return keySb.toString();
				}
				else
				{
					throw new ProductKeyEncoderNotDefinedException();
				}
			}
			else
			{
				throw new EncodingDataNotCompleteException();
			}
		}
		catch (NumberFormatException nfe)
		{
			throw new InvalidSeedException();
		}
	}
	
	/* PROTECTED METHODS */
	/**
	 * This method will build the {@link ProductKeySectionWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 * 
	 * @return
	 * The {@link ProductKeySectionWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	protected abstract ProductKeySectionWorker<ED> buildProductKeySectionWorker();
	
	/**
	 * This method will build the {@link ChecksumWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 * 
	 * @return
	 * The {@link ChecksumWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	protected abstract ChecksumWorker buildChecksumWorker();
	
	/**
	 * This method will build the {@link BlacklistWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 * 
	 * @return
	 * The {@link BlacklistWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	protected abstract BlacklistWorker buildBlacklistWorker();
	
	/**
	 * This method will build the {@link ProductKeyStylingWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 * 
	 * @return
	 * The {@link ProductKeyStylingWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	protected abstract ProductKeyStylingWorker buildProductKeyStylingWorker();
	
	/**
	 * This method will build the {@link SeedAvailabilityWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 * 
	 * @return
	 * The {@link SeedAvailabilityWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	protected abstract SeedAvailabilityWorker buildSeedAvailabilityWorker();
	
	/* PRIVATE METHODS */
	/**
	 * This method will get the {@link ProductKeySectionWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 * 
	 * @return
	 * The {@link ProductKeySectionWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	private ProductKeySectionWorker<ED> getProductKeySectionWorker()
	{
		if (this.productKeySectionWorker == null)
		{
			this.productKeySectionWorker = this.buildProductKeySectionWorker();
		}
		
		return this.productKeySectionWorker;
	}
	
	/**
	 * This method will get the {@link ChecksumWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 * 
	 * @return
	 * The {@link ChecksumWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	private ChecksumWorker getChecksumWorker()
	{
		if (this.checksumWorker == null)
		{
			this.checksumWorker = this.buildChecksumWorker();
		}
		
		return this.checksumWorker;
	}
	
	/**
	 * This method will get the {@link BlacklistWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 * 
	 * @return
	 * The {@link BlacklistWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	private BlacklistWorker getBlacklistWorker()
	{
		if (this.blacklistWorker == null)
		{
			this.blacklistWorker = this.buildBlacklistWorker();
		}
		
		return this.blacklistWorker;
	}
	
	/**
	 * This method will get the {@link ProductKeyStylingWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 * 
	 * @return
	 * The {@link ProductKeyStylingWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	private ProductKeyStylingWorker getProductKeyStylingWorker()
	{
		if (this.productKeyStylingWorker == null)
		{
			this.productKeyStylingWorker = this.buildProductKeyStylingWorker();
		}
		
		return this.productKeyStylingWorker;
	}
	
	/**
	 * This method will get the {@link SeedAvailabilityWorker} to be used by this
	 * {@link ProductKeyGenerator}.
	 * 
	 * @return
	 * The {@link SeedAvailabilityWorker} to be used by this {@link ProductKeyGenerator}.
	 */
	private SeedAvailabilityWorker getSeedAvailabilityWorker()
	{
		if (this.seedAvailabilityWorker == null)
		{
			this.seedAvailabilityWorker = this.buildSeedAvailabilityWorker();
		}
		
		return this.seedAvailabilityWorker;
	}
	
}
