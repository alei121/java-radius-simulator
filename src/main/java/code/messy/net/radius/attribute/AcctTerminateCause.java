package code.messy.net.radius.attribute;

public class AcctTerminateCause extends IntegerAttribute {
	public enum Type {
		UserRequest(1),
		LostCarrier(2), 
		LostService(3),
		IdleTimeout(4),
		SessionTimeout(5),
		AdminReset(6),
		AdminReboot(7),
		PortError(8),
		NASError(9),
		NASRequest(10),
		NASReboot(11),
		PortUnneeded(12),
		PortPreempted(13),
		PortSuspended(14),
		ServiceUnavailable(15),
		Callback(16),
		UserError(17),
		HostRequest(18);
		
		int value;
		private Type(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	
	public AcctTerminateCause(Type type) {
		super(49, type.getValue());
	}
	
	@Override
	public String toString() {
		return "AcctTerminateCause=" + super.toString();
	}
}
