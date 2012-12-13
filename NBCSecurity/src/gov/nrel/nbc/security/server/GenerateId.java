package gov.nrel.nbc.security.server;

import gov.nrel.nbc.security.client.AppConstants;
import gov.nrel.nbc.security.utils.XLogger;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;


/**
 * GenerateId source file.
 **/
public class GenerateId implements AppConstants {
    private static final XLogger log = new XLogger(XLogger.INFO);
    public SecureRandom prng=null;

    /**
     * Constructor.
     **/
    public GenerateId() throws NoSuchAlgorithmException {
      prng = SecureRandom.getInstance(GENKEY);
	}
	  
    /**
     * Return a random number.
     * @return The randomly generated number.
	 * @throws NoSuchAlgorithmException
     **/
	public int getRandomNumber() throws NoSuchAlgorithmException {
      //generate a random number
      int randomNum = prng.nextInt();
	  return randomNum;
	}
 
    /**
     * Return the supplied input integer into its encoded equivalent.
     * @param Id integer to encode.
     * @return Encoded version of integer.
	 * @throws NoSuchAlgorithmException
     **/
    public String getHexEncoding(int Id) throws NoSuchAlgorithmException {
      MessageDigest sha = MessageDigest.getInstance("SHA-1");
	  String sid = new Integer(Id).toString();
      byte[] result =  sha.digest( sid.getBytes() );
	  return hexEncode(result);
	}

    /**
     * Return the supplied input integer into its encoded equivalent.
     * @param sid string to encode.
     * @return Encoded version of string.
	 * @throws NoSuchAlgorithmException
     **/
    public String getHexEncoding(String sid) throws NoSuchAlgorithmException {
      MessageDigest sha = MessageDigest.getInstance("SHA-1");
      byte[] result =  sha.digest( sid.getBytes() );
	  return hexEncode(result);
	}

    /**
     * Convert the supplied input string into its hex encoded equivalent.
     * @param aInput String to encode.
     * @return hex encoded version of input string.
     **/
    private String hexEncode( byte[] aInput) {
      StringBuffer result = new StringBuffer();
      char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
      for ( int idx = 0; idx < aInput.length; ++idx) 
	  {
          byte b = aInput[idx];
          result.append( digits[ (b&0xf0) >> 4 ] );
          result.append( digits[ b&0x0f] );
      }
      return result.toString();
   }

    /**
     * Digest the supplied input string into its encrypted equivalent.
     * @param input String to encrypt.
     * @return Encrypted version of input string.
     **/
    public String getEncrypted(String input)
    {
        try
        {
            // Digest input String into a stream of bytes
            MessageDigest sha = MessageDigest.getInstance("MD5");
            byte[] tmp = input.getBytes();
            sha.update(tmp);
            byte[] encryptedStringInBytes = sha.digest();

            // Split bytes in half so that they'll map to printable characters 
            byte[] newBytes = new byte[encryptedStringInBytes.length * 2];
            int length = encryptedStringInBytes.length;
            log.info("encrypted string="+encryptedStringInBytes);

            int byteCounter = 0;
            for (int i = 0; i < length; i++)
            {
                newBytes[byteCounter++] =
                    (byte) (((encryptedStringInBytes[i] & 0xF0) << 0x04)
                        + 0x41);
                newBytes[byteCounter++] =
                    (byte) ((encryptedStringInBytes[i] & 0x0F) + 0x41);
            }

            // Return printable bytes as a string.            
            return new String(newBytes);
        }
        catch (java.security.NoSuchAlgorithmException e)
        {
            log.warning("Unable to retrieve algorithm");
            SecurityServiceImpl.getStackTrace(e);
            return input;
        }
    }

} 

