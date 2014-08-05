package code.messy.net.radius.attribute;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Set;


public class VendorSpecific implements AttributeIF {
	byte[] value;
	int vendorId;
	HashMap<String, String> vsa = new HashMap<String, String>();
	
	public VendorSpecific(byte[] value) {
		this.value = value;
		ByteBuffer bb = ByteBuffer.wrap(value);
		vendorId = bb.getInt();

		// Only process Cisco vsa
		if (vendorId == 9) {
			while (bb.remaining() > 0) {
				int type = bb.get();
				int len = bb.get() - 2;
				if (type == 1) {
					byte[] b = new byte[len];
					bb.get(b);
					String s = new String(b);
					int equalIndex = s.indexOf('=');
					String k = s.substring(0, equalIndex);
					String v = s.substring(equalIndex + 1);
					vsa.put(k, v);
				} else {
					bb.position(bb.position() + len);
				}
			}
		}
	}

	public HashMap<String, String> getCiscoVSA() {
		return vsa;
	}
	
	@Override
	public ByteBuffer getPayload() {
		
		// TODO require to build value from vsa
		
		int length = value.length + 2;
		ByteBuffer bb = ByteBuffer.allocate(length);
		bb.put((byte)26);
		bb.put((byte)length);
		bb.put(value);
		bb.flip();
		return bb;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("vendorId=" + vendorId);
		sb.append(" vsa=[");
		Set<String> keys = vsa.keySet();
		for (String key : keys) {
			String value = vsa.get(key);
			sb.append(key);
			sb.append('=');
			sb.append(value);
			sb.append(',');
		}
		sb.append(']');
		return sb.toString();
	}
}
