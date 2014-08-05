package code.messy.net.radius.attribute;

public class AcctInputPackets extends IntegerAttribute {
	public AcctInputPackets(int value) {
		super(47, value);
	}
	
	@Override
	public String toString() {
		return "AcctInputPackets=" + super.toString();
	}
}
