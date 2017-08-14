package security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
 * 
 * @author www.codejava.net
 *
 */
public class XMLEncryption {
	private static Logger LOGGER = new Logging().create(XMLEncryption.class.getName(), false);
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";
	private static final int Key_Length = 128;
	private static final PublicKey pubKey = getPublicKey();
	private static final PrivateKey privKey = getPrivateKey();

	private static PrivateKey getPrivateKey() {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIaCOtGhSLT5w1lC90/qOSLcptI1CizM+ESns5CwCGkfiBJV0P1cBa3pyytMGJLyQ3ZubOFaLg1rgge2OvWJfjxGPXuH01KgA+ve7AgaJc3nSTOWRHpT59OJpjr8bwLWCSk3T/XsHOeYtNiYs3saqXT86dXnbqAAHqGEO7SIvBunAgMBAAECgYEAgXwChWT+qgE59cbKbyrqH85L5VXb2q7iXK0X5jQflJsfEY99JTkd2oKS67ZQfb0sBmlc9T8r0A1rWmMptM7MLnHOFo4DbuedW0DKEDgR597WAbEftNoMf7eumX5XNhvyMrYdXHxVtone8O8DjQYVRPzN/FGKPkH23bOT39ByQgkCQQDGwvjcY4pWUHEhQNzKxYYaoCxjoiHcNNcmCxoQbIVW34VgVInq1PQNHIfsJa66NgZ9ODsevTvcZzdTlLv3NdprAkEArT5uBfxQqjncXrhGlOvpivUgDRDYueqUAay59N1tyuU7S1zIBihdw01Q/JZRyg2LiSw83GUnoR3xjhuz+IGKtQJBAJaXZmHkcQ2QiQY+1Io1ObO2o6shTjI/PrsuMiM7dZMqay1fH9dyXS4J7dm3ezjOcgAxuGlDyrTk5wBGcluMy90CQExlT1dMYLXwNgF++YEKVz5zweKBH1E85G+on4gfbR8f3vnovDwa6N4K5j+8eezD9FhVLbFy2frunWqxRLe9SFECQCqqiIlCD8Qc3bRiZaKozpY8v+xZbyJbpJnApEJB/sZAgzDNV0PKK8RRUwzfWKinfaMOHdMiIAVPOVnXqEn6qm4="));
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");

			return kf.generatePrivate(spec);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Errors.getError(6));
			return null;
		}
	}

	private static PublicKey getPublicKey() {
		X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder()
				.decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGgjrRoUi0+cNZQvdP6jki3KbSNQoszPhEp7OQsAhpH4gSVdD9XAWt6csrTBiS8kN2bmzhWi4Na4IHtjr1iX48Rj17h9NSoAPr3uwIGiXN50kzlkR6U+fTiaY6/G8C1gkpN0/17BznmLTYmLN7Gql0/OnV526gAB6hhDu0iLwbpwIDAQAB"));
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");

			return kf.generatePublic(spec);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Errors.getError(6));
			return null;
		}
	}

	public static void encrypt(File inputFile, File outputFile) {
		try {
			/*KeyPairGenerator keyPGen = KeyPairGenerator.getInstance("RSA");
			KeyPair kp = keyPGen.generateKeyPair();
			PrivateKey privKey=kp.getPrivate();
			System.out.println("PRIV:"+Base64.getEncoder().encodeToString(privKey.getEncoded())+"~~END\n");
			PublicKey pubKey = kp.getPublic();
			System.out.println("PUB:"+Base64.getEncoder().encodeToString(pubKey.getEncoded())+"~~END\n");		
			*/
			
			KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
			keyGen.init(Key_Length);
			Key secretKey = keyGen.generateKey();
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
	        cipher.init(Cipher.ENCRYPT_MODE, privKey);
	        System.out.print(secretKey.getEncoded());
	        byte[] hex = secretKey.getEncoded();
	        hex = cipher.doFinal(hex);
	        hex = Base64.getEncoder().encode(hex);
	        System.out.println(hex.length);
	        /*
	        hex = Base64.getDecoder().decode(hex);
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
	        cipher.init(Cipher.DECRYPT_MODE, privKey);
			hex = cipher.doFinal(hex);
			System.out.println(hex);
			*/
			File f = new File(Settings.directory + "Key.txt");
			f.delete();
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(hex);
			fos.close();
			cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException ex) {
			LOGGER.log(Level.SEVERE, Errors.getError(4));
		}
	}

	public static byte[] decrypt(File inputFile) {
		try {
			File f = new File(Settings.directory + "Key.txt");

			FileInputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[172];
			fis.read(buffer);
			fis.close();
			buffer = Base64.getDecoder().decode(buffer);
			Cipher cipher = Cipher.getInstance("RSA");  
	        cipher.init(Cipher.DECRYPT_MODE, pubKey);
			buffer = cipher.doFinal(buffer);
			Key secretKey = new SecretKeySpec(buffer, ALGORITHM);
			cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			inputStream.close();
			return outputBytes;

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException ex) {
			LOGGER.log(Level.SEVERE, Errors.getError(5));
			return null;
		}
	}

	public static void LogtoDB() {
		LOGGER = new Logging().create(Settings.class.getName(), true);
	}
}
