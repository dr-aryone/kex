package utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Parser extends Thread {

	private static final int EXPIRATION_TIME = 50;
	WorldState world;
	DatagramSocket socket;

	public Parser(WorldState world, DatagramSocket socket) {
		this.world = world;
		this.socket = socket;
	}

	public void run() {
		while (true) {
			byte[] receiveData = new byte[4096];
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
			parseServerParams(message);
		} else if (command.equals("player_param")) {

		} else if (command.equals("init")) {

		}
	}

	private void parseServerParams(String message) {
		System.out.println(message);
		String[] params = message.split("\\)\\(");
		for (String param : params) {
			param = param.replace(")", "");
			String[] s = param.split(" ");
			world.putServerParam(s[0], s[1]);
		}
	}

	private void parseAural(String message) {
		ParseContext<Integer> timeContext = parseTime(message);
		message = timeContext.message;
		int time = timeContext.parsedData;
		
		int space = message.indexOf(' ');
		String firstParam = message.substring(0, space);
		
		message = message.substring(space, message.length()-2);
		
		if(firstParam.equals("referee")) {
			parseReferee(message);
		}
		
	}

	private void parseReferee(String message) {
		if (message.equals("kick_off_l")) {
			if (world.isLeftSide()) {
				world.setState(WorldState.FRIENDLY_KICK_OFF);
			} else {
				world.setState(WorldState.ENEMY_KICK_OFF);
			}
		} else if (message.equals("kick_off_r")) {
			if (world.isRightSide()) {
				world.setState(WorldState.FRIENDLY_KICK_OFF);
			} else {
				world.setState(WorldState.ENEMY_KICK_OFF);
			}
		} else if (message.equals("play_on")) {
			world.setState(WorldState.PLAY_ON);
		}
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

}
