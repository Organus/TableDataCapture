package gov.nrel.nbc.security.crypto;

import gov.nrel.nbc.security.client.AppConstants;
import gov.nrel.nbc.security.dbUtils.GenerateGUID;
import gov.nrel.nbc.security.server.SecurityServiceImpl;
import gov.nrel.nbc.security.utils.XLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryption implements AppConstants
{
	private static final String ENCODED_EXTENSION = ".enc";
	private static final String DECODED_EXTENSION = ".dec";
	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(XLogger.INFO);
    // Buffer used to transport the bytes from one stream to another
    private byte[] buf = new byte[1024];
    private Cipher ecipher;
    private Cipher dcipher;
    
    public FileEncryption(String client) {
        // Create an 8-byte initialization vector
        //byte[] iv = new byte[]{
        //    (byte)0x*, 0x*, 0x*, (byte)0x*,
        //    0x*, 0x*, 0x*, 0x*
        //};
        //AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        //SecurityServiceImpl susi = new SecurityServiceImpl();
        //String guid = susi.getGuidForClient(client);
        //String seed = guid+"."+CRYPTOKEY;
        //String seed = client+"."+CRYPTOKEY;
    	String seed = CRYPTOKEY;
        try {
            ecipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
            dcipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");

            // CBC requires an initialization vector
    		byte[] key_byteform = seed.getBytes();

    		SecretKeySpec skeySpec = new SecretKeySpec(key_byteform, "Blowfish");

    		ecipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            dcipher.init(Cipher.DECRYPT_MODE, skeySpec);
        //} catch (java.security.InvalidAlgorithmParameterException e) {
        } catch (javax.crypto.NoSuchPaddingException e) {
        	log.warning("no padding exception");
        } catch (java.security.NoSuchAlgorithmException e) {
        	log.warning("no such algorithm exception");
        } catch (java.security.InvalidKeyException e) {
        	log.warning("invalid key exception");
        }
    }

	public String decryptString( byte[] encrypted, String passphrase )
    throws Exception
    {
		return decryptString(new String(encrypted), passphrase);
    }
	
	public String decryptString( String encrypted, String passphrase )
    throws Exception
    {
		log.info("encrypted="+encrypted);
		byte[] eBytes = encrypted.getBytes();
		
		byte[] key_byteform = passphrase.getBytes();

		SecretKeySpec skeySpec = new SecretKeySpec(key_byteform, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE,  skeySpec );

        byte decrypted[] = cipher.doFinal(eBytes) ;
       
        return new String( decrypted )  ;
    }
	
	public String encryptString( byte[] input, String passphrase )
    throws Exception {
		return encryptString(new String(input), passphrase);
	}
	
	public String encryptString( String input, String passphrase )
    throws Exception
    {
		log.info("input="+input);
        byte[] key_byteform = passphrase.getBytes();
        
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");  // more common algorithm
        SecretKeySpec skeySpec = new SecretKeySpec(key_byteform, "Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[]inputBytes = input.getBytes();//"UTF8");
        //Encode
        byte[] outputBytes = cipher.doFinal(inputBytes);
        String base64 = new String( outputBytes );
        log.info("encrypted="+new String(base64));
        return base64;
    }
	
	public String encryptFile(String filename) {
		String destfile = "";
		InputStream in = null;
		OutputStream out = null;
        try {
            //boolean encoded = filename.endsWith( ENCODED_EXTENSION );
            //String destfile = encoded ? filename.substring( 0, filename.length() - 4 )
            //                          : filename + ENCODED_EXTENSION;
        	int index = filename.lastIndexOf(FORWARD_SLASH);
        	String localName = filename.substring(index+1);
        	String path = filename.substring(0,index+1);
        	destfile = path+getObfuscatedFilename(localName)+ENCODED_EXTENSION;
            in = new FileInputStream( filename );
            FileOutputStream fout = new FileOutputStream(destfile);
            // Bytes written to out will be encrypted
            out = new CipherOutputStream(fout, ecipher);

            // Read in the cleartext bytes and write to out to encrypt
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0) {
                out.write(buf, 0, numRead);
            }
            out.close();
        } catch (java.io.IOException e) {
        }
        return destfile;
	}
	
	public String getObfuscatedFilename(String oldName) {
		String newName = "";
        GenerateGUID gen = new GenerateGUID(oldName);
        newName = gen.generateGUID();
		return newName;
	}
	
	public String decryptFile(String filename) {
		String destfile = "";
		InputStream in = null;
		OutputStream out = null;
		try {
            //boolean decoded = filename.endsWith( DECODED_EXTENSION );
            //String destfile = decoded ? filename.substring( 0, filename.length() - 4 )
            //                          : filename + DECODED_EXTENSION;
			destfile = filename.substring( 0, filename.length() - 4 ) + DECODED_EXTENSION;
            out = new FileOutputStream( destfile );
            FileInputStream fin = new FileInputStream(filename);
            // Bytes read from in will be decrypted
            in = new CipherInputStream(fin, dcipher);

            // Read in the decrypted bytes and write the cleartext to out
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0) {
                out.write(buf, 0, numRead);
            }
            out.close();
        } catch( Exception ex ) {
            ex.printStackTrace();
        } finally {
        	try {
	            if (in != null)
	            	in.close();
	            if (out != null)
	            	out.close();        
	        } catch (IOException ioe) {
        		ioe.printStackTrace();
        	}
        }
        return destfile;
	}
	public static void main( String[] args )
    {
        if( args.length < 2 )
        {
            System.err.println( "call with: client filename" );
            System.exit( 1 );
        }
        String client = args[ 0 ];
        String filename = args[ 1 ];
        FileEncryption fe = new FileEncryption(client);
        fe.encryptFile(filename);
        fe.decryptFile(filename);
    }
}
