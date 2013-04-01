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
					playLogic();
					break;
				case WorldState.ENEMY_KICK_OFF:
					break;
				case WorldState.FRIENDLY_KICK_OFF:
					kick(70, 95);
					break;
				case WorldState.BEFORE_KICK_OFF:
					moveFriendlyKickoff();
					break;
				case WorldState.FRIENDLY_FREE_KICK:
					playLogic();
					break;
				case WorldState.ENEMY_FREE_KICK:
					break;
				case WorldState.FRIENDLY_KICK_IN:
					kickIn();
					break;
				case WorldState.ENEMY_KICK_IN:
					break;
				case WorldState.FRIENDLY_CORNER_KICK:
					String passTarget = getPassTarget();
					if (world.getDistToBall() < Double.parseDouble(world
							.getServerParam("kickable_margin"))) {
						if (passTarget != null) {
							pass(passTarget);
						} else {
							kick(100, 0);
						}
					} else {
						runToBall();
					}
					break;
				case WorldState.ENEMY_CORNER_KICK:
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

	private void kickIn() {
		if (canSeeBall()) {
			if (world.getDistToBall() < Double.parseDouble(world
					.getServerParam("kickable_margin"))) {
				String passTarget = getPassTarget();
				if (passTarget != null) {
					pass(passTarget);
				} else {
					turn(180);
				}
			} else {
				runToBall();
			}
		} else {
			turn(Constants.Params.TURNING_LOOKING_ANGLE);
		}
	}

	private void playLogic() {
		if (canSeeBall()) {
			if (world.getDistToBall() < Double.parseDouble(world
					.getServerParam("kickable_margin"))) {
				String passTarget = getPassTarget();
				if (world.getDistToEnemyGoal() < Constants.Params.SCORING_DISTANCE) {
					tryToScore();
				} else if (passTarget != null) {
					pass(passTarget);
				} else {
					dribble();
				}
			} else {
				if (friendlyPlayerChasingBall()) {
					approachBall();
				} else {
					runToBall();
				}
			}
		} else {
			turn(Constants.Params.TURNING_LOOKING_ANGLE);
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
