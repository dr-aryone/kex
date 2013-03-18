package utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Parser extends Thread {

	WorldState world;
	DatagramSocket socket;
	private int notSinceBallSince;

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
			if (message.contains("kick_off")) {
				world.setState(WorldState.KICK_OFF);
			} else if (message.contains("play_on")) {
				world.setState(WorldState.PLAY_ON);
			}
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
		}
	}

	private void cantSeeBall() {
		if(notSinceBallSince > 5)
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
		if (world.isLeftSide()) {
			world.setAngleToFriendlyGoal(Constants.Params.NOT_DEFINED);
		} else {
			world.setAngleToEnemyGoal(Constants.Params.NOT_DEFINED);
		}

	}

	private void cantSeeRightGoal() {
		if (world.isRightSide()) {
			world.setAngleToFriendlyGoal(Constants.Params.NOT_DEFINED);
		} else {
			world.setAngleToEnemyGoal(Constants.Params.NOT_DEFINED);
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
