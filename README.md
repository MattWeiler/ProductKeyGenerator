# ProductKeyGenerator
A simple but powerful product-key generator written in Java.

This code has been based off of an example article found at:  
http://www.brandonstaggs.com/2007/07/26/implementing-a-partial-serial-number-verification-system-in-delphi/

This example caught my eye since pascal/Delphi were my first encounter with programming.
I just ported this example to Java as that's what I now use everyday.


## Introduction
The basic idea behind this product-key generator is that not all of the encoding/decoding logic is included in the client-installed application binary files.  
This design is commonly known as a **Partial Key Verification System**.

#### Key Goals
###### 1. Easy to Type In
The generated keys will have hyphens (dashes) inserted separating the key into groups of characters.

###### 2. Black-list Keys
The ability to black-list keys has been made very dynamic wherein it can be done in an online/offline manor.

###### 3. Offline Validation/Verification
The validation/verification of a product-key must be completly offline.  
This just ensures that if your server goes down or you go out of business, your customers can still install your software.  
While having an online validation/verification service can provide a much better way of stopping illegitimate usage of your product, these approaches usually just punish the legal users.

###### 4. Client Binaries Lack Full Encoder
To ensure that hackers can't make a key-generator for your product, it's a good approach to not include the entire encoding logic in the client-installed binary files.  
Basically the generated product-key contains several sections each of which is encoded using one algorithm but the encoding data differs for each section.  
The product-key generation logic must know the encoding data for all sections as it will generate those sections but the client-installed application should only contain the encoding data for a sub-set of the sections.  
Thus, the validation/verification process only checks some sections of each product-key but that should be enough, along with the checksum, to give a reasonable confidence in the product-key.  
Taking this approach, a hacker could only create a key-generator for a specific version/build of your software and once you release a new version with different sections "exposed", they'd have to crack it all over again.

###### 5. Typo Detection
If a user makes a typo while entering their product-key, we can detect this through the use of a checksum component in the product-key.  
This checksum will allow us to see that the user likely made a typo while entering their product-key.


## Getting Started
1. Create a new class that extends [ProductKeyEncodingData](src/com/weilerhaus/productKeys/beans/ProductKeyEncodingData.java).
2. Create a new class that extends [ProductKeyGenerator](src/com/weilerhaus/productKeys/ProductKeyGenerator.java) and specify your [ProductKeyEncodingData](src/com/weilerhaus/productKeys/beans/ProductKeyEncodingData.java) class as the generic parameter.
3. Create an instance of your [ProductKeyGenerator](src/com/weilerhaus/productKeys/ProductKeyGenerator.java) class and call the **generateProductKey(...)** method to generate a product-key.
4. Call the **verifyProductKey(...)** method on your [ProductKeyGenerator](src/com/weilerhaus/productKeys/ProductKeyGenerator.java) class to validate/verify the product-key generated.
5. To properly simulate a client validating/verifying a generated product-key, create a new instance of your [ProductKeyGenerator](src/com/weilerhaus/productKeys/ProductKeyGenerator.java) class but this time nullify some of those [ProductKeyEncodingData](src/com/weilerhaus/productKeys/beans/ProductKeyEncodingData.java) entries that you pass into this instance of the [ProductKeyGenerator](src/com/weilerhaus/productKeys/ProductKeyGenerator.java) class.


#### Example Code
For ease of readability I've create a [BasicProductKeyGenerator](src/com/weilerhaus/productKeys/impl/BasicProductKeyGenerator.java) class which serves as a basic [ProductKeyGenerator](src/com/weilerhaus/productKeys/ProductKeyGenerator.java).
```java
String tmpKey;
ProductKeyState tmpGeneratedKeyState;
final List<String> generatedProductKeys = new ArrayList<String>();

// @formatter:off
ProductKeyGenerator<BasicProductKeyEncodingData> productKeyGenerator = new BasicProductKeyGenerator(
		new BasicProductKeyEncodingData((byte) 24, (byte) 3, (byte) 101),
		new BasicProductKeyEncodingData((byte) 10, (byte) 4, (byte) 56),
		new BasicProductKeyEncodingData((byte) 1, (byte) 2, (byte) 91),
		new BasicProductKeyEncodingData((byte) 7, (byte) 1, (byte) 100),
		new BasicProductKeyEncodingData((byte) 2, (byte) 36, (byte) 45),
		new BasicProductKeyEncodingData((byte) 13, (byte) 5, (byte) 54),
		new BasicProductKeyEncodingData((byte) 21, (byte) 67, (byte) 25),
		new BasicProductKeyEncodingData((byte) 3, (byte) 76, (byte) 12),
		new BasicProductKeyEncodingData((byte) 31, (byte) 22, (byte) 34),
		new BasicProductKeyEncodingData((byte) 15, (byte) 72, (byte) 65)
);
// @formatter:on

int tmpTryCount;

System.out.println("**** BUILDING KEYS ****");

final Random randomGenerator = new Random(System.nanoTime());

for (int n = 0; n < 25; n++ )
{
	tmpKey = null;
	tmpTryCount = 0;
	
	while ((tmpKey == null) && (tmpTryCount < 10))
	{
		try
		{
			tmpKey = productKeyGenerator.generateProductKey(randomGenerator.nextLong());
		}
		catch (Exception e)
		{
			
		}
		
		tmpTryCount++ ;
	}
	
	if ((tmpKey != null) && (tmpKey.trim().length() > 0))
	{
		tmpGeneratedKeyState = productKeyGenerator.verifyProductKey(tmpKey);
		
		if (ProductKeyState.KEY_GOOD == tmpGeneratedKeyState)
		{
			generatedProductKeys.add(tmpKey);
			
			System.out.println((n + 1) + ") " + ((n < 9) ? " " : "") + "Product Key (GOOD): " + tmpKey);
		}
		else
		{
			System.out.println((n + 1) + ")  " + ((n < 9) ? " " : "") + "Product Key (BAD): " + tmpKey + ":  Key State: " + tmpGeneratedKeyState.name());
		}
	}
	else
	{
		System.out.println("Failed to generate product keys.");
		
		break;
	}
}

System.out.println();
System.out.println();
System.out.println("**** VERIFYING KEYS ****");

// @formatter:off
BasicProductKeyGenerator basicProductKeyGenerator = new BasicProductKeyGenerator(
	new BasicProductKeyEncodingData((byte) 24, (byte) 3, (byte) 101),
	null,
	new BasicProductKeyEncodingData((byte) 1, (byte) 2, (byte) 91),
	new BasicProductKeyEncodingData((byte) 7, (byte) 1, (byte) 100),
	null,
	null,
	new BasicProductKeyEncodingData((byte) 21, (byte) 67, (byte) 25),
	null,
	new BasicProductKeyEncodingData((byte) 31, (byte) 22, (byte) 34),
	null
);
// @formatter:on

int keyIndex = 0;

for (String productKey : generatedProductKeys)
{
	if ((productKey != null) && (productKey.trim().length() > 0))
	{
		tmpGeneratedKeyState = basicProductKeyGenerator.verifyProductKey(productKey);
		
		if (ProductKeyState.KEY_GOOD == tmpGeneratedKeyState)
		{
			System.out.println((keyIndex + 1) + ") " + ((keyIndex < 9) ? " " : "") + "Product Key (GOOD): " + productKey);
		}
		else
		{
			System.out.println((keyIndex + 1) + ")  " + ((keyIndex < 9) ? " " : "") + "Product Key (BAD): " + productKey + ":  Key State: " + tmpGeneratedKeyState.name());
		}
		
		keyIndex++ ;
	}
	else
	{
		System.out.println("Unable to validate empty product key.");
	}
}
```

#### Example Output
```
**** BUILDING KEYS ****
1)  Product Key (GOOD): 2FBE0992-D87B89-53DBD4-743ACD-7C8735
2)  Product Key (GOOD): EA667E36-9DA412-FCB237-4ACEC3-CCE942
3)  Product Key (GOOD): C78E21BF-387794-07D057-253B79-1D94E5
4)  Product Key (GOOD): 3573DE13-42CE09-BCBB9A-A2CA53-E6E329
5)  Product Key (GOOD): 333EB5D2-C454B9-0B8BC1-90B23C-3D0F06
6)  Product Key (GOOD): C422BFAA-2B529F-3B4537-30F109-45E9E8
7)  Product Key (GOOD): 12C93C35-677513-78304D-8E8ECA-93A5E3
8)  Product Key (GOOD): 3B2864FB-C46467-ADC175-C093EC-118B03
9)  Product Key (GOOD): 84494A54-F1683B-B4E85E-2A4203-D291E5
10) Product Key (GOOD): 6926BF39-1413D6-7AF333-51EB42-4C2DE4
11) Product Key (GOOD): 6A3E3D4C-0731F5-5E3EE3-51AD53-3CC70C
12) Product Key (GOOD): 7BB1A89A-84174F-1599AB-D41FCD-634C1D
13) Product Key (GOOD): 1E7239E2-F977A9-1397A1-E23C14-A43FEB
14) Product Key (GOOD): F18D676D-9CE7ED-EAB679-9CE92B-5BBE64
15) Product Key (GOOD): 763CB7B6-81D692-2F52C1-A8FE25-795006
16) Product Key (GOOD): B6E9CE70-C34B20-BCE15A-AFC603-93D229
17) Product Key (GOOD): D5860BB7-227992-575214-B57ED5-0DCDDE
18) Product Key (GOOD): 90BE8E63-F79A29-3CF7E4-94CC0A-3CE234
19) Product Key (GOOD): 7A54A5D9-87D5BE-2F8B93-DAB779-E82242
20) Product Key (GOOD): 51A28CD8-ACDF7E-7DCB22-85975D-05022F
21) Product Key (GOOD): 3203F605-57C703-ECAC1F-90C0FA-0696F3
22) Product Key (GOOD): BDC1544D-D06B35-8C7E18-EE8D77-C3BC2F
23) Product Key (GOOD): 86D02C51-F33338-786995-3E829B-E160DB
24) Product Key (GOOD): 522EF17C-2F02E5-C62261-89237B-1DAEE0
25) Product Key (GOOD): D7614871-A26A20-B0611E-A3061B-83B3BE


**** VERIFYING KEYS ****
1)  Product Key (GOOD): 2FBE0992-D87B89-53DBD4-743ACD-7C8735
2)  Product Key (GOOD): EA667E36-9DA412-FCB237-4ACEC3-CCE942
3)  Product Key (GOOD): C78E21BF-387794-07D057-253B79-1D94E5
4)  Product Key (GOOD): 3573DE13-42CE09-BCBB9A-A2CA53-E6E329
5)  Product Key (GOOD): 333EB5D2-C454B9-0B8BC1-90B23C-3D0F06
6)  Product Key (GOOD): C422BFAA-2B529F-3B4537-30F109-45E9E8
7)  Product Key (GOOD): 12C93C35-677513-78304D-8E8ECA-93A5E3
8)  Product Key (GOOD): 3B2864FB-C46467-ADC175-C093EC-118B03
9)  Product Key (GOOD): 84494A54-F1683B-B4E85E-2A4203-D291E5
10) Product Key (GOOD): 6926BF39-1413D6-7AF333-51EB42-4C2DE4
11) Product Key (GOOD): 6A3E3D4C-0731F5-5E3EE3-51AD53-3CC70C
12) Product Key (GOOD): 7BB1A89A-84174F-1599AB-D41FCD-634C1D
13) Product Key (GOOD): 1E7239E2-F977A9-1397A1-E23C14-A43FEB
14) Product Key (GOOD): F18D676D-9CE7ED-EAB679-9CE92B-5BBE64
15) Product Key (GOOD): 763CB7B6-81D692-2F52C1-A8FE25-795006
16) Product Key (GOOD): B6E9CE70-C34B20-BCE15A-AFC603-93D229
17) Product Key (GOOD): D5860BB7-227992-575214-B57ED5-0DCDDE
18) Product Key (GOOD): 90BE8E63-F79A29-3CF7E4-94CC0A-3CE234
19) Product Key (GOOD): 7A54A5D9-87D5BE-2F8B93-DAB779-E82242
20) Product Key (GOOD): 51A28CD8-ACDF7E-7DCB22-85975D-05022F
21) Product Key (GOOD): 3203F605-57C703-ECAC1F-90C0FA-0696F3
22) Product Key (GOOD): BDC1544D-D06B35-8C7E18-EE8D77-C3BC2F
23) Product Key (GOOD): 86D02C51-F33338-786995-3E829B-E160DB
24) Product Key (GOOD): 522EF17C-2F02E5-C62261-89237B-1DAEE0
25) Product Key (GOOD): D7614871-A26A20-B0611E-A3061B-83B3BE
```
