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
	protected Queue<String> queue;
	private boolean hasMoved;

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
		if (hasMoved)
			return;
		hasMoved = true;
		int mult = 1;
		if (world.isRightSide()) {
			turn(180);
			mult = -1;
		}
		switch (role) {
		case Constants.Team.GOALIE:
			move(Constants.Goalie.START_X, Constants.Goalie.START_Y);
			break;
		case Constants.Team.OUTER_LEFT_DEFENDER:
		case Constants.Team.INNER_LEFT_DEFENDER:
		case Constants.Team.INNER_RIGHT_DEFENDER:
		case Constants.Team.OUTER_RIGHT_DEFENDER:
		case Constants.Team.RIGHT_MID:
		case Constants.Team.MID_MID:
		case Constants.Team.LEFT_MID:
		case Constants.Team.LEFT_WING:
			move(Constants.LeftWing.FRIENDLY_KICKOFF_X, mult
					* Constants.LeftWing.FRIENDLY_KICKOFF_Y);
			break;
		case Constants.Team.CENTER_FORWARD:
			move(Constants.CenterForward.FRIENDLY_KICKOFF_X, mult
					* Constants.CenterForward.FRIENDLY_KICKOFF_Y);
			break;
		case Constants.Team.RIGHT_WING:
			move(Constants.RightWing.FRIENDLY_KICKOFF_X, mult
					* Constants.RightWing.FRIENDLY_KICKOFF_Y);
			break;
		}
	}

	public boolean canSeeFriendlyGoal() {
		boolean canSeeGoalFlag = world.getAngleToFriendlyGoal() != Constants.Params.NOT_DEFINED;
		String side = world.isRightSide() ? "r" : "l";
		boolean canSeeGoalPost1 = world.getAngleToObject("f g " + side + " b") != Constants.Params.NOT_DEFINED;
		boolean canSeeGoalPost2 = world.getAngleToObject("f g " + side + " t") != Constants.Params.NOT_DEFINED;
		return canSeeGoalFlag && canSeeGoalPost1 && canSeeGoalPost2;
	}

	public boolean canSeeBall() {
		return world.getAngleToBall() != Constants.Params.NOT_DEFINED;
	}

	public boolean canSeeEnemyGoal() {
		return world.getAngleToEnemyGoal() != Constants.Params.NOT_DEFINED;
	}

	public abstract void run();

	private void move(double x, double y) {
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
		}
	}

	public void kick(int power, double direction) {
		queue.add("(kick " + power + " " + direction + ")");
	}

	public void runToBall() {
		if (Math.abs(world.getAngleToBall()) > 20) {
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

	/**
	 * @return target name if there is any, null otherwise
	 */
	public String getPassTarget() {
		int enemyGoalAngle = world.getAngleToEnemyGoal();
		String target = null;
		String currTarget = "p \"" + Constants.Team.NAME + "\"";
		if (!isLookingBack()) {
			for (int i = 1; i <= 12; i++) {
				int angle = world.getAngleToObject(currTarget);
				if (angle == Constants.Params.NOT_DEFINED)
					continue;
				double distance = world.getDistanceToObject(currTarget);
				if (Math.abs(angle) < Constants.Params.FORWARD_PASSING_ANGLE
						|| Math.abs(angle - enemyGoalAngle) < Constants.Params.FORWARD_PASSING_ANGLE) {
					if (distance > Constants.Params.FORWARD_PASSING_DISTANCE) {
						if (target == null
								|| world.getDistanceToObject(target) < distance) {
							target = currTarget;
						}
					}
				}
				currTarget = "p \"" + Constants.Team.NAME + "\" " + i;
			}
		}
		return target;
	}

	private int getPassingPower(double distance) {
		int power = (int) (60 + distance * 2);
		return Math.min(power, 100);
	}

	public void passForward(String target) {
		System.out.println("PASSING " + target);
		kick(getPassingPower(world.getDistanceToObject(target)),
				world.getAngleToObject(target));
	}

	public void dribble() {
		if (canSeeEnemyGoal()) {
			kick(Constants.Params.DRIBBLING_KICK_POWER,
					world.getAngleToEnemyGoal());
		} else {
			int angle = 0;
			if (isLookingBack()) {
				angle = 180;
			} else if (isLookingLeft()) {
				angle = 100;
			} else if (isLookingRight()) {
				angle = -100;
			}
			kick(Constants.Params.DRIBBLING_KICK_POWER, angle);
		}
	}

	private boolean isLookingBack() {
		String[] leftFlags = { "f l b 10", "f l b 20", "f l b 30", "f l t 10",
				"f l t 20", "f l t 30" };
		String[] rightFlags = { "f r b 10", "f r b 20", "f r b 30", "f r t 10",
				"f r t 20", "f r t 30" };
		String[] behindFlags = world.isLeftSide() ? leftFlags : rightFlags;
		for (String flag : behindFlags) {
			if (world.getAngleToObject(flag) < Constants.Params.CLOSE_ANGLE) {
				return true;
			}
		}
		return false;
	}

	private boolean isLookingLeft() {
		String[] topFlags = { "f t l 10", "f t l 30", "f t l 50", "f t 0",
				"f t r 10", "f t r 30", "f t r 50" };
		String[] bottomFlags = { "f b l 10", "f b l 30", "f b l 50", "f b 0",
				"f b r 10", "f b r 30", "f b r 50" };
		String[] leftFlags = world.isLeftSide() ? topFlags : bottomFlags;
		for (String flag : leftFlags) {
			if (world.getAngleToObject(flag) < Constants.Params.CLOSE_ANGLE) {
				return true;
			}
		}
		return false;
	}

	private boolean isLookingRight() {
		String[] topFlags = { "f t l 10", "f t l 30", "f t l 50", "f t 0",
				"f t r 10", "f t r 30", "f t r 50" };
		String[] bottomFlags = { "f b l 10", "f b l 30", "f b l 50", "f b 0",
				"f b r 10", "f b r 30", "f b r 50" };
		String[] rightFlags = world.isRightSide() ? topFlags : bottomFlags;
		for (String flag : rightFlags) {
			if (world.getAngleToObject(flag) < Constants.Params.CLOSE_ANGLE) {
				return true;
			}
		}
		return false;
	}

	private void sendMessage(String message) {
		message += "\u0000";
		byte[] buf = message.getBytes();
		DatagramPacket msg = new DatagramPacket(buf, buf.length,
				Constants.Server.IP, Constants.Server.PORT);
		try {
			socket.send(msg);
		} catch (IOException e) {
			System.err.println("IOException");
		}
	}

}
