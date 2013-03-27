package players;

import utilities.Constants;
import utilities.WorldState;

public class RightWing extends Agent {

	public static void main(String[] args) {
		new RightWing();
	}

	public RightWing() {
		super(Constants.Team.RIGHT_WING);
	}

	@Override
	public void run() {
		while (true) {
			if (world.hasNewData()) {
				switch (world.getState()) {
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
			System.out.println("Kicking, angle to goal is "+world.getAngleToEnemyGoal());
			System.out.println("LOOOOOOOL");
			kick(100, world.getAngleToEnemyGoal());
			return true;
		} else {
			return false;
		}
	}
}
