package code.messy.net.radius.attribute;

public class DNSServerIPv6Address extends IPv6AddressAttribute {
	public DNSServerIPv6Address(byte[] address) {
		super(169, address);
	}
	
	@Override
	public String toString() {
		return "FramedIPv6Address=" + super.toString();
	}
}
