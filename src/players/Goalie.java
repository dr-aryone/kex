package players;

import utilities.Constants;
import utilities.WorldState;

public class Goalie extends Agent {

	public static void main(String[] args) {
		new Goalie();
	}

	public Goalie() {
		super(Constants.Team.GOALIE);
	}

	@Override
	public void run() {
		while (true) {
			if (world.hasNewData()) {
				switch (world.getState()) {
				case WorldState.BEFORE_KICK_OFF:
					moveFriendlyKickoff();
					break;
				default:
					if (world.getDistToBall() < 1.5) {
						catchTheBall();
					} else if (world.getDistToBall() < 10) {
						runToBall();
					} else {
						alignToBall();
					}
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

	private void alignToBall() {
		if(!canSeeFriendlyGoal()) {
			System.out.println("WHEREZ DA GOAL????");
			turn(45);
		} else if (!canSeeBall() || world.getDistToBall() > 50) {
			returnToGoal();
		} else {
			if (Math.abs(world.getAngleToBall()) > 20) {
				turn(world.getAngleToBall());
			} else if (world.getAngleToFriendlyGoal() > 0) { // gå mot höger stolpe
				if (world.isRightSide()) {
					dash(10, world.getAngleToObject("f g r t"));
				} else {
					dash(10, world.getAngleToObject("f g l b"));
				}
			} else { // gå mot vänster stolpe
				if (world.isRightSide()) {
					dash(10, world.getAngleToObject("f g r b"));
				} else {
					dash(10, world.getAngleToObject("f g l t"));
				}
			}
		}
	}

	private void returnToGoal() {
		if (world.getAngleToFriendlyGoal() != Constants.Params.NOT_DEFINED) {
			if (Math.abs(world.getAngleToFriendlyGoal()) > 10
					&& world.getDistToFriendlyGoal() > 5) {
				turn(world.getAngleToFriendlyGoal());
			} else if (world.getDistToFriendlyGoal() > 5) {
				dash(100, world.getAngleToFriendlyGoal());
			} else {
				turn(45);
			}
		} else {
			turn(45);
		}
	}

	private void catchTheBall() {
		catchBall(world.getAngleToBall());
	}
}
