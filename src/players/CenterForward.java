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
					kick(70, 95);
					break;
				case WorldState.PLAY_ON:
					playLogic();
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

	private void playLogic() {
		if (canSeeBall()) {
			if (world.getDistToBall() < Double.parseDouble(world
					.getServerParam("kickable_margin"))) {
				String passTarget = getPassTarget();
				if (world.getDistToEnemyGoal() < Constants.Params.SCORING_DISTANCE) {
					tryToScore();
				} else if (passTarget != null){
					passForward(passTarget);
				} else {
					dribble();
				}
			} else {
				runToBall();
			}
		} else {
			turn(45);
		}
	}

	private void tryToScore() {

		if (canSeeEnemyGoal()) {
			kick(100, world.getAngleToEnemyGoal());
		} else {
			turn(45);
		}
	}
}
