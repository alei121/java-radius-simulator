package code.messy.net.radius.packet;

import java.nio.ByteBuffer;

public class AccessAccept extends RadiusPacket {
	public AccessAccept(ByteBuffer bb) {
		super(bb);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("AccessAccept ");
		sb.append(super.toString());
		return sb.toString();
	}
}
