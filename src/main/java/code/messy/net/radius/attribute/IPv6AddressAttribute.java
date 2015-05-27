package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;

public class IPv6AddressAttribute implements AttributeIF {
	// must be 16 bytes
	int type;
	byte[] address;
	
	public IPv6AddressAttribute(int type, byte[] address) {
		if (address.length != 16) throw new IllegalArgumentException("IPv6 length=" + address.length);
		this.type = type;
		this.address = address;
	}
	
	@Override
	public ByteBuffer getPayload() {
		ByteBuffer bb = ByteBuffer.allocate(18);
		bb.put((byte)type);
		bb.put((byte)18);
		bb.put(address);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("IPv6Address=");
		for (int i = 0; i < address.length; i++) {
			sb.append(String.format("%02X", address[i] & 0xFF));
			if (i < (address.length - 1)) {
				if ((i % 2) == 1) sb.append(":");
			}
		}
		return sb.toString();
	}
}
