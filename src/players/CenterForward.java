package players;

import utilities.Constants;
import utilities.WorldState;

public class CenterForward extends Agent {

	public static void main(String[] args) {
		new CenterForward();
	}

	public CenterForward() {
		super(Constants.Team.CENTER_FORWARD);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch (world.getState()) {
			case WorldState.PLAY_ON:
				if (world.getAngleToBall() == Constants.Params.NOT_DEFINED) {
					turn(90);
				} else {
					tryToKick();
					runToBall();
				}
			default:
			}
		}
	}

	private void runToBall() {
		if (Math.abs(world.getAngleToBall()) > 10) {
			turn(world.getAngleToBall());
		} else {
			dash(100);
		}

	}

	private void tryToKick() {
		if (world.getDistToBall() < 1.9) {
			if (world.getAngleToEnemyGoal() != Constants.Params.NOT_DEFINED) {
				kick(100, world.getAngleToEnemyGoal());
			} else {
				turn(45);
			}
		}
	}
}
