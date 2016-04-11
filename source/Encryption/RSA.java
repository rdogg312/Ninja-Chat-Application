package Encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.Cipher;

public class RSA {

	private KeyPair keyPair;

	public RSA ( boolean generate ) {
		if ( generate ) {
			this.generateKeys ( 512 );
		}
	}

	private KeyPair generateKeys ( int length ) {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance ( "RSA" );
			keygen.initialize ( length );
			this.keyPair = keygen.generateKeyPair ();
			return this.keyPair;
		}
		catch ( Exception exception ) {
			return null;
		}
	}

	public PrivateKey getPrivate () {
		return this.keyPair.getPrivate ();
	}

	public PublicKey getPublic () {
		return this.keyPair.getPublic ();
	}

	public String exportPrivate () {
		return DatatypeConverter.printBase64Binary (
			this.keyPair.getPrivate ().getEncoded ()
		).replaceAll ( "(.{64})", "$1\n" );
	}

	public String exportPublic () {
		return DatatypeConverter.printBase64Binary (
			this.keyPair.getPublic ().getEncoded ()
		).replaceAll ( "(.{64})", "$1\n" );
	}

	public PrivateKey savePrivate ( String filepath ) {
		try {
			File file = new File ( filepath );
			file.getParentFile ().mkdirs ();
			file.createNewFile ();
			ObjectOutputStream privateKey = new ObjectOutputStream (
					new FileOutputStream ( file )
			);
			privateKey.writeObject ( this.keyPair.getPrivate () );
			privateKey.close ();
			return this.keyPair.getPrivate ();
		}
		catch ( Exception exception ) {
			return null;
		}
	}

	public PublicKey savePublic ( String filepath ) {
		try {
			File file = new File ( filepath );
			file.getParentFile ().mkdirs ();
			file.createNewFile ();
			ObjectOutputStream publicKey = new ObjectOutputStream (
					new FileOutputStream ( file )
			);
			publicKey.writeObject ( this.keyPair.getPrivate () );
			publicKey.close ();
			return this.keyPair.getPublic ();
		}
		catch ( Exception exception ) {
			return null;
		}
	}

	public static PublicKey loadPublic ( String filepath ) {
		File file = new File ( filepath );
		if ( file.exists () ) {
			try {
				ObjectInputStream input = new ObjectInputStream ( new FileInputStream ( filepath ) );
				return ( PublicKey ) input.readObject ();
			}
			catch ( Exception exception ) {}
		}
		return null;
	}

	public static PrivateKey loadPrivate ( String filepath ) {
		File file = new File ( filepath );
		if ( file.exists () ) {
			try {
				ObjectInputStream input = new ObjectInputStream ( new FileInputStream ( filepath ) );
				return ( PrivateKey ) input.readObject ();
			}
			catch ( Exception exception ) {}
		}
		return null;
	}

	public static String encrypt ( PublicKey key, String input ) {
		try {
			Cipher cipher = Cipher.getInstance ( "RSA" );
			cipher.init ( Cipher.ENCRYPT_MODE, key );
			byte [] output = cipher.doFinal ( input.getBytes () );
			return DatatypeConverter.printBase64Binary ( output );
		}
		catch ( Exception exception ) {
			return null;
		}
	}

	public static String decrypt ( PrivateKey key, String input ) {
		try {
			Cipher cipher = Cipher.getInstance ( "RSA" );
			cipher.init ( Cipher.DECRYPT_MODE, key );
			byte [] output = cipher.doFinal ( DatatypeConverter.parseBase64Binary ( input ) );
			return new String ( output );
		}
		catch ( Exception exception ) {
			return null;
		}
	}

	public String encrypt ( String input ) {
		return RSA.encrypt ( this.getPublic (), input );
	}

	public String decrypt ( String input ) {
		return RSA.decrypt ( this.getPrivate (), input );
	}

}