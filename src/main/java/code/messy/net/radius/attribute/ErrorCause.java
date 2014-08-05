package code.messy.net.radius.attribute;

public class ErrorCause extends IntegerAttribute {
	public ErrorCause(int value) {
		super(101, value);
	}
	
	@Override
	public String toString() {
		return "ErrorCause=" + super.toString();
	}
}
