package code.messy.net.radius.attribute;

public class StatefulIPv6AddressPool extends StringAttribute {
	public StatefulIPv6AddressPool(String value) {
		super(172, value);
	}
	
	@Override
	public String toString() {
		return "StatefulIPv6AddressPool=" + super.toString();
	}
}
