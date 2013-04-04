package players;

import utilities.Constants;
import utilities.WorldState;

public class InnerRightDefender extends Agent {

	public static void main(String[] args) {
		new InnerRightDefender();
	}

	public InnerRightDefender() {
		super(Constants.Team.INNER_RIGHT_DEFENDER);
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
		if (canSeeBall()
				&& world.getDistToBall() < Constants.Params.DEFENDER_TAKE_BALL_DISTANCE
				&& world.getDistanceToObject("f c t") > 17
				&& world.getDistanceToObject("f c") > 17
				&& world.getDistanceToObject("f c b") > 17) {
			if (world.getDistToBall() < Double.parseDouble(world
					.getServerParam("kickable_margin"))) {
				String passTarget = getPassTarget();
				if (world.getDistToEnemyGoal() < Constants.Params.SCORING_DISTANCE) {
					tryToScore();
				} else if (passTarget != null && isPassSafe(passTarget)) {
					pass(passTarget);
				} else {
					dribble();
				}
			} else if (world.getDistanceToObject("p") < Integer.parseInt(world
					.getServerParam("tackle_dist"))
					&& world.getDistanceToObject(getClosestFriend()) > Integer
							.parseInt(world.getServerParam("tackle_dist"))) {
				tackle(world.getAngleToObject("p"));
			} else {
				runToBall();
			}
		} else {
			runToSlot(world.isRightSide() ? "f p r c" : "f p l c", 10);
		}
	}

	private void tryToScore() {

		if (canSeeEnemyGoal()) {
			kick(100, world.getAngleToEnemyGoal());
		} else {
			turn(Constants.Params.TURNING_LOOKING_ANGLE);
		}
	}
}
