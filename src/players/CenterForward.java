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
				case WorldState.FRIENDLY_KICK_OFF:
					kick(20, 180);
					break;
				case WorldState.PLAY_ON:
					if (canSeeBall()) {
						if(!tryToKick()) {
							runToBall();
						}
					} else {
						turn(45);
					}
					break;
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

	private boolean tryToKick() {
		if (world.getDistToBall() < Double.parseDouble(world.getServerParam("kickable_margin"))) {
			if(canSeeEnemyGoal()) {
				System.out.println("KICKKUUUUUUUUUUU");
				kick(100, world.getAngleToEnemyGoal());
			} else {
				System.out.println("TURNUUUUUUUUUUUU");
				turn(45);
			}
			return true;
		} else {
			return false;
		}
	}
}
