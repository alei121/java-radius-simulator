package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class TerminationAction implements AttributeIF {
	int value;
	
	public TerminationAction(byte[] value) {
		this.value = value[3];
	}

	@Override
	public ByteBuffer getPayload() {
		ByteBuffer bb = ByteBuffer.allocate(6);
		bb.put((byte)29);
		bb.put((byte)6);
		bb.put((byte)0);
		bb.put((byte)0);
		bb.put((byte)0);
		bb.put((byte)value);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		return "TerminationAction=" + value;
	}
}
