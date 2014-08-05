package code.messy.net.radius.attribute;

public class AcctStatusType extends IntegerAttribute {
	public enum Type {
		Start(1),
		Stop(2), 
		InterimUpdate(3);
		
		int value;
		private Type(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	
	public AcctStatusType(Type type) {
		super(40, type.getValue());
	}
	
	@Override
	public String toString() {
		return "AcctStatusType=" + super.toString();
	}
}
