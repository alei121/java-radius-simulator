package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class IntegerAttribute implements AttributeIF {
	int type;
	int value;

	public IntegerAttribute(int type, int value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public ByteBuffer getPayload() {
		ByteBuffer bb = ByteBuffer.allocate(6);
		bb.put((byte)type);
		bb.put((byte)6);
		bb.putInt(value);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		return "Integer=" + value;
	}
}
