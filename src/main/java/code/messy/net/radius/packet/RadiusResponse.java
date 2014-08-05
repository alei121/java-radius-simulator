package code.messy.net.radius.packet;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import code.messy.net.radius.attribute.AttributeIF;

public class RadiusResponse {
	private int code;
	private int id;
	private String secret;
	private byte[] requestAuthenticator;

	
	private ArrayList<AttributeIF> attributes = new ArrayList<AttributeIF>();

	/**
	 * For creation
	 * @param code
	 */
	public RadiusResponse(int code, int id, String secret, byte[] requestAuthenticator) {
		this.code = code;
		this.id = id;
		this.secret = secret;
		this.requestAuthenticator = requestAuthenticator;
	}

	public void add(AttributeIF attribute) {
		attributes.add(attribute);
	}
	
	public ArrayList<AttributeIF> getAttributes() {
		return attributes;
	}
	
	// Trying out
	public ByteBuffer getPayload() {
		int responseAuthenticatorPos = 0;
		int messageAuthenticatorPos = 0;
		
		ByteBuffer bb = ByteBuffer.allocate(10 * 1024);
		bb.put((byte)code);
		bb.put((byte)id);

		// skip length for later
		bb.putShort((short)0);
		
		// Response authenticator. Use request authenticator first
		responseAuthenticatorPos = bb.position();
		bb.put(requestAuthenticator);

		// Message authenticator. Zero first
		bb.put((byte)80);
		bb.put((byte)18);
		messageAuthenticatorPos = bb.position();
		for (int i = 0; i < 16; i++) {
			bb.put((byte)0);
		}
		
		for (AttributeIF attribute : attributes) {
			bb.put(attribute.getPayload());
		}
		
		bb.flip();
		
		// set length now
		bb.putShort(2, (short)bb.limit());

		// set message authenticator now
		byte[] authenticator = hmac(bb);
		bb.position(messageAuthenticatorPos);
		bb.put(authenticator);
		bb.rewind();
		
		// set response authenticator now
		authenticator = RadiusPacket.getAuthenticator(bb, secret);
		bb.position(responseAuthenticatorPos);
		bb.put(authenticator);
		bb.rewind();

		return bb;
	}

	private byte[] hmac(ByteBuffer text) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

		// Prepare Key
		byte[] key = secret.getBytes();
		if (key.length > 64) {
			key = md5.digest(key);
			md5.reset();
		}

		// Prepare inner and outer pads
		byte[] kIpad = new byte[64];
		byte[] kOpad = new byte[64];
		System.arraycopy(key, 0, kIpad, 0, key.length);
		System.arraycopy(key, 0, kOpad, 0, key.length);
		for (int i = 0; i < 64; i++) {
			kIpad[i] ^= 0x36;
			kOpad[i] ^= 0x5C;
		}
		
		md5.update(kIpad);
		md5.update(text);
		byte[] firstPass = md5.digest();
		md5.reset();
		md5.update(kOpad);
		return md5.digest(firstPass);
	}
}
