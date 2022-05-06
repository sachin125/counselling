package com.inn.counselling.utils;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DecryptUtil {
	/**
     * Hex string to byte array.
     *
     * @param s the s
     * @return the byte[]
     */
    public static byte [] hexStringToByteArray ( String s )
    {
        int len = s.length ();
        byte [] data = new byte[len / 2];
        for ( int i = 0; i < len; i += 2 )
        {
            data[i / 2] = (byte) ( ( Character.digit ( s.charAt ( i ), 16 ) << 4 ) + Character.digit ( s.charAt ( i + 1 ), 16 ) );
        }
        return data;
    }

    
    /**
     * Generate key from password with salt.
     *
     * @param password the password
     * @param saltBytes the salt bytes
     * @return the secret key
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeySpecException 
     * @throws GeneralSecurityException the general security exception
     */
    public static SecretKey generateKeyFromPasswordWithSalt ( String password, byte [] saltBytes ) throws NoSuchAlgorithmException, InvalidKeySpecException 
    {
        KeySpec keySpec = new PBEKeySpec( password.toCharArray (), saltBytes, 100, 128 );
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance ( "PBKDF2WithHmacSHA1" );
        SecretKey secretKey = keyFactory.generateSecret ( keySpec );

        return new SecretKeySpec( secretKey.getEncoded (), "AES" );
    }

    /**
     * Decrypt aes encrypt with salt and iv.
     *
     * @param encryptedData the encrypted data
     * @param key the key
     * @param salt the salt
     * @param iv the iv
     * @return the string
     * @throws Exception the exception
     */
    public static String decryptAESEncryptWithSaltAndIV ( String encryptedData, String key, String salt, String iv ) throws Exception
    {

        byte [] saltBytes = hexStringToByteArray ( salt );
        byte [] ivBytes = hexStringToByteArray ( iv );
        IvParameterSpec ivParameterSpec = new IvParameterSpec ( ivBytes );
        SecretKeySpec sKey = (SecretKeySpec) generateKeyFromPasswordWithSalt( key, saltBytes );

        Cipher c = Cipher.getInstance ( "AES/CBC/PKCS5Padding" );
        c.init ( Cipher.DECRYPT_MODE, sKey, ivParameterSpec );
        byte [] decordedValue=Base64.decodeBase64(encryptedData.getBytes());
        byte [] decValue = c.doFinal ( decordedValue );
       return  new String ( decValue );
         
    }
    public static String decryptStringWithBase64(String encryptedString) {
		if (encryptedString != null) {
			byte[] decodedchecksum = Base64.decodeBase64(encryptedString
					.getBytes());
			return new String(decodedchecksum);
		} else {
			return encryptedString;
		}
	}
	
	public static String getNewCSRFToken() {
		Double randomNumber = Math.random();
		String newCsrfToken = randomNumber.toString();
		newCsrfToken = newCsrfToken.substring(2, newCsrfToken.length() - 1);
		newCsrfToken = newCsrfToken + newCsrfToken + newCsrfToken;
		return convertHexToBase36(newCsrfToken);
	}

	static String convertHexToBase36(String hex) {
		BigInteger big = new BigInteger(hex, 16);
		return big.toString(36);
	}

}

