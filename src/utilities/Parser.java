package utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Parser extends Thread {

	WorldState world;
	DatagramSocket socket;
	private int notSinceBallSince = 100;
	private int notSeenLeftGoalSince = 100;
	private int notSeenRightGoalSince = 100;

	public Parser(WorldState world, DatagramSocket socket) {
		this.world = world;
		this.socket = socket;
	}

	public void run() {
		while (true) {
			byte[] receiveData = new byte[1024];
			DatagramPacket p = new DatagramPacket(receiveData,
					receiveData.length);
			try {
				socket.receive(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String message = new String(p.getData());
			parse(message);

		}
	}

	private void parse(String message) {
		System.out.println(message);
		if (message.contains("hear")) {
			processAudio(message);
		} else if (message.contains("see")) {
			if (message.contains("ball")) {
				setBallState(message);
			} else {
				cantSeeBall();
			}
			if (message.contains("goal r")) {
				setRightGoalState(message);
			} else {
				cantSeeRightGoal();
			}
			if (message.contains("goal l")) {
				setLeftGoalState(message);
			} else {
				cantSeeLeftGoal();
			}
		} else if (message.contains("init")) {
			if (message.contains(" l ")) {
				world.setLeftSide(true);
			} else {
				world.setLeftSide(false);
			}
			if (message.contains("before_kick_off")) {
				world.setState(WorldState.BEFORE_KICK_OFF);
			}
		}
	}

	private void processAudio(String message) {
		if (message.contains("kick_off_l")) {
			if (world.isLeftSide()) {
				world.setState(WorldState.FRIENDLY_KICK_OFF);
			} else {
				world.setState(WorldState.ENEMY_KICK_OFF);
			}
		} else if (message.contains("kick_off_r")) {
			if (world.isRightSide()) {
				world.setState(WorldState.FRIENDLY_KICK_OFF);
			} else {
				world.setState(WorldState.ENEMY_KICK_OFF);
			}
		} else if (message.contains("play_on")) {
			world.setState(WorldState.PLAY_ON);
		}
	}

	private void cantSeeBall() {
		if (notSinceBallSince > 5)
			world.setAngleToBall(Constants.Params.NOT_DEFINED);
		else
			notSinceBallSince++;

	}

	private void setBallState(String message) {
		String[] params = message.split("(ball)")[1].split("[ \\)]");
		world.setDistToBall(Double.parseDouble(params[2]));
		world.setAngleToBall(Double.parseDouble(params[3]));
	}

	private void cantSeeLeftGoal() {
		if (notSeenLeftGoalSince > 5) {
			if (world.isLeftSide()) {
				world.setAngleToFriendlyGoal(Constants.Params.NOT_DEFINED);
			} else {
				world.setAngleToEnemyGoal(Constants.Params.NOT_DEFINED);
			}
		} else {
			notSeenLeftGoalSince++;
		}
	}

	private void cantSeeRightGoal() {
		if (notSeenRightGoalSince > 10) {
			if (world.isRightSide()) {
				world.setAngleToFriendlyGoal(Constants.Params.NOT_DEFINED);
			} else {
				world.setAngleToEnemyGoal(Constants.Params.NOT_DEFINED);
			}
		} else {
			notSeenRightGoalSince++;
		}

	}

	private void setLeftGoalState(String message) {
		if (world.isRightSide()) {
			String[] params = message.split("(goal l)")[1].split("[ \\)]");
			world.setDistToEnemyGoal(Double.parseDouble(params[2]));
			world.setAngleToEnemyGoal(Double.parseDouble(params[3]));
		} else {
			String[] params = message.split("(goal l)")[1].split("[ \\)]");
			world.setDistToFriendlyGoal(Double.parseDouble(params[2]));
			world.setAngleToFriendlyGoal(Double.parseDouble(params[3]));
		}

	}

	private void setRightGoalState(String message) {
		if (world.isLeftSide()) {
			String[] params = message.split("(goal r)")[1].split("[ \\)]");
			world.setDistToEnemyGoal(Double.parseDouble(params[2]));
			world.setAngleToEnemyGoal(Double.parseDouble(params[3]));
		} else {
			String[] params = message.split("(goal r)")[1].split("[ \\)]");
			world.setDistToFriendlyGoal(Double.parseDouble(params[2]));
			world.setAngleToFriendlyGoal(Double.parseDouble(params[3]));
		}

	}
}
