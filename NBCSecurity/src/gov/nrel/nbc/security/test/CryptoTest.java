package gov.nrel.nbc.security.test;

import gov.nrel.nbc.security.client.AppConstants;
import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.crypto.FileEncryption;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CryptoTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;
	private String testString = "Now the the time for all good men to come to the aid of their country.";
	private String passphrase = "password";

	@Override
	protected void setUp() {
		thisLoader = DataEncryption.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(CryptoTest.class);
	}
	
	public void testEncryption() throws Exception {
		DataEncryption de = new DataEncryption(CRYPTOKEY);
		String encrypted = de.encrypt("Hello World");
		System.out.println(encrypted);
		assertFalse(encrypted.equals("Hello World"));
		String decrypted = de.decrypt(encrypted);
		System.out.println(decrypted);
		assertFalse(decrypted.equals(encrypted));
		assertTrue(decrypted.equals("Hello World"));
		System.out.println("security pwd is "+de.encrypt(CRYPTOKEY));
		System.out.println("securityuserpw is "+de.encrypt("securityuserpw"));
		System.out.println("spreadsheetuserpw is "+de.encrypt("spreadsheetuserpw"));
		System.out.println("trackeruserpw is "+de.encrypt("trackeruserpw"));
		System.out.println("tcufuserpw is "+de.encrypt("tcufuserpw"));
		System.out.println("$@!vad0R is "+de.encrypt("$@!vad0R"));
		System.out.println("B00g3rs3! is "+de.encrypt("B00g3rs3!"));
		System.out.println("B00g3rs#! is "+de.encrypt("B00g3rs#!"));
	}
	
	public void testDecryption() throws Exception {
		DataEncryption de = new DataEncryption(CRYPTOKEY);
		String decrypted = de.decrypt("SutzABsnhuWt8ieIy6gzYQ==");
		System.out.println(decrypted);
		assertFalse(decrypted.equals("SutzABsnhuWt8ieIy6gzYQ=="));
		assertTrue(decrypted.equals("Hello World"));
	}
	
	public void testAES() throws Exception {
		DataEncryption aes = new DataEncryption(CRYPTOKEY);
		String in = "H3ll0 W@rlD";
		String en = aes.encryptAES(in);
		System.out.println("["+in+"]");
		System.out.println(en);
		String un = aes.decryptAES(en);
		System.out.println("["+un+"]");
		assertTrue(in.equals(un));
	}

	public void testStringEncryption() throws Exception {
		FileEncryption fe = new FileEncryption(passphrase);
		String encrypted = fe.encryptString(testString, passphrase);
		System.out.println("testString has length="+testString.length());
		System.out.println("got "+encrypted);
		System.out.println("encrypted has length="+encrypted.length());
		String decrypted = fe.decryptString(encrypted, passphrase);
		System.out.println("got "+decrypted);
	}

	public void testFileEncryption() throws Exception {
		FileEncryption fe = new FileEncryption("Company#1");
		String testFile = "C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/usa.txt";
		String encname1 = fe.encryptFile(testFile);
		System.out.println("encname1="+encname1);
		testFile = "C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/usa.docx";
		String encname2 = fe.encryptFile(testFile);
		System.out.println("encname2="+encname2);
	}

	public void testFileDecryption() throws Exception {
		FileEncryption fe = new FileEncryption("Company#1");
		String testFile = "C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/24cb11b2-24cb-11b2-7621-ac9ca9f3315f.enc";
		String decname1 = fe.decryptFile(testFile);
		System.out.println("decname1="+decname1);
		testFile = "C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/748fbb3e-748f-bb3e-666f-470f60c2d38e.enc";
		String decname2 = fe.decryptFile(testFile);
		System.out.println("decname2="+decname2);
	}
}
