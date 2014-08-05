package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class TunnelType implements AttributeIF {
	int tag;
	int value;
	
	public TunnelType(byte[] value) {
		this.tag = value[0];
		this.value = value[3];
	}

	@Override
	public ByteBuffer getPayload() {
		ByteBuffer bb = ByteBuffer.allocate(6);
		bb.put((byte)64);
		bb.put((byte)6);
		bb.put((byte)tag);
		bb.put((byte)0);
		bb.put((byte)0);
		bb.put((byte)value);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		return "TunnelType=[tag=" + tag + ", value=" + value + "]";
	}
}
