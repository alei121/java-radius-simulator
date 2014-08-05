
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import code.messy.net.radius.attribute.ErrorCause;
import code.messy.net.radius.attribute.ReplyMessage;
import code.messy.net.radius.packet.CoAResponse;
import code.messy.net.radius.packet.DisconnectResponse;
import code.messy.net.radius.packet.RadiusPacket;
import code.messy.net.radius.packet.RadiusResponse;

public class DynAuthListener {
	final static String RADIUS_SECRET = System.getProperty("RADIUS_SECRET",
			"secret");
	final static String RADIUS_PORT = System.getProperty("RADIUS_PORT", "1700");
	final static String REPLY_MESSAGE = System.getProperty("REPLY_MESSAGE");
	final static String ERROR_CAUSE = System.getProperty("ERROR_CAUSE");

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		int port = Integer.parseInt(RADIUS_PORT);
		InetSocketAddress sa;
		if (args.length > 0) {
			InetAddress address = InetAddress.getByName(args[0]);
			sa = new InetSocketAddress(address, port);
		} else {
			sa = new InetSocketAddress(port);
		}
		DatagramChannel channel = DatagramChannel.open();
		channel.socket().bind(sa);
		System.out.println("DynAuthListener listening");

		ByteBuffer bb = ByteBuffer.allocate(10 * 1024);

		while (true) {
			bb.clear();
			SocketAddress sender = channel.receive(bb);
			bb.flip();

			RadiusPacket req = RadiusPacket.create(bb);
			System.out.println("Received from " + sender);
			System.out.println(req.toString());

			RadiusResponse response = null;
			
			switch (req.getCode()) {
			case 40:
				if (ERROR_CAUSE == null) {
					response = new DisconnectResponse(true, req.getId(),
							RADIUS_SECRET, req.getAuthenticator());
				}
				else {
					response = new DisconnectResponse(false, req.getId(),
							RADIUS_SECRET, req.getAuthenticator());	
				}
				break;
			case 43:
				if (ERROR_CAUSE == null) {
					response = new CoAResponse(true, req.getId(),
						RADIUS_SECRET, req.getAuthenticator());
				}
				else {
					response = new CoAResponse(false, req.getId(),
							RADIUS_SECRET, req.getAuthenticator());
				}
				break;
			}

			if (response != null) {
				if (ERROR_CAUSE != null) {
					response.add(new ErrorCause(Integer.parseInt(ERROR_CAUSE)));
				}
				if (REPLY_MESSAGE != null) {
					response.add(new ReplyMessage(REPLY_MESSAGE));
				}
				channel.send(response.getPayload(), sender);
			}
		
		}
	}
}
