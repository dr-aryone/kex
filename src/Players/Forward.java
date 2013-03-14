package Players;

import java.net.Socket;

public class Forward {
	private Socket socket;
	
	public static void main(String [] args) {
		new Forward();
	}
	
	public Forward() {
		socket = new Socket();
	}
}
