package players;

import java.io.IOException;

import utilities.Constants;
import utilities.WorldState;

public class Forward extends Agent {

	public static void main(String[] args) {
		try {
			new Forward();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Forward() throws IOException {
		super(10, 0);
	}

	@Override
	public void run() throws IOException {
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch (world.getState()) {
			case WorldState.PLAY_ON:
				if(world.getAngleToBall() == Constants.Params.NOT_DEFINED) {
					turn(90);
				} else {
					//run to ball
					if(Math.abs(world.getAngleToBall()) > 10) {
						turn(world.getAngleToBall());
					} else {
						dash(100);
					}
				}
			default:
			}
		}
	}

}
