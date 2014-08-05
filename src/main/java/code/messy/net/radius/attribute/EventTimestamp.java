package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;
import java.util.Date;


public class EventTimestamp implements AttributeIF {
	long timestamp;
	
	public EventTimestamp(byte[] value) {
		timestamp = 0;
		timestamp |= (value[0] & 0xFF) << 24;
		timestamp |= (value[1] & 0xFF) << 16;
		timestamp |= (value[2] & 0xFF) << 8;
		timestamp |= (value[3] & 0xFF);
		timestamp *= 1000;
	}
	
	@Override
	public ByteBuffer getPayload() {
		byte[] value = new byte[4];
		long timestampInSec = timestamp / 1000;
		
		for (int i = 0; i < 4; i++) {
			int offset = (4 - 1 - i) * 8;
			value[i] = (byte)((timestampInSec >> offset) & 0xFF);
		}
		
		ByteBuffer bb = ByteBuffer.allocate(6);
		bb.put((byte)55);
		bb.put((byte)6);
		bb.put(value);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		return "EventTimestamp=" + (new Date(timestamp)).toString();
	}
}
