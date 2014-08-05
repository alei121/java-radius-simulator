package code.messy.net.radius.attribute;

public class AcctOutputOctets extends IntegerAttribute {
	public AcctOutputOctets(int value) {
		super(43, value);
	}
	
	@Override
	public String toString() {
		return "AcctOutputOctets=" + super.toString();
	}
}
