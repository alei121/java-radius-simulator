package code.messy.net.radius.attribute;

public class DelegatedIPv6PrefixPool extends StringAttribute {
	public DelegatedIPv6PrefixPool(String value) {
		super(171, value);
	}
	
	@Override
	public String toString() {
		return "DelegatedIPv6PrefixPool=" + super.toString();
	}
}
