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
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCBvLhQKT9+F+qhN0JmblnST15xywcqCGEEXENiyIjBXz2fmmPa/Io6SajQ/uEB/jDNdAlsQJDfAg+gfSzdmbM6DlTUHn8ToSSClV4eLFPigthlIJb1XhYkhoEZSCeB5V+oTHrqRxdcZagby6mEv2Ez4Y4ZshxRhm2e9CCKyPoF7sNfz0Yar2e87EkcbWqTsu2NE94DCrjfAOF0nv82uuf3jl4XAHLk1syQmMqIRmd+fxrw2hwWWlG2te3VzxXzFvcX94t2DZFefaF2K+deN5qZiziRsnSW6vFpmBigxvGjRY+8dmw9b2ub/OGwH4Ms3uGQyREs6auJgXGQ3W41o/qvAgMBAAECggEATJzSRtwk12RuNOe5+V0fA7fE8PPQ9jxedkG5tWLwhyij7lUwtIe/ZO5Ml6Z73z5pOVpVmiyCoH7KFZ712KLEGPuPknez2Tampcpo+u54VuGnwgg9vQXjk4wuPmzCj82Q8ITBbQmwPOr3G/rnzzFoyEdhL8FjQa1dnKlTckF+i/N6K87eVQu8l0Hp3mueXDxMFBLOoJKqT6fY1GJqhW1zf7hiHplyt8+iYB8TkV+4TjCQyL31/PaST9WdUsCqzdhCl3jhH1bM/0fkduEcAKvZn6gJ4k/RO57cMmegAjfNrPPdYhJx0/AqvmIT2xhkk6fsFT805TECac+vle6sokMvCQKBgQDWZV8ncXNWXc1k6TILYteno5MxCnbGW5Ogcx1czU3q+0QogxSl5NIp9O1RcsUnuq67RMqjo1Be8MRpmTW1Uo4UFIfs3tPMrADm330NjsXU+W28YXRajcXFhtyt9SfGsmgwRmLd7yC8+9/MsPC9/Xv7IExfBQQKsKTfPDmu9RIKEwKBgQCa6bjZR2GW6Vjek0hOJTreS5FrdJIaQMm+5cTSXRiM8wDilqSEddUPaQK0YgKLXcE7ijoyiAEOYyvl2xICF2OpjWKIDXdMEaKuA+30+pSjAAnyrxccQWI9mTCTWI+NBTcHJAA6YZU85K1nCgrmeHT3iZ8ZQHH4inmyg6SOlLEgdQKBgQC24FVgMbeuLekrcHbNm4xUZIUDSeeynx63uDmNlVonXBjxbY5JS3uxakicNwfRMb4l4zHujznZIgqIALX9auPiYPjopdGIPheu12e2DtjVJ8Xg8EftjgzqxnSqtfkt1bRlqURgBr+oNDjcQvm3NedxeEVsr7yY2cBo4thZUpYGnQKBgDQN8YVhjRNu2eQAzb0kmYF1Fypqg2W6+4/gGaEukz9ZsKRzRlkUV0HUYYpHnN9D49ij/CZauTAidu/IC2cUgPLKhZ9+FuZCzs12mJFHWobjTnR0DWn7No3IWiBJFAOpCvKlUEZN4B4UIYiMQVlshLFhV0re8u03aulnfvZCVLq5AoGAcIhkkvxkAx7086xQ7iSsize0Qw+UuNrUTB1BPVNYE2KSNSJs8jeMbJ2bCmLSdEIe/pAbSXLK1RIXgFsOVQwwoDW9zhkGEJHLThOJlTif66NCpCzv/x+45rC0jg+hZ/YRdkGsyTM3/9cj2+yP/7sOiE+6jPmLZRpQS/vn+R1yvuU="));
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
				.decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgby4UCk/fhfqoTdCZm5Z0k9eccsHKghhBFxDYsiIwV89n5pj2vyKOkmo0P7hAf4wzXQJbECQ3wIPoH0s3ZmzOg5U1B5/E6EkgpVeHixT4oLYZSCW9V4WJIaBGUgngeVfqEx66kcXXGWoG8uphL9hM+GOGbIcUYZtnvQgisj6Be7DX89GGq9nvOxJHG1qk7LtjRPeAwq43wDhdJ7/Nrrn945eFwBy5NbMkJjKiEZnfn8a8NocFlpRtrXt1c8V8xb3F/eLdg2RXn2hdivnXjeamYs4kbJ0lurxaZgYoMbxo0WPvHZsPW9rm/zhsB+DLN7hkMkRLOmriYFxkN1uNaP6rwIDAQAB"));
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
			KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
			keyGen.init(Key_Length);
			Key secretKey = keyGen.generateKey();
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
	        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
	        System.out.print(secretKey.getEncoded());
	        byte[] hex = secretKey.getEncoded();
	        hex = cipher.doFinal(hex);
	        hex = Base64.getEncoder().encode(hex);
	        System.out.println(hex.length);
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
			byte[] buffer = new byte[344];
			fis.read(buffer);
			fis.close();
			buffer = Base64.getDecoder().decode(buffer);
			Cipher cipher = Cipher.getInstance("RSA");  
	        cipher.init(Cipher.DECRYPT_MODE, privKey);
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
