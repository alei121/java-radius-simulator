package code.messy.net.radius.attribute;

public class AcctOutputPackets extends IntegerAttribute {
	public AcctOutputPackets(int value) {
		super(48, value);
	}
	
	@Override
	public String toString() {
		return "AcctOutputPackets=" + super.toString();
	}
}
