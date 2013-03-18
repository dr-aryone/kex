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
		super(-10, 0);
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
					tryToKick();
					runToBall();
				}
			default:
			}
		}
	}

	private void runToBall() throws IOException {
		if(Math.abs(world.getAngleToBall()) > 10) {
			turn(world.getAngleToBall());
		} else {
			dash(100);
		}
		
	}

	private void tryToKick() throws IOException {
		if(world.getDistToBall() < 1.9) {
			if(world.getAngleToEnemyGoal() != Constants.Params.NOT_DEFINED) {
				kick(100, world.getAngleToEnemyGoal());
			} else {
				turn(45);
			}
		}
	}
}
