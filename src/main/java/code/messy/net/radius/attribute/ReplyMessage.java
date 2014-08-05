package code.messy.net.radius.attribute;

public class ReplyMessage extends StringAttribute {
	public ReplyMessage(String message) {
		super(18, message);
	}
	
	@Override
	public String toString() {
		return "ReplyMessage=" + super.toString();
	}
}
