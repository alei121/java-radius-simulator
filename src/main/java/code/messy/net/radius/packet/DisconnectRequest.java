package code.messy.net.radius.packet;

import java.nio.ByteBuffer;

public class DisconnectRequest extends RadiusPacket {
	public DisconnectRequest(ByteBuffer bb) {
		super(bb);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("DisconnectRequest ");
		sb.append(super.toString());
		return sb.toString();
	}
}
