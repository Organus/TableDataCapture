package gov.nrel.nbc.security.crypto;

import gov.nrel.nbc.security.client.AppConstants;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Base64.Base64;

public class DataEncryption implements AppConstants {
	Cipher ecipher;
	Cipher dcipher;

	// 8-byte Salt
	byte[] salt = {
		(byte)0x*, (byte)0x*, (byte)0x*, (byte)0x*,
		(byte)0x*, (byte)0x*, (byte)0x*, (byte)0x
	};

	// Iteration count
	int iterationCount = 19;

	public DataEncryption() {
		this(CRYPTOKEY);
	}
	
	public DataEncryption(String passPhrase) {
		try {
			// Create the key
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.
			//getInstance("AES").generateSecret(keySpec);
				getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			ecipher = Cipher.getInstance(key.getAlgorithm());
			dcipher = Cipher.getInstance(key.getAlgorithm());

			// Prepare the parameter to the ciphers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

			// Create the ciphers
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (java.security.InvalidAlgorithmParameterException e) {
		} catch (java.security.spec.InvalidKeySpecException e) {
		} catch (javax.crypto.NoSuchPaddingException e) {
		} catch (java.security.NoSuchAlgorithmException e) {
		} catch (java.security.InvalidKeyException e) {
		}
	}

	public String encrypt(String str) {
		try {
			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			return Base64.encodeBytes(enc);//new BASE64Encoder().encode(enc);
		} catch (javax.crypto.BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		} 
		return null;
	}

	public String decrypt(String str) {
		if (str == null || str.isEmpty()) return null;
		try {
			// Decode base64 to get bytes
			byte[] dec = Base64.decode(str);//new BASE64Decoder().decodeBuffer(str);

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");
		} catch (javax.crypto.BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		} 
		return null;
	}

	public static DataEncryption getInstance() {
		return new DataEncryption(CRYPTOKEY);
	}

	public static byte[] genKey = null;
    private byte[] generateAESKey() throws Exception
    {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            // Generate the secret key specs.
            SecretKey skey = kgen.generateKey();
            return skey.getEncoded();
    }


    private byte[] encryptAES(byte key[], byte data[]) throws Exception
    {
           SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

           // Instantiate the cipher
           Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
           cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));

           return cipher.doFinal(data);
    }

    private byte[] decryptAES(byte key[], byte msg[]) throws Exception
    {
           SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

           // Instantiate the cipher
           Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
           cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));

           return cipher.doFinal(msg);
    }
    
    public String encryptAES(String msg) {
    	String ret = null;
    	try {
	    	DataEncryption.genKey = generateAESKey();
	    	byte[] enc = encryptAES(DataEncryption.genKey,msg.getBytes());
	    	ret = new String(enc);
    	} catch (Exception e) {}
    	return ret;
    }
    
    public String decryptAES(String msg) {
    	String ret = null;
    	try {
	    	byte[] dec = decryptAES(DataEncryption.genKey,msg.getBytes());
	    	ret = new String(dec);
    	} catch (Exception e) {}
    	return ret;
    }
}
