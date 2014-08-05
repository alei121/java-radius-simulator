package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class MessageAuthenticator implements AttributeIF {
	byte[] value;
	
	public MessageAuthenticator(byte[] value) {
		this.value = value;
	}

	@Override
	public ByteBuffer getPayload() {
		int length = value.length + 2;
		ByteBuffer bb = ByteBuffer.allocate(length);
		bb.put((byte)80);
		bb.put((byte)length);
		bb.put(value);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("MessageAuthenticator=");
		for (int i = 0; i < value.length; i++) {
			sb.append(Integer.toHexString(0xFF & value[i]));
		}
		return sb.toString();
	}
}
