package players;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import utilities.Constants;
import utilities.MyTimer;
import utilities.Parser;
import utilities.TimeListener;
import utilities.WorldState;

public abstract class Agent implements TimeListener {

	private DatagramSocket socket;
	protected WorldState world;
	private Parser parser;
	private boolean goalie;
	private int role;
	private boolean hasMoved;
	protected Queue<String> queue;

	public Agent(int role) {
		if (role == Constants.Team.GOALIE) {
			goalie = true;
		} else {
			goalie = false;
		}
		queue = new LinkedList<String>();
		this.role = role;
		try {
			Constants.Server.IP = InetAddress
					.getByName(Constants.Server.INIT_ADDRESS);
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

		MyTimer t = new MyTimer();
		t.addListener(this);
		t.start();

		initConnection();
		run();

	}

	@Override
	public void newCycle() {
		if (!queue.isEmpty()) {
			sendMessage(queue.poll());
		}
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
		return world.getAngleToBall() != Constants.Params.NOT_DEFINED;
	}

	public boolean canSeeEnemyGoal() {
		return world.getAngleToEnemyGoal() != Constants.Params.NOT_DEFINED;
	}

	public abstract void run();

	private void move(int x, int y) {
		queue.add("(move " + x + " " + y + ")");
	}

	private void initConnection() {
		String msg = "(init " + Constants.Team.NAME + " (version 15))";
		if (goalie) {
			msg = "(init " + Constants.Team.NAME + " (version 15) (goalie))";
		}
		queue.add(msg);

	}

	public void dash(int power, double direction) {
		queue.add("(dash " + power + " " + direction + ")");
	}

	public void turn(int direction) {
		updateAngles(direction);
		queue.add("(turn " + direction + ")");
	}

	private void updateAngles(int direction) {
		String key;
		Integer previousAngle, newAngle;
		HashMap<String, Integer> angleToObjects = world.getAngleToObjects();
		Set<String> keys = angleToObjects.keySet();
		for (Iterator<String> i = keys.iterator(); i.hasNext();) {
			key = i.next();
			previousAngle = angleToObjects.get(key);
			newAngle = previousAngle - direction;
			if (newAngle > 180) {
				newAngle = -1 * (360 - newAngle);
			} else if (newAngle < -180) {
				newAngle = 360 + newAngle;
			}
			angleToObjects.put(key, newAngle);
			if(key.equals("b")) {
				System.out.println("BOLLEEEEEEEEEEEEEN pre: "+ previousAngle + "new: " + newAngle);
			}
		}
	}

	public void kick(int power, double direction) {
		queue.add("(kick " + power + " " + direction + ")");
	}

	public void runToBall() {
		if (Math.abs(world.getAngleToBall()) > 10) {
			System.out.println(world.getAngleToBall());
			turn(world.getAngleToBall());
		} else {
			dash(100, world.getAngleToBall());
		}
	}

	/**
	 * Goalie only
	 */
	public void catchBall(double direction) {
		if (goalie) {
			queue.add("(catch " + direction + ")");
		}
	}

	private void sendMessage(String message) {
		message += "\u0000";
		byte[] buf = message.getBytes();
		DatagramPacket msg = new DatagramPacket(buf, buf.length,
				Constants.Server.IP, Constants.Server.PORT);
		try {
			socket.send(msg);
			System.out.println(message);
		} catch (IOException e) {
			System.err.println("IOException");
		}
	}

}
