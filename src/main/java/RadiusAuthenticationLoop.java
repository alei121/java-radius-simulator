

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import code.messy.net.radius.attribute.CallingStationID;
import code.messy.net.radius.attribute.CiscoVSA;
import code.messy.net.radius.attribute.FramedIPAddress;
import code.messy.net.radius.attribute.FramedIPNetmask;
import code.messy.net.radius.attribute.FramedIPv6Address;
import code.messy.net.radius.attribute.NASIPAddress;
import code.messy.net.radius.attribute.NASIPv6Address;
import code.messy.net.radius.attribute.NASPort;
import code.messy.net.radius.attribute.NASPortID;
import code.messy.net.radius.attribute.NASPortType;
import code.messy.net.radius.attribute.UserName;
import code.messy.net.radius.attribute.UserPassword;
import code.messy.net.radius.packet.AccessRequest;

public class RadiusAuthenticationLoop {
	final static String USERNAME = System.getProperty("USERNAME", "user1");
	final static String PASSWORD = System.getProperty("PASSWORD", "Lab123");
	final static String CALLING_STATION_ID = System.getProperty("CALLING_STATION_ID", "00:11:22:33:44:55");
	final static String AUDIT_SESSION_ID = System.getProperty("AUDIT_SESSION_ID", "101");
	final static String RADIUS_SECRET = System.getProperty("RADIUS_SECRET", "secret");
	final static String NAS_IP_ADDRESS = System.getProperty("NAS_IP_ADDRESS", "10.0.0.1");
	final static String FRAMED_IP_ADDRESS = System.getProperty("FRAMED_IP_ADDRESS");
	final static String FRAMED_IP_MASK = System.getProperty("FRAMED_IP_MASK", "255.255.255.0");
	final static String NAS_PORT = System.getProperty("NAS_PORT");
	final static String NAS_PORT_ID = System.getProperty("NAS_PORT_ID");
	final static String NAS_PORT_TYPE = System.getProperty("NAS_PORT_TYPE");
	final static String FRAMED_IPV6_ADDRESS = System.getProperty("FRAMED_IPV6_ADDRESS");
	final static String NAS_IPV6_ADDRESS = System.getProperty("NAS_IPV6_ADDRESS");

	static byte[] requestAuthenticator = new byte[16];

	static final int PRINT_AT_COUNT = 1024;
	static final int AUTHENTICATION_COUNT = 6000;
	static final int THREAD_COUNT = 3;
	static final int AUTHENTICATION_TIMEOUT_MS = 5000;
	
	static Executor executor;

	/**
	 * Main <ip> <authCount> <threadCount>
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("RadiusAuthenticationLoop <ip> <authCount> <threadCount>");
			System.exit(1);
		}
		int authCount = Integer.parseInt(args[1]);
		int threadCount = Integer.parseInt(args[2]);
		
		executor = Executors.newCachedThreadPool();
		for (int i = 0; i < threadCount; i++) {
			LoginThread loginThread = new LoginThread(args[0], authCount / threadCount);
			loginThread.start();			
		}
	}
	
	
	
	static class LoginThread extends Thread {
		static AtomicInteger currentThreadId = new AtomicInteger();
		String ip;
		int threadId;
		int count;

		long totalReceived = 0;
		long totalDelta = 0;
		long startTimes[] = new long[256];
		ByteBuffer payloads[] = new ByteBuffer[256];
		
		long lastPrintTime = System.currentTimeMillis();
		
		public LoginThread(String ip, int count) throws Exception {
			this.ip = ip;
			this.threadId = currentThreadId.getAndIncrement();
			this.count = count;
			generate256Payloads(payloads, USERNAME, PASSWORD);
		}
		
		@Override
		public void run() {
			try {
				InetAddress address = InetAddress.getByName(ip);
				int port = 1812;
				InetSocketAddress sa = new InetSocketAddress(address, port);
				DatagramChannel channel = DatagramChannel.open();
				channel.connect(sa);
				
				executor.execute(() -> {
					try {
						for (int i = 0; i < 256; i++) {
							startTimes[i] = System.currentTimeMillis();
							channel.write(payloads[i]);
							Thread.sleep(2);
						}					
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				});
				
				ByteBuffer bb = ByteBuffer.allocate(10 * 1024);
	
				for (int i = 0; i < count; i++) {
					bb.clear();
					channel.read(bb);
					bb.flip();
					int id = getIdOfRadiusResponse(bb);
					// ID do not come back in sequence
	
					long delta = System.currentTimeMillis() - startTimes[id];
					totalDelta += delta;
					totalReceived++;
					startTimes[id] = System.currentTimeMillis();
					payloads[id].position(0);
					channel.write(payloads[id]);
					if ((totalReceived % PRINT_AT_COUNT) == 0) {
						printAndResetStatistics();
					}
				}
				channel.close();
				printAndResetStatistics();
				printTimeouts();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void printAndResetStatistics() {
			long lastPrintDuration = System.currentTimeMillis() - lastPrintTime;
			long messageRate = (totalReceived * 1000L) / lastPrintDuration;
			long response = totalDelta / totalReceived;
			System.out.println("Thread(" + threadId + ") response(ms)=" + response + " rate(msg/s)=" + messageRate);
			totalReceived = 0;
			totalDelta = 0;
			lastPrintTime = System.currentTimeMillis();
		}
		
		private void printTimeouts() {
			long current = System.currentTimeMillis();
			int timeoutCount = 0;
			for (int i = 0; i < startTimes.length; i++) {
				if ((current - startTimes[i]) > AUTHENTICATION_TIMEOUT_MS) {
					timeoutCount++;
				}
			}
			System.out.println("Thread(" + threadId + ") timeouts or drops=" + timeoutCount);
		}

	}

	static int getIdOfRadiusResponse(ByteBuffer bb) {
		byte code = bb.get();
		// code must be 2 (Radius Accept)
		if (code != 2) {
			System.out.println("Wrong code " + code);
		}
		return bb.get() & 0xFF;
	}
	
	static void generate256Payloads(ByteBuffer payloads[], String username, String password) throws Exception {
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
		if (FRAMED_IPV6_ADDRESS != null) {
			String[] addresses = FRAMED_IPV6_ADDRESS.split(",");
			for (String address : addresses) {
				req.add(new FramedIPv6Address(InetAddress.getByName(address).getAddress()));
			}
		}
		if (NAS_IPV6_ADDRESS != null) {
			req.add(new NASIPv6Address(InetAddress.getByName(NAS_IPV6_ADDRESS).getAddress()));
		}
		
		for (int i = 0; i < 256; i++) {
			payloads[i] = req.getPayload((byte)i);
		}
	}
}
