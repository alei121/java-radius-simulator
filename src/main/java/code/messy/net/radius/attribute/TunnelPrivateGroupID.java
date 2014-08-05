package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class TunnelPrivateGroupID implements AttributeIF {
	int tag;
	String id;
	
	public TunnelPrivateGroupID(byte[] value) {
		this.tag = value[0];
		id = new String(value, 1, value.length -1);
	}

	@Override
	public ByteBuffer getPayload() {
		int length = id.length() + 2;
		ByteBuffer bb = ByteBuffer.allocate(length);
		bb.put((byte)81);
		bb.put((byte)length);
		bb.put((byte)tag);
		bb.put(id.getBytes());
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		return "TunnelPrivateGroupID=[tag=" + tag + ", id=" + id + "]";
	}
}
