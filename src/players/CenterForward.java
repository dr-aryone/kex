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
			if (world.hasNewData()) {
				switch (world.getState()) {
				case WorldState.PLAY_ON:
					if (canSeeBall()) {
						tryToKick();
						runToBall();
					} else {
						turn(10);
					}
					break;
				case WorldState.FRIENDLY_KICK_OFF:
				case WorldState.BEFORE_KICK_OFF:
					moveFriendlyKickoff();
					break;
				default:
				}
				world.dataProcessed();
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
			kick(100, world.getAngleToEnemyGoal());
		}
	}

}
