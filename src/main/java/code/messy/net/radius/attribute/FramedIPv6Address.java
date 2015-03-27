package code.messy.net.radius.attribute;

public class FramedIPv6Address extends IPv6AddressAttribute {
	public FramedIPv6Address(byte[] address) {
		super(168, address);
	}
	
	@Override
	public String toString() {
		return "FramedIPv6Address=" + super.toString();
	}
}
