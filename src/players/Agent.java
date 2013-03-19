package players;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import utilities.Constants;
import utilities.Parser;
import utilities.WorldState;

public abstract class Agent {

	private DatagramSocket socket;
	protected WorldState world;
	private Parser parser;
	private InetAddress ip;
	private boolean goalie;
	private int role;

	public Agent(int role) {
		goalie = false;
		this.role = role;
		try {
			ip = InetAddress.getByName(Constants.Server.IP);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		world = new WorldState();
		parser = new Parser(world, socket);
		parser.start();

		initConnection();
		run();

	}

	public void moveFriendlyKickoff() {
		switch (role) {
		case Constants.Team.GOALIE:
		case Constants.Team.OUTER_LEFT_DEFENDER:
		case Constants.Team.INNER_LEFT_DEFENDER:
		case Constants.Team.INNER_RIGHT_DEFENDER:
		case Constants.Team.OUTER_RIGHT_DEFENDER:
		case Constants.Team.RIGHT_MID:
		case Constants.Team.MID_MID:
		case Constants.Team.LEFT_MID:
		case Constants.Team.LEFT_WING:
		case Constants.Team.CENTER_FORWARD:
			move(Constants.CenterForward.FRIENDLY_KICKOFF_X,
					Constants.CenterForward.FRIENDLY_KICKOFF_Y);
		case Constants.Team.RIGHT_WING:
		}
	}

	public boolean canSeeBall() {
		return world.getAngleToBall() == Constants.Params.NOT_DEFINED;
	}

	public boolean canSeeEnemyGoal() {
		return world.getAngleToEnemyGoal() == Constants.Params.NOT_DEFINED;
	}

	public abstract void run();

	private void move(int x, int y) {
		sendMessage("(move " + x + " " + y + ")");
	}

	protected void isGoalie() {
		goalie = true;
	}

	private void initConnection(){
		String msg = "(init " + Constants.Team.NAME + ")";
		if (goalie) {
			msg = "(init " + Constants.Team.NAME + " 1)";
		}
		sendMessage(msg);
	}

	public void dash(int power) {
		sendMessage("(dash " + power + ")");
	}

	public void turn(double direction) {
		sendMessage("(turn " + direction + ")");
	}

	public void kick(int power, double direction) {
		sendMessage("(kick " + power + " " + direction + ")");
	}

	/**
	 * Goalie only
	 */
	public void catchBall(double direction) {
		sendMessage("(catch " + direction + ")");
	}

	private void sendMessage(String message) {
		byte[] buf = message.getBytes();
		DatagramPacket msg = new DatagramPacket(buf, buf.length, ip,
				Constants.Server.PORT);
		try {
			socket.send(msg);
		} catch (IOException e) {
			System.err.println("IOException");
		}
	}

}
