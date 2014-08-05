package code.messy.net.radius.packet;


public class CoAResponse extends RadiusResponse {
	// CoA ACK
	public CoAResponse(boolean success, int id, String secret, byte[] requestAuthenticator) {
		super(success ? 44 : 45, id, secret, requestAuthenticator);
	}
	
	public CoAResponse(int id, String secret, byte[] requestAuthenticator) {
		this(true, id, secret, requestAuthenticator);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("CoAResponse ");
		sb.append(super.toString());
		return sb.toString();
	}
}
