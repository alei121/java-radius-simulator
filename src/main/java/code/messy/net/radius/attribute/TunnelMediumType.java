package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class TunnelMediumType implements AttributeIF {
	int tag;
	int value;
	
	public TunnelMediumType(byte[] value) {
		this.tag = value[0];
		this.value = value[3];
	}

	@Override
	public ByteBuffer getPayload() {
		ByteBuffer bb = ByteBuffer.allocate(6);
		bb.put((byte)65);
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
		return "TunnelMediumType=[tag=" + tag + ", value=" + value + "]";
	}
}
