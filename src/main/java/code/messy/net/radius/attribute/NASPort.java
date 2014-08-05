package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class NASPort implements AttributeIF {
	int value;

	public NASPort(int value) {
		this.value = value;
	}

	@Override
	public ByteBuffer getPayload() {
		ByteBuffer bb = ByteBuffer.allocate(6);
		bb.put((byte)5);
		bb.put((byte)6);
		bb.putInt(value);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		return "NASPort=" + value;
	}
}
