package code.messy.net.radius.packet;

import java.nio.ByteBuffer;

public class AccountingResponse extends RadiusPacket {
	public AccountingResponse(ByteBuffer bb) {
		super(bb);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("AccountingResponse ");
		sb.append(super.toString());
		return sb.toString();
	}
}
