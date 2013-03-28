package players;

import utilities.Constants;
import utilities.WorldState;

public class LeftWing extends Agent {

	public static void main(String[] args) {
		new LeftWing();
	}

	public LeftWing() {
		super(Constants.Team.LEFT_WING);
	}

	@Override
	public void run() {
		while (true) {
			if (world.hasNewData()) {
				switch (world.getState()) {
				case WorldState.PLAY_ON:
					break;
				case WorldState.ENEMY_KICK_OFF:
					break;
				case WorldState.FRIENDLY_KICK_OFF:
					break;
				case WorldState.BEFORE_KICK_OFF:
					break;
				case WorldState.FRIENDLY_FREE_KICK:
					break;
				case WorldState.ENEMY_FREE_KICK:
					break;
				case WorldState.FRIENDLY_KICK_IN:
					break;
				case WorldState.ENEMY_KICK_IN:
					break;
				case WorldState.FRIENDLY_CORNER_KICK:
					break;
				case WorldState.ENEMY_CORNER_KICK:
					break;
				default:
					break;
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
		String target = world.isRightSide() ? "f p r t" : "f p l b";
		if (world.getAngleToObject(target) != Constants.Params.NOT_DEFINED) {
			if (Math.abs(world.getAngleToObject(target)) > 10) {
				turn(world.getAngleToObject(target));
			} else {
				if (world.getDistanceToObject(target) < 5) {
					turn(45);
				} else {
					dash(60, world.getAngleToObject(target));
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
