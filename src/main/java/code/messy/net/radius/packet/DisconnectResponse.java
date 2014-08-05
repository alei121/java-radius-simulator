package code.messy.net.radius.packet;


public class DisconnectResponse extends RadiusResponse {
	// Disconnect ACK/NAK
	public DisconnectResponse(boolean success, int id, String secret, byte[] requestAuthenticator) {
		super(success ? 41 : 42, id, secret, requestAuthenticator);
	}
	
	public DisconnectResponse(int id, String secret, byte[] requestAuthenticator) {
		this(true, id, secret, requestAuthenticator);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("DisconnectResponse ");
		sb.append(super.toString());
		return sb.toString();
	}
}
