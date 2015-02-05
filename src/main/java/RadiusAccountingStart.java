

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import code.messy.net.radius.attribute.AcctAuthentic;
import code.messy.net.radius.attribute.AcctDelayTime;
import code.messy.net.radius.attribute.AcctInputOctets;
import code.messy.net.radius.attribute.AcctInputPackets;
import code.messy.net.radius.attribute.AcctLinkCount;
import code.messy.net.radius.attribute.AcctMultiSessionID;
import code.messy.net.radius.attribute.AcctOutputOctets;
import code.messy.net.radius.attribute.AcctOutputPackets;
import code.messy.net.radius.attribute.AcctSessionID;
import code.messy.net.radius.attribute.AcctSessionTime;
import code.messy.net.radius.attribute.AcctStatusType;
import code.messy.net.radius.attribute.CallingStationID;
import code.messy.net.radius.attribute.CiscoVSA;
import code.messy.net.radius.attribute.FramedIPAddress;
import code.messy.net.radius.attribute.FramedIPNetmask;
import code.messy.net.radius.attribute.NASIPAddress;
import code.messy.net.radius.attribute.NASPort;
import code.messy.net.radius.attribute.NASPortID;
import code.messy.net.radius.attribute.NASPortType;
import code.messy.net.radius.attribute.UserName;
import code.messy.net.radius.attribute.UserPassword;
import code.messy.net.radius.packet.AccountingRequest;
import code.messy.net.radius.packet.RadiusPacket;

public class RadiusAccountingStart {
	final static String USERNAME = System.getProperty("USERNAME", "user1");
	final static String PASSWORD = System.getProperty("PASSWORD", "Lab123");
	final static String CALLING_STATION_ID = System.getProperty("CALLING_STATION_ID", "00:11:22:33:44:55");
	final static String AUDIT_SESSION_ID = System.getProperty("AUDIT_SESSION_ID", "101");
	final static String ACCT_SESSION_ID = System.getProperty("ACCT_SESSION_ID", "101");
	final static String RADIUS_SECRET = System.getProperty("RADIUS_SECRET", "secret");
	final static String NAS_IP_ADDRESS = System.getProperty("NAS_IP_ADDRESS", "10.0.0.1");
	final static String FRAMED_IP_ADDRESS = System.getProperty("FRAMED_IP_ADDRESS");
	final static String FRAMED_IP_MASK = System.getProperty("FRAMED_IP_MASK", "255.255.255.0");
	final static String NAS_PORT = System.getProperty("NAS_PORT");
	final static String NAS_PORT_ID = System.getProperty("NAS_PORT_ID");
	final static String NAS_PORT_TYPE = System.getProperty("NAS_PORT_TYPE");

	static byte[] requestAuthenticator = new byte[16];

	/**
	 * Main <ip>
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		InetAddress address = InetAddress.getByName(args[0]);
		int port = 1813;
		InetSocketAddress sa = new InetSocketAddress(address, port);
		DatagramChannel channel = DatagramChannel.open();
		channel.connect(sa);
		
		RadiusPacket resp = start(channel, USERNAME, PASSWORD);
		System.out.println(resp.toString());
		
		channel.close();
	}

	static RadiusPacket start(DatagramChannel channel, String username, String password) throws Exception {
		AccountingRequest req = new AccountingRequest(RADIUS_SECRET);
		req.add(new UserName(username));
		req.add(new UserPassword(password, RADIUS_SECRET, requestAuthenticator));
		req.add(new NASIPAddress(InetAddress.getByName(NAS_IP_ADDRESS).getAddress()));

		req.add(new AcctStatusType(AcctStatusType.Type.Start));
		req.add(new AcctDelayTime(1));
		req.add(new AcctInputOctets(2));
		req.add(new AcctOutputOctets(3));
		req.add(new AcctSessionID(ACCT_SESSION_ID));
		req.add(new AcctAuthentic(AcctAuthentic.Type.RADIUS));
		req.add(new AcctSessionTime(4));
		req.add(new AcctInputPackets(5));
		req.add(new AcctOutputPackets(6));
		req.add(new AcctMultiSessionID("321"));
		req.add(new AcctLinkCount(1));
		

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
		if (CALLING_STATION_ID != null) {
			req.add(new CallingStationID(CALLING_STATION_ID));
		}
		
		channel.write(req.getPayload());

		ByteBuffer bb = ByteBuffer.allocate(10 * 1024);
		channel.read(bb);
		bb.flip();
		
		return RadiusPacket.create(bb);
	}
}
