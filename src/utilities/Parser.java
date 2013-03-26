package utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Parser extends Thread {

	WorldState world;
	DatagramSocket socket;
	private boolean hasBeenInit = false;

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
			if(!hasBeenInit) {
				Constants.Server.PORT = p.getPort();
				Constants.Server.IP = p.getAddress();
				hasBeenInit = true;
			}
			String message = new String(p.getData());
			System.out.println(message);
			parse(message);
			world.newData();
		}
	}

	private void parse(String message) {
		int firstSpace = message.indexOf(' ');
		String command = message.substring(1, firstSpace);
		message = message.substring(firstSpace, message.length() - 1).trim();
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
			parseInit(message);
		}
	}

	private void parseInit(String message) {
		message = message.substring(0, message.length() - 1);
		String[] params = message.split(" ");
		if (params[0].equals("l")) {
			world.setLeftSide();
		} else {
			world.setRightSide();
		}
		parseState(params[2]);
	}

	private void parseServerParams(String message) {
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

		int space = message.indexOf(' ');
		String firstParam = message.substring(0, space);

		message = message.substring(space, message.length());

		if (firstParam.equals("referee")) {
			System.out.println("referee says "+message);
			parseState(message);
		}

	}

	private void parseState(String message) {
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
		} else if (message.contains("before_kick_off")) {
			world.setState(WorldState.BEFORE_KICK_OFF);
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
			String item = itemContext.parsedData.toLowerCase();

			world.sawObjectAtTime(item, time);
			int rightPara = message.indexOf(')');
			String[] params = message.substring(0, rightPara).split(" ");
			try {
				message = message.substring(rightPara + 2);
			} catch (java.lang.StringIndexOutOfBoundsException e) {
				hasNext = false;
			}
			
			/**
			if (params.length > 0) {
				world.distanceToObject(item, Double.parseDouble(params[0]));
			}
			if (params.length > 1) {
				world.angleToObject(item, Integer.parseInt(params[1]));
			}**/
			if(item.equals("b")) {
				world.setDistanceToBall(Double.parseDouble(params[0]));
				world.setAngleToBall(Integer.parseInt(params[1]));
			}

		}

	}

	private ParseContext<String> parseItem(String message) {
		int rightPara = message.indexOf(')');
		String item = message.substring(2, rightPara);
		message = message.substring(rightPara+1, message.length()).trim();
		return new ParseContext<String>(message, item);
	}

	private ParseContext<Integer> parseTime(String message) {
		int firstSpace = message.indexOf(' ', 1);
		int firstPara = message.indexOf(')');
		if(firstPara < firstSpace || firstSpace == -1) {
			return new ParseContext<Integer>("", 0);
		}
		int time = Integer.parseInt(message.substring(0, firstSpace));
		world.setCurrentTime(time);
		message = message.substring(firstSpace, message.length() - 1).trim();
		return new ParseContext<Integer>(message, time);
	}

}
