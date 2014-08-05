package code.messy.net.radius.attribute;

public class AcctDelayTime extends IntegerAttribute {
	public AcctDelayTime(int value) {
		super(41, value);
	}
	
	@Override
	public String toString() {
		return "AcctDelayTime=" + super.toString();
	}
}
