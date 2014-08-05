package code.messy.net.radius.attribute;

public class AcctSessionID extends StringAttribute {
	public AcctSessionID(String ID) {
		super(44, ID);
	}
	
	@Override
	public String toString() {
		return "AcctSessionID=" + super.toString();
	}
}
