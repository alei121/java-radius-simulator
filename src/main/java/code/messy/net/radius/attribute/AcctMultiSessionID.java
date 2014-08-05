package code.messy.net.radius.attribute;

public class AcctMultiSessionID extends StringAttribute {
	public AcctMultiSessionID(String ID) {
		super(50, ID);
	}
	
	@Override
	public String toString() {
		return "AcctMultiSessionID=" + super.toString();
	}
}
