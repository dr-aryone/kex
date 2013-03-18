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
	private boolean goalie;

	public Agent(int startX, int startY) throws IOException {
		goalie = false;
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
	
	public boolean canSeeBall() {
		return world.getAngleToBall() == Constants.Params.NOT_DEFINED;
	}

	public boolean canSeeEnemyGoal() {
		return world.getAngleToEnemyGoal() == Constants.Params.NOT_DEFINED;
	}
	
	public abstract void run() throws IOException;
	
	private void move(int x, int y) throws IOException {
		byte[] buf = (("(move " + x +" "+ y+ ")").getBytes());
		DatagramPacket msg = new DatagramPacket(buf, buf.length, ip,
				Constants.Server.PORT);
		socket.send(msg);
	}
	
	protected void isGoalie() {
		goalie = true;
	}

	private void initConnection() throws IOException {
		byte[] buf = (("(init " + Constants.Team.NAME + ")").getBytes());
		if(goalie) {
			buf = (("(init " + Constants.Team.NAME + " 1)").getBytes());
		}
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

	public void turn(double direction) throws IOException {
		byte[] buf = (("(turn " + direction + ")").getBytes());
		DatagramPacket msg = new DatagramPacket(buf, buf.length, ip,
				Constants.Server.PORT);
		socket.send(msg);
	}
	public void kick(int power, double direction) throws IOException {
		byte[] buf = (("(kick " + power + " "+direction+")").getBytes());
		DatagramPacket msg = new DatagramPacket(buf, buf.length, ip,
				Constants.Server.PORT);
		socket.send(msg);
	}
	/**
	 * Goalie only
	 */
	public void catchBall(double direction) throws IOException {
		byte[] buf = (("(catch "+direction+")").getBytes());
		DatagramPacket msg = new DatagramPacket(buf, buf.length, ip,
				Constants.Server.PORT);
		socket.send(msg);
	}

}
