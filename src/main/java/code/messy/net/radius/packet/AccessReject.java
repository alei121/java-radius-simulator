package code.messy.net.radius.packet;

import java.nio.ByteBuffer;

public class AccessReject extends RadiusPacket {
	public AccessReject(ByteBuffer bb) {
		super(bb);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("AccessReject ");
		sb.append(super.toString());
		return sb.toString();
	}
}
