package code.messy.net.radius.attribute;

public class AcctAuthentic extends IntegerAttribute {
	public enum Type {
		RADIUS(1),
		Local(2), 
		Remote(3);
		
		int value;
		private Type(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	
	public AcctAuthentic(Type type) {
		super(45, type.getValue());
	}
	
	@Override
	public String toString() {
		return "AcctAuthentic=" + super.toString();
	}
}
