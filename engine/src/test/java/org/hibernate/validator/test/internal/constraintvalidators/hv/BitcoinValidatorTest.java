/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.test.internal.constraintvalidators.hv;

import org.hibernate.validator.constraints.BitcoinAddress;
import org.hibernate.validator.constraints.BitcoinAddressType;
import org.hibernate.validator.internal.constraintvalidators.bv.BitcoinAddressValidator;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hibernate.validator.constraints.BitcoinAddressType.ANY;
import static org.hibernate.validator.constraints.BitcoinAddressType.BECH32;
import static org.hibernate.validator.constraints.BitcoinAddressType.P2PKH;
import static org.hibernate.validator.constraints.BitcoinAddressType.P2SH;
import static org.hibernate.validator.constraints.BitcoinAddressType.P2TR;
import static org.hibernate.validator.constraints.BitcoinAddressType.P2WPKH;
import static org.hibernate.validator.constraints.BitcoinAddressType.P2WSH;
import static org.testng.Assert.assertTrue;

/**
 * Tests the {@code BitcoinAddress} constraint.
 *
 * @author José Yoshiriro
 */
public class BitcoinValidatorTest {

	private BitcoinAddressValidator bitcoinAddressValidator;
	private ConstraintAnnotationDescriptor.Builder<BitcoinAddress> descriptorBuilder;

	@BeforeMethod
	public void setUp() {
		descriptorBuilder = new ConstraintAnnotationDescriptor.Builder<>( BitcoinAddress.class );
		bitcoinAddressValidator = new BitcoinAddressValidator();
	}

	@Test(dataProvider = "validAddressesSingleType")
	public void valid_btc_address_single_type_pass_validation( BitcoinAddressType type, String address ) {
		descriptorBuilder.setAttribute( "value", new BitcoinAddressType[] { type } );
		bitcoinAddressValidator.initialize( descriptorBuilder.build().getAnnotation() );

		assertTrue( bitcoinAddressValidator.isValid( address, null ),
				String.format( "should be a valid %s address. Tested value: %s", type.getDescription(), address ) );
	}

	@Test(dataProvider = "validAddressesMultipleTypes")
	public void valid_btc_address_simultiple_types_pass_validation(BitcoinAddressType[] types, String address ) {
		descriptorBuilder.setAttribute( "value", types );
		bitcoinAddressValidator.initialize( descriptorBuilder.build().getAnnotation() );

		String descriptions = Arrays.stream( types )
				.map( BitcoinAddressType::getDescription )
				.collect( Collectors.joining( "," ) );

		assertTrue( bitcoinAddressValidator.isValid( address, null ),
				String.format( "should be a valid %s address. Tested value: %s", descriptions, address ) );
	}


	@DataProvider(name = "validAddressesSingleType")
	private static Object[][] validAddressesSingleType() {
		return new Object[][] {
				{ ANY, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ ANY, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ ANY, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ ANY, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ ANY, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ ANY, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ P2PKH, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ P2SH, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ BECH32, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ P2WSH, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ P2WPKH, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ P2TR, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" }
		};
	}


	@DataProvider(name = "validAddressesMultipleTypes")
	private static Object[][] validAddressesMultipleTypes() {
		return new Object[][]{
				{ new BitcoinAddressType[] {P2PKH, P2SH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2PKH, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2PKH, BECH32}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2PKH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2PKH, P2WSH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2PKH, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2PKH, P2WPKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2PKH, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2PKH, P2TR}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2PKH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2SH, P2PKH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2SH, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2SH, BECH32}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2SH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2SH, P2WSH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2SH, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2SH, P2WPKH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2SH, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2SH, P2TR}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2SH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {BECH32, P2PKH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {BECH32, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {BECH32, P2SH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {BECH32, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {BECH32, P2WSH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {BECH32, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {BECH32, P2WPKH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {BECH32, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {BECH32, P2TR}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {BECH32, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WSH, P2SH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WSH, BECH32}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WSH, P2TR}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2PKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2TR, P2SH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2TR, BECH32}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2TR, P2WSH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {BECH32, P2WPKH, P2TR}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {BECH32, P2WPKH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2PKH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2PKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2SH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2SH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2WSH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2WSH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2WPKH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2WPKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {BECH32, P2TR, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, P2SH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, P2SH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, BECH32}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, BECH32}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, P2WPKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, P2WPKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, P2TR}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, P2TR}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WSH, P2PKH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, P2PKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, P2PKH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, BECH32}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, BECH32}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, P2WPKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, P2WPKH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, P2TR}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, P2TR}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WSH, P2SH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2PKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2PKH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2SH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2SH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2WPKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2WPKH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2TR}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2TR}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WSH, BECH32, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, P2PKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, P2PKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, P2SH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, P2SH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, BECH32}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, BECH32}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, P2TR}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, P2TR}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WSH, P2WPKH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, P2PKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, P2PKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, P2SH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, P2SH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, BECH32}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, BECH32}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, P2WPKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, P2WPKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WSH, P2TR, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, P2SH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, P2SH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, BECH32}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, BECH32}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, P2WSH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, P2WSH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, P2TR}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, P2TR}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WPKH, P2PKH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, P2PKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, P2PKH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, BECH32}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, BECH32}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, P2WSH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, P2WSH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, P2TR}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, P2TR}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WPKH, P2SH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2PKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2PKH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2SH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2SH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2WSH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2WSH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2TR}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2TR}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WPKH, BECH32, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, P2PKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, P2PKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, P2SH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, P2SH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, BECH32}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, BECH32}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, P2TR}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, P2TR}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2WPKH, P2WSH, P2TR}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, P2PKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, P2PKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, P2SH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, P2SH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, BECH32}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, BECH32}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, P2WSH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, P2WSH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2WPKH, P2TR, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, P2SH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, P2SH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, BECH32}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, BECH32}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, P2WSH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, P2WSH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, P2WPKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, P2WPKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2TR, P2PKH, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2TR, P2SH, P2PKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2SH, P2PKH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2TR, P2SH, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2TR, P2SH, BECH32}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2SH, BECH32}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2TR, P2SH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2TR, P2SH, P2WSH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2SH, P2WSH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2TR, P2SH, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2TR, P2SH, P2WPKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2SH, P2WPKH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2TR, P2SH, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2PKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2PKH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2SH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2SH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2WSH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2WSH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2WPKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2WPKH}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2TR, BECH32, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, P2PKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, P2PKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, P2SH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, P2SH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, BECH32}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, BECH32}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, P2WPKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, P2WPKH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" },
				{ new BitcoinAddressType[] {P2TR, P2WSH, P2WPKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, P2PKH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, P2PKH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, P2PKH}, "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, P2SH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, P2SH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, P2SH}, "342ftSRCvFHfCeFFBuz4xwbeqnDw6BGUey" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, BECH32}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, BECH32}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, BECH32}, "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, P2WSH}, "bc1p5d7rjq7g6rdk2yhzks9smlaqtedr4dekq08ge8ztwac72sfr9rusxg3297" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, P2WSH}, "bc1q34aq5drpuwy3wgl9lhup9892qp6svr8ldzyy7c" },
				{ new BitcoinAddressType[] {P2TR, P2WPKH, P2WSH}, "bc1qeklep85ntjz4605drds6aww9u0qr46qzrv5xswd35uhjuj8ahfcqgf6hak" }
		};
	}

}
