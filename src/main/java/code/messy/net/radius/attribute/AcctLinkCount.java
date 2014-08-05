package code.messy.net.radius.attribute;

public class AcctLinkCount extends IntegerAttribute {
	public AcctLinkCount(int value) {
		super(51, value);
	}
	
	@Override
	public String toString() {
		return "AcctLinkCount=" + super.toString();
	}
}
