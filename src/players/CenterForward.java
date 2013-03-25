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
			if (newData) {
				newData = false;
				switch (world.getState()) {
				case WorldState.PLAY_ON:
					if (world.getAngleToBall() == Constants.Params.NOT_DEFINED) {
						turn(90);
					} else {
						tryToKick();
						runToBall();
					}
				case WorldState.BEFORE_KICK_OFF:
					moveFriendlyKickoff();
				default:
				}
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void tryToKick() {
		if (world.getDistToBall() < 1) {
			if (world.getAngleToEnemyGoal() != Constants.Params.NOT_DEFINED) {
				kick(100, world.getAngleToEnemyGoal());
			} else {
				turn(45);
			}
		}
	}

}
