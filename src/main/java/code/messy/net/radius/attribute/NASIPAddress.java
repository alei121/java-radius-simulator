package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;


public class NASIPAddress implements AttributeIF {
	// must be 4 bytes
	byte[] address;
	
	public NASIPAddress(byte[] address) {
		this.address = address;
	}
	
	@Override
	public ByteBuffer getPayload() {
		ByteBuffer bb = ByteBuffer.allocate(6);
		bb.put((byte)4);
		bb.put((byte)6);
		bb.put(address);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("NASIPAddress=");
		sb.append(address[0] & 0xFF);
		sb.append('.');
		sb.append(address[1] & 0xFF);
		sb.append('.');
		sb.append(address[2] & 0xFF);
		sb.append('.');
		sb.append(address[3] & 0xFF);
		return sb.toString();
	}
}
