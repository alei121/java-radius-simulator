package code.messy.net.radius.packet;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import code.messy.net.radius.attribute.AttributeIF;
import code.messy.net.radius.attribute.AttributeFactory;
import code.messy.net.radius.attribute.VendorSpecific;

public class RadiusPacket {
	private int code;
	private int id;
	private int length;
	private byte[] authenticator = new byte[16];
	
	private ArrayList<AttributeIF> attributes = new ArrayList<AttributeIF>();

	/**
	 * For creation
	 * @param code
	 */
	public RadiusPacket(int code) {
		this.code = code;
	}
	
	public RadiusPacket(ByteBuffer bb) {
		code = bb.get();
		id = bb.get();
		length = bb.getShort();
		bb.get(authenticator);
		
		while (bb.remaining() > 0) {
			int type = bb.get();
			int length = bb.get();
			byte[] value = new byte[length - 2];
			bb.get(value);
			
			attributes.add(AttributeFactory.createAttribute(type, value));
		}
	}

	public int getCode() {
		return code;
	}

	public int getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

	public byte[] getAuthenticator() {
		return authenticator;
	}

	public ArrayList<AttributeIF> getAttributes() {
		return attributes;
	}
	
	public String getVSA(String name) {
		for (AttributeIF attribute : attributes) {
			if (attribute instanceof VendorSpecific) {
				VendorSpecific vs = (VendorSpecific)attribute;
				String value = vs.getCiscoVSA().get(name);
				if (value != null) return value;
			}
		}
		return null;
	}

	public void dump() {
		System.out.println("code=" + code + " id=" + id + " length=" + length);
		System.out.print("authenticator=");
		for (int i = 0; i < 16; i++) {
			System.out.print(Integer.toHexString(0xFF & authenticator[i]));
		}
		System.out.println();
		System.out.println("Attributes={");
		for (AttributeIF attribute : attributes) {
			System.out.println("    " + attribute.toString());
		}
		System.out.println("}");
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("code=" + code + " id=" + id + " length=" + length + "\n");
		sb.append("authenticator=");
		for (int i = 0; i < 16; i++) {
			sb.append(Integer.toHexString(0xFF & authenticator[i]));
		}
		sb.append('\n');
		sb.append("Attributes={\n");
		for (AttributeIF attribute : attributes) {
			sb.append("    " + attribute.toString() + "\n");
		}
		sb.append("}\n");
		return sb.toString();
	}
	
	public static RadiusPacket create(ByteBuffer bb) {
		int code = bb.get(0);
		switch (code) {
		case 2:
			return new AccessAccept(bb);
		case 3:
			return new AccessReject(bb);
		case 5:
			return new AccountingResponse(bb);
		case 11:
			return new AccessChallenge(bb);
		case 40:
			return new DisconnectRequest(bb);
		case 43:
			return new CoARequest(bb);
		}
		return null;
	}
	
	public static byte[] getAuthenticator(ByteBuffer bb, String secret) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
		md5.update(bb);
		bb.rewind();

		md5.update(secret.getBytes());
		return md5.digest();
	}
}
