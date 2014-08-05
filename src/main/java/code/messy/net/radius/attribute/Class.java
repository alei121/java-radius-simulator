package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;

public class Class implements AttributeIF {
	byte[] value;
	
	public Class(byte[] value) {
		this.value = value;
	}

	@Override
	public ByteBuffer getPayload() {
		int length = value.length + 2;
		ByteBuffer bb = ByteBuffer.allocate(length);
		bb.put((byte)24);
		bb.put((byte)length);
		bb.put(value);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		return "Class=" + new String(value);
	}
}
