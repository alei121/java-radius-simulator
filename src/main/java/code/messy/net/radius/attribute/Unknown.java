package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class Unknown implements AttributeIF {
	int code;
	byte[] value;
	
	public Unknown(int code, byte[] value) {
		this.code = code;
		this.value = value;
	}

	@Override
	public ByteBuffer getPayload() {
		int length = value.length + 2;
		ByteBuffer bb = ByteBuffer.allocate(length);
		bb.put((byte)code);
		bb.put((byte)length);
		bb.put(value);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		return "Unknown code=" + code + " length=" + value.length;
	}
}
