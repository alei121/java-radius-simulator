package code.messy.net.radius.packet;

import java.nio.ByteBuffer;

public class CoARequest extends RadiusPacket {
	public CoARequest(ByteBuffer bb) {
		super(bb);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("CoARequest ");
		sb.append(super.toString());
		return sb.toString();
	}
}
