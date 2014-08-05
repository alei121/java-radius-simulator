package code.messy.net.radius.packet;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import code.messy.net.radius.attribute.AttributeIF;

public class AccountingRequest {
	byte id;
	String secret;

	ArrayList<AttributeIF> attributes = new ArrayList<AttributeIF>();
	
	public AccountingRequest(String secret) {
		this.secret = secret;
	}

	public void add(AttributeIF attribute) {
		attributes.add(attribute);
	}

	public ByteBuffer getPayload() {
		id++;
		int authenticatorPos = 0;
		
		ByteBuffer bb = ByteBuffer.allocate(10 * 1024);
		bb.put((byte)4);
		bb.put(id);

		// skip length for later
		bb.putShort((short)0);
		
		// Authenticator. Put zeros first
		authenticatorPos = bb.position();
		for (int i = 0; i < 16; i++) {
			bb.put((byte)0);
		}
		
		for (AttributeIF attribute : attributes) {
			bb.put(attribute.getPayload());
		}
		
		bb.flip();
		
		// set length now
		bb.putShort(2, (short)bb.limit());

		// set authenticator now
		byte[] authenticator = RadiusPacket.getAuthenticator(bb, secret);
		bb.position(authenticatorPos);
		bb.put(authenticator);
		bb.rewind();
	
		return bb;
	}
}
