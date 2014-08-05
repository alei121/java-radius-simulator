package code.messy.net.radius.attribute;

public class AcctInputOctets extends IntegerAttribute {
	public AcctInputOctets(int value) {
		super(42, value);
	}
	
	@Override
	public String toString() {
		return "AcctInputOctets=" + super.toString();
	}
}
