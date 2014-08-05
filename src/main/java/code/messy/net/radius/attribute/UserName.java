package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class UserName implements AttributeIF {
	String name;
	
	public UserName(byte[] b) {
		name = new String(b);
	}
	
	public UserName(String name) {
		this.name = name;
	}
	
	@Override
	public ByteBuffer getPayload() {
		byte[] value = name.getBytes();
		int length = value.length + 2;
		ByteBuffer bb = ByteBuffer.allocate(length);
		bb.put((byte)1);
		bb.put((byte)length);
		bb.put(value);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		return "UserName=" + name;
	}
}
