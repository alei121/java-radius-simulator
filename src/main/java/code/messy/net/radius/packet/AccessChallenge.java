package code.messy.net.radius.packet;

import java.nio.ByteBuffer;

public class AccessChallenge extends RadiusPacket {
	public AccessChallenge(ByteBuffer bb) {
		super(bb);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("AccessChallege ");
		sb.append(super.toString());
		return sb.toString();
	}
}
