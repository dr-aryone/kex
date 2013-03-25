package players;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;

import utilities.Constants;
import utilities.Parser;
import utilities.TimeListener;
import utilities.WorldState;

public abstract class Agent implements TimeListener{

	private DatagramSocket socket;
	protected WorldState world;
	private Parser parser;
	private InetAddress ip;
	private boolean goalie;
	private int role;
	private boolean hasMoved;

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
		hasMoved = false;
		initConnection();
		run();

	}
	

	@Override
	public void newCycle() {
		
	}

	public void moveFriendlyKickoff() {
		if (!hasMoved) {
			if (world.isRightSide()) {
				turn(180);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			switch (role) {
			case Constants.Team.GOALIE:
				move(-50, 0);
				break;
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
				break;
			case Constants.Team.RIGHT_WING:
			}
			hasMoved = true;
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

	private void initConnection() {
		String msg = "(init " + Constants.Team.NAME + "(version 13))";
		if (goalie) {
			msg = "(init " + Constants.Team.NAME + " (version 13) 1)";
		}
		sendMessage(msg);
	}

	public void dash(int power, double direction) {
		sendMessage("(dash " + power + " " + direction + ")");
	}

	public void turn(double direction) {
		updateAngles(direction);
		sendMessage("(turn " + direction + ")");
	}

	private void updateAngles(double direction) {
		Set<String> keys = world.getAngleToObjects().keySet();
		for(Iterator<String> i = keys.iterator(); i.hasNext();) {
			
		}
	}

	public void kick(int power, double direction) {
		sendMessage("(kick " + power + " " + direction + ")");
	}
	
	public void runToBall() {
		if (world.getAngleToEnemyGoal() != Constants.Params.NOT_DEFINED) {
			if (Math.abs(world.getAngleToBall()) > 10 && world.getDistToBall() > 5) {
				turn(world.getAngleToBall());
			} else {
				dash(100, world.getAngleToBall());
			}
		} else {
			turn(45);
		}
	}

	/**
	 * Goalie only
	 */
	public void catchBall(double direction) {
		if (goalie) {
			sendMessage("(catch " + direction + ")");
		}
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
