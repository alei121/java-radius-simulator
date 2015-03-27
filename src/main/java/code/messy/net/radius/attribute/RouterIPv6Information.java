package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;

public class RouterIPv6Information implements AttributeIF {
	// must be 0-16 bytes
	byte[] prefix;
	
	public RouterIPv6Information(byte[] prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public ByteBuffer getPayload() {
		ByteBuffer bb = ByteBuffer.allocate(20);
		bb.put((byte)170);
		bb.put((byte)prefix.length);
		bb.put(prefix);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("RouterIPv6Information=");
		for (int i = 0; i < prefix.length; i++) {
			sb.append(String.format("%02X", prefix[i] & 0xFF));
			if (i < (prefix.length - 1)) {
				if ((i % 2) == 1) sb.append(":");
			}
		}
		return sb.toString();
	}
}
