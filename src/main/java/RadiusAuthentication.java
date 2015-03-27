

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import code.messy.net.radius.attribute.CallingStationID;
import code.messy.net.radius.attribute.CiscoVSA;
import code.messy.net.radius.attribute.FramedIPAddress;
import code.messy.net.radius.attribute.FramedIPNetmask;
import code.messy.net.radius.attribute.FramedIPv6Address;
import code.messy.net.radius.attribute.NASIPAddress;
import code.messy.net.radius.attribute.NASPort;
import code.messy.net.radius.attribute.NASPortID;
import code.messy.net.radius.attribute.NASPortType;
import code.messy.net.radius.attribute.UserName;
import code.messy.net.radius.attribute.UserPassword;
import code.messy.net.radius.packet.AccessRequest;
import code.messy.net.radius.packet.RadiusPacket;

public class RadiusAuthentication {
	final static String USERNAME = System.getProperty("USERNAME", "user1");
	final static String PASSWORD = System.getProperty("PASSWORD", "Lab123");
	final static String CALLING_STATION_ID = System.getProperty("CALLING_STATION_ID", "00:11:22:33:44:55");
	final static String AUDIT_SESSION_ID = System.getProperty("AUDIT_SESSION_ID", "101");
	final static String RADIUS_SECRET = System.getProperty("RADIUS_SECRET", "secret");
	final static String NAS_IP_ADDRESS = System.getProperty("NAS_IP_ADDRESS", "10.0.0.1");
	final static String FRAMED_IP_ADDRESS = System.getProperty("FRAMED_IP_ADDRESS", "1.2.3.4");
	final static String FRAMED_IP_MASK = System.getProperty("FRAMED_IP_MASK", "255.255.255.0");
	final static String NAS_PORT = System.getProperty("NAS_PORT");
	final static String NAS_PORT_ID = System.getProperty("NAS_PORT_ID");
	final static String NAS_PORT_TYPE = System.getProperty("NAS_PORT_TYPE");
	final static String FRAMED_IPv6_ADDRESS = System.getProperty("FRAMED_IPv6_ADDRESS");

	static byte[] requestAuthenticator = new byte[16];

	/**
	 * Main <ip>
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		InetAddress address = InetAddress.getByName(args[0]);
		int port = 1812;
		InetSocketAddress sa = new InetSocketAddress(address, port);
		DatagramChannel channel = DatagramChannel.open();
		channel.connect(sa);
		
		RadiusPacket resp = login(channel, USERNAME, PASSWORD);
		System.out.println(resp.toString());
		
		channel.close();
	}

	static RadiusPacket login(DatagramChannel channel, String username, String password) throws Exception {
		AccessRequest req = new AccessRequest(RADIUS_SECRET, requestAuthenticator);
		req.add(new UserName(username));
		req.add(new UserPassword(password, RADIUS_SECRET, requestAuthenticator));
		req.add(new NASIPAddress(InetAddress.getByName(NAS_IP_ADDRESS).getAddress()));
		req.add(new CallingStationID(CALLING_STATION_ID));
		req.add(new CiscoVSA("audit-session-id=" + AUDIT_SESSION_ID));
		if (FRAMED_IP_ADDRESS != null) {
			req.add(new FramedIPAddress(InetAddress.getByName(FRAMED_IP_ADDRESS).getAddress()));
			req.add(new FramedIPNetmask(InetAddress.getByName(FRAMED_IP_MASK).getAddress()));
		}
		if (NAS_PORT != null) {
			req.add(new NASPort(Integer.parseInt(NAS_PORT)));
		}
		if (NAS_PORT_ID != null) {
			req.add(new NASPortID(NAS_PORT_ID));
		}
		if (NAS_PORT_TYPE != null) {
			req.add(new NASPortType(Integer.parseInt(NAS_PORT_TYPE)));
		}
		if (FRAMED_IPv6_ADDRESS != null) {
			req.add(new FramedIPv6Address(InetAddress.getByName(FRAMED_IPv6_ADDRESS).getAddress()));
		}
		
		channel.write(req.getPayload());

		ByteBuffer bb = ByteBuffer.allocate(10 * 1024);
		channel.read(bb);
		bb.flip();
		
		return RadiusPacket.create(bb);
	}
}
