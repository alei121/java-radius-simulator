package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;

public class StringAttribute implements AttributeIF {
	int type;
	String value;
	
	public StringAttribute(int type, String value) {
		this.type = type;
		this.value = value;
	}

	public StringAttribute(int type, byte[] b) {
		this(type, new String(b));
	}
		
	@Override
	public ByteBuffer getPayload() {
		byte[] b = value.getBytes();
		int length = b.length + 2;
		ByteBuffer bb = ByteBuffer.allocate(length);
		bb.put((byte)type);
		bb.put((byte)length);
		bb.put(b);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		return "String=" + value;
	}
}
