package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class UserPassword implements AttributeIF {
	String password;
	String secret;
	byte[] requestAuthenticator;
	
	public UserPassword(String password, String secret, byte[] requestAuthenticator) {
		this.password = password;
		this.secret = secret;
		this.requestAuthenticator = requestAuthenticator;
	}
	
	@Override
	public ByteBuffer getPayload() {
		byte[] encodedPassword = encode(password);
		
		int length = encodedPassword.length + 2;
		ByteBuffer bb = ByteBuffer.allocate(length);
		bb.put((byte)2);
		bb.put((byte)length);
		bb.put(encodedPassword);
		bb.flip();
		return bb;
	}
	
	private byte[] encode(String password) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
		byte[] b = password.getBytes();
		int chunkCount = (b.length / 16) + 1;
		byte[][] chunks = new byte[chunkCount][16];
		
		for (int i = 0; i < chunkCount; i++) {
			int start = i * 16;
			int length;
			if ((start + 16) > b.length) length = b.length - start;
			else length = 16;
			System.arraycopy(b, i * 16, chunks[i], 0, length);
		}
		
		byte[][] ciphertextBlocks = new byte[chunkCount][16];
		for (int i = 0; i < chunkCount; i++) {
			md5.reset();
			md5.update(secret.getBytes());
			if (i == 0) md5.update(requestAuthenticator);
			else md5.update(ciphertextBlocks[i - 1]);
			byte[] digest = md5.digest();
			
			for (int j = 0; j < 16; j++) {
				ciphertextBlocks[i][j] = (byte)(chunks[i][j] ^ digest[j]);
			}
		}		
		
		byte[] result = new byte[chunkCount * 16];
		for (int i = 0; i < chunkCount; i++) {
			System.arraycopy(ciphertextBlocks[i], 0, result, i * 16, 16);
		}
		return result;
	}

}
