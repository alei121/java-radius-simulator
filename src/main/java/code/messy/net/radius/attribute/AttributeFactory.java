package code.messy.net.radius.attribute;


public class AttributeFactory {
	public static AttributeIF createAttribute(int type, byte[] value) {
		switch (type) {
		case 1:
			return new UserName(value);
		case 4:
			return new NASIPAddress(value);
		case 8:
			return new FramedIPAddress(value);
		case 9:
			return new FramedIPNetmask(value);
		case 24:
			return new State(value);
		case 25:
			return new Class(value);
		case 26:
			return new VendorSpecific(value);
		case 29:
			return new TerminationAction(value);
		case 30:
			return new CalledStationID(value);
		case 31:
			return new CallingStationID(value);
		case 55:
			return new EventTimestamp(value);
		case 64:
			return new TunnelType(value);
		case 65:
			return new TunnelMediumType(value);
		case 81:
			return new TunnelPrivateGroupID(value);
		case 80:
			return new MessageAuthenticator(value);
		}
		return new Unknown(type, value);
	}
}
