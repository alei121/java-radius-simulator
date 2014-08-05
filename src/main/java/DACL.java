

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

import code.messy.net.radius.attribute.AttributeIF;
import code.messy.net.radius.attribute.CallingStationID;
import code.messy.net.radius.attribute.CiscoVSA;
import code.messy.net.radius.attribute.FramedIPAddress;
import code.messy.net.radius.attribute.FramedIPNetmask;
import code.messy.net.radius.attribute.NASIPAddress;
import code.messy.net.radius.attribute.State;
import code.messy.net.radius.attribute.UserName;
import code.messy.net.radius.attribute.UserPassword;
import code.messy.net.radius.packet.AccessChallenge;
import code.messy.net.radius.packet.AccessRequest;
import code.messy.net.radius.packet.RadiusPacket;

public class DACL {
	final static String USERNAME = "nicdummy";
	final static String PASSWORD = "Lab123";
	final static String CALLING_STATION_ID = "00:11:22:33:44:55";
	final static String RADIUS_SECRET = "secret";
	final static byte[] NAS_IP_ADDRESS = { (byte)172, 21, 74, (byte)213 };
	final static byte[] FRAMED_IP_ADDRESS = { 1, 2, 3, 4 };
	final static byte[] FRAMED_IP_MASK = { (byte)255, (byte)255, (byte)255, 0 };

	static final String CISCO_DACL_VSA = "ACS:CiscoSecure-Defined-ACL";
	
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
		resp.dump();
		
		String aclName = resp.getVSA(CISCO_DACL_VSA);
		System.out.println();
		System.out.println("aclName=" + aclName);
		if (aclName != null) {
			dACL(channel, aclName);
		}
		
		channel.close();
	}

	static RadiusPacket login(DatagramChannel channel, String username, String password) throws Exception {
		AccessRequest req = new AccessRequest(RADIUS_SECRET, requestAuthenticator);
		req.add(new UserName(username));
		req.add(new UserPassword(password, RADIUS_SECRET, requestAuthenticator));
		req.add(new NASIPAddress(NAS_IP_ADDRESS));
		req.add(new CallingStationID(CALLING_STATION_ID));
		req.add(new CiscoVSA("audit-session-id=123"));
		req.add(new FramedIPAddress(FRAMED_IP_ADDRESS));
		req.add(new FramedIPNetmask(FRAMED_IP_MASK));
		channel.write(req.getPayload());

		ByteBuffer bb = ByteBuffer.allocate(10 * 1024);
		channel.read(bb);
		bb.flip();
		
		return RadiusPacket.create(bb);
	}
	
	/**
	 * dACL is cisco proprietary to overload radius for downloading ACL
	 * 
	 * Mechanism describe here:
	 * http://www.cisco.com/en/US/docs/security/asa/asa70/configuration/guide/fwaaa.html#wp1056570
	 * 
	 * @param channel
	 * @param aclName
	 * @throws Exception
	 */
	static void dACL(DatagramChannel channel, String aclName) throws Exception {
		// TODO simply downloading now
		// TODO need to check on last packet behavior (AccessAccept or AccessChallenge)
		
		AccessRequest req;
		req = new AccessRequest(RADIUS_SECRET, requestAuthenticator);
		req.setMessageAuthenticator(true);
		req.add(new UserName(aclName));
		req.add(new CiscoVSA("aaa:service=ip-admission"));
		req.add(new CiscoVSA("aaa:event=acl-download"));

		channel.write(req.getPayload());
		
		ByteBuffer bb = ByteBuffer.allocate(10 * 1024);
		channel.read(bb);
		bb.flip();
		
		// Access-Challenge for intermediate packets
		while (bb.get(0) == 11) {
			State state = null;
			AccessChallenge ac = new AccessChallenge(bb);
			ArrayList<AttributeIF> attributes = ac.getAttributes();
			for (AttributeIF attribute : attributes) {
				if (attribute instanceof State) state = (State)attribute;
			}
			if (state == null) throw new Exception("Missing state");
			
			req = new AccessRequest(RADIUS_SECRET, requestAuthenticator);
			req.setMessageAuthenticator(true);
			req.add(new UserName(aclName));
			req.add(new CiscoVSA("aaa:service=ip-admission"));
			req.add(new CiscoVSA("aaa:event=acl-download"));
			req.add(state);

			channel.write(req.getPayload());

			bb.clear();
			channel.read(bb);
			bb.flip();
		}
		
		// should be AccessAccept for last packet
		if (bb.get(0) != 2) throw new Exception("No Access-Accept");
	}
}
