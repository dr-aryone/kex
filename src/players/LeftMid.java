package players;

import utilities.Constants;
import utilities.WorldState;

public class LeftMid extends Agent {

	public static void main(String[] args) {
		new LeftMid();
	}

	private int notLookedAroundSince;

	public LeftMid() {
		super(Constants.Team.LEFT_MID);
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
		if (canSeeBall() && world.getDistToBall() < Constants.Params.TAKE_BALL_DISTANCE*1.5) {
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
			} else {
				runToBall();
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
		if(notLookedAroundSince > 20) {
			turn(180);
			notLookedAroundSince = 0;
			return;
		}
		notLookedAroundSince++;
		String target = world.isLeftSide() ? "f c b" : "f c t";
		String target2 = "f c";
		if (world.getAngleToObject(target) != Constants.Params.NOT_DEFINED) {
			if (Math.abs(world.getAngleToObject(target)) > 10) {
				turn(world.getAngleToObject(target));
			} else {
				if (world.getDistanceToObject(target) < 10) {
					if(world.getDistanceToObject(target2) < 25) {
						turn(90);
					} else {
						dash(Constants.Params.JOGGING_SPEED, world.getAngleToObject(target2));
					}
				} else {
					dash(Constants.Params.JOGGING_SPEED, world.getAngleToObject(target));
				}
			}
		} else {
			turn(45);
		}
	}

}
