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
			properParse(message);

		}
	}

	private void properParse(String message) {
		int firstSpace = message.indexOf(' ');
		String command = message.substring(1, firstSpace);
		message = message.substring(firstSpace, message.length() - 1);
		if (command.equals("see")) {
			parseSee(message);
		} else if (command.equals("sense_body")) {

		} else if (command.equals("hear")) {
			parseAural(message);
		} else if (command.equals("fullstate")) {

		} else if (command.equals("error")) {
			// Do we even care about errors?
		} else if (command.equals("player_type")) {

		} else if (command.equals("server_param")) {

		} else if (command.equals("player_param")) {

		} else if (command.equals("init")) {

		}
	}

	private void parseAural(String message) {
		ParseContext<Integer> timeContext = parseTime(message);
		message = timeContext.message;
		int time = timeContext.parsedData;

		
		
	}

	private void parseSee(String message) {
		ParseContext<Integer> timeContext = parseTime(message);
		message = timeContext.message;
		int time = timeContext.parsedData;
		boolean hasNext = true;

		while (hasNext && message.length() > 2) {
			ParseContext<String> itemContext = null;
			try {
				itemContext = parseItem(message);
			} catch (java.lang.StringIndexOutOfBoundsException e) {
				break;
			}
			message = itemContext.message;
			String item = itemContext.parsedData;

			world.sawObjectAtTime(item, time);

			int rightPara = message.indexOf(')');
			String[] params = message.substring(0, rightPara).split(" ");
			try {
				message = message.substring(rightPara + 2);
			} catch (java.lang.StringIndexOutOfBoundsException e) {
				hasNext = false;
			}

			if (params.length > 0) {
				world.angleToObject(item, Double.parseDouble(params[0]));
			}
			if (params.length > 1) {
				world.distanceToObject(item, Integer.parseInt(params[1]));
			}

		}

	}

	private ParseContext<String> parseItem(String message) {
		int rightPara = message.indexOf(')');
		String item = message.substring(2, rightPara);
		message = message.substring(rightPara + 2, message.length() - 1);
		return new ParseContext<String>(message, item);
	}

	private ParseContext<Integer> parseTime(String message) {
		int firstSpace = message.indexOf(' ', 1);
		int time = Integer.parseInt(message.substring(1, firstSpace));
		message = message.substring(firstSpace + 1, message.length() - 1);
		return new ParseContext<Integer>(message, time);
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
		if (notSeenRightGoalSince > 5) {
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
