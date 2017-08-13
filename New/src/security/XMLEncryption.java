package security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import general.Errors;
import general.Logging;
import general.Settings;

/**
 * A utility class that encrypts or decrypts a file. 
 * @author www.codejava.net
 *
 */
public class XMLEncryption {
	private static Logger LOGGER = new Logging().create(XMLEncryption.class.getName(), false);
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";
	private static final int Key_Length=128;

	public static void encrypt(File inputFile, File outputFile)
			 {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
			keyGen.init(Key_Length);
			Key secretKey = keyGen.generateKey();
			byte[] hex = Base64.getEncoder().encode(secretKey.getEncoded());
			File f = new File(Settings.directory+"Key.txt");
			f.delete();
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(hex);
			fos.close();
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			
			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);
			
			byte[] outputBytes = cipher.doFinal(inputBytes);
			
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);
			
			inputStream.close();
			outputStream.close();
			
		} catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException ex) {
			LOGGER.log(Level.SEVERE, Errors.getError(4));
		}
	}

	public static byte[] decrypt(File inputFile)
			 {
		try {
			//Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			
			File f = new File(Settings.directory+"Key.txt");
			
			FileInputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[24];
			fis.read(buffer);
			fis.close();
			buffer=Base64.getDecoder().decode(buffer);
			Key secretKey = new SecretKeySpec(buffer, ALGORITHM);
			
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			
			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);
			
			byte[] outputBytes = cipher.doFinal(inputBytes);
			
			
			inputStream.close();
			return outputBytes;
			
		} catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException ex) {
			LOGGER.log(Level.SEVERE, Errors.getError(5));
			return null;
		}
	}
	
	public static void LogtoDB() {
		LOGGER = new Logging().create(Settings.class.getName(), true);
	}
}
