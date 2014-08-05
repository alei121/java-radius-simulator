package code.messy.net.radius.attribute;

public class AcctSessionTime extends IntegerAttribute {
	public AcctSessionTime(int value) {
		super(46, value);
	}
	
	@Override
	public String toString() {
		return "AcctSessionTime=" + super.toString();
	}
}
