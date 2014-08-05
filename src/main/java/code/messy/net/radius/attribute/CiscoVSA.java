package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class CiscoVSA implements AttributeIF {
	String value;
	
	public CiscoVSA(String value) {
		this.value = value;
	}
	
	@Override
	public ByteBuffer getPayload() {
		int length = value.length() + 2 + 4 + 2;
		ByteBuffer bb = ByteBuffer.allocate(length);
		bb.put((byte)26);
		bb.put((byte)length);
		bb.putInt(9);
		
		bb.put((byte)1);
		byte[] b = value.getBytes();
		bb.put((byte)(b.length + 2));
		bb.put(b);
		bb.flip();
		return bb;
	}
}
