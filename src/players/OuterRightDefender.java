package players;

import utilities.Constants;
import utilities.WorldState;

public class OuterRightDefender extends Agent {

	public static void main(String[] args) {
		new OuterRightDefender();
	}

	public OuterRightDefender() {
		super(Constants.Team.OUTER_RIGHT_DEFENDER);
	}

	@Override
	public void run() {
		while (true) {
			if (world.hasNewData()) {
				switch (world.getState()) {
				case WorldState.PLAY_ON:
					playLogic();
					break;
				case WorldState.ENEMY_KICK_OFF:
					break;
				case WorldState.FRIENDLY_KICK_OFF:
					break;
				case WorldState.BEFORE_KICK_OFF:
					moveFriendlyKickoff();
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

	private void playLogic() {
		if (canSeeBall() && world.getDistToBall() < Constants.Params.TAKE_BALL_DISTANCE) {
			if (world.getDistToBall() < Double.parseDouble(world
					.getServerParam("kickable_margin"))) {
				String passTarget = getPassTarget();
				if (world.getDistToEnemyGoal() < Constants.Params.SCORING_DISTANCE) {
					tryToScore();
				} else if (passTarget != null) {
					passForward(passTarget);
				} else {
					dribble();
				}
			}
		} else {
			runToSlot();
		}
	}

	private void tryToScore() {

		if (canSeeEnemyGoal()) {
			kick(100, world.getAngleToEnemyGoal());
		} else {
			turn(45);
		}
	}

	private void runToSlot() {
		String target = world.isRightSide() ? "f t r 20" : "f b l 20";
		if (world.getAngleToObject(target) != Constants.Params.NOT_DEFINED) {
			if (world.getDistanceToObject(target) < 20) {
				turn(90);
			}else if (Math.abs(world.getAngleToObject(target)) > 10) {
				turn(world.getAngleToObject(target));
			} else {
				dash(Constants.Params.JOGGING_SPEED, world.getAngleToObject(target));
			}
		} else {
			turn(45);
		}
	}
}