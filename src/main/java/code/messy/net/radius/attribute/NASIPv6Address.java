package code.messy.net.radius.attribute;

public class NASIPv6Address extends IPv6AddressAttribute {
	public NASIPv6Address(byte[] address) {
		super(95, address);
	}
	
	@Override
	public String toString() {
		return "NASIPv6Address=" + super.toString();
	}
}
