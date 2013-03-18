package players;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import utilities.Constants;
import utilities.Parser;
import utilities.WorldState;

public abstract class Agent {

	private DatagramSocket socket;
	protected WorldState world;
	private Parser parser;
	InetAddress ip;

	public Agent(int startX, int startY) throws IOException {
		ip = InetAddress.getByName(Constants.Server.IP);
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		world = new WorldState();
		parser = new Parser(world, socket);
		parser.start();

		initConnection();
		move(startX, startY);
		run();

	}
	
	public abstract void run() throws IOException;
	
	private void move(int x, int y) throws IOException {
		byte[] buf = (("(move " + x +" "+ y+ ")").getBytes());
		DatagramPacket msg = new DatagramPacket(buf, buf.length, ip,
				Constants.Server.PORT);
		socket.send(msg);
	}

	private void initConnection() throws IOException {
		byte[] buf = (("(init " + Constants.Team.NAME + ")").getBytes());
		DatagramPacket msg = new DatagramPacket(buf, buf.length, ip,
				Constants.Server.PORT);
		socket.send(msg);
	}

	public void dash(int power) throws IOException {
		byte[] buf = (("(dash " + power + ")").getBytes());
		DatagramPacket msg = new DatagramPacket(buf, buf.length, ip,
				Constants.Server.PORT);
		socket.send(msg);
	}

	public void turn(double d) throws IOException {
		byte[] buf = (("(turn " + d + ")").getBytes());
		DatagramPacket msg = new DatagramPacket(buf, buf.length, ip,
				Constants.Server.PORT);
		socket.send(msg);
	}

}
