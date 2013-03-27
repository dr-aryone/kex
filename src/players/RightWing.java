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
					if (canSeeBall() && world.getDistToBall() < 20) {
						if (!tryToKick()) {
							runToBall();
						}
					} else {
						runToSlot();
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

	private void runToSlot() {
		String target = world.isRightSide() ? "f p l t" : "f p r b";
		if (world.getAngleToObject(target) != Constants.Params.NOT_DEFINED) {
			if (Math.abs(world.getAngleToObject(target)) > 10) {
				turn(world.getAngleToObject(target));
			} else {
				if (world.getDistanceToObject(target) < 5) {
					turn(45);
				} else {
					dash(50, world.getAngleToObject(target));
				}
			}
		} else {
			turn(45);
		}
	}

	private boolean tryToKick() {
		if (world.getDistToBall() < Double.parseDouble(world
				.getServerParam("kickable_margin"))) {
			if (canSeeEnemyGoal()) {
				kick(100, world.getAngleToEnemyGoal());
			} else {
				turn(45);
			}
			return true;
		} else {
			return false;
		}
	}
}
