package players;

import utilities.Constants;
import utilities.WorldState;

public class Goalie extends Agent {

	public static void main(String[] args) {
		new Goalie();
	}

	public Goalie() {
		super(Constants.Team.GOALIE);
		isGoalie();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch (world.getState()) {
			case WorldState.BEFORE_KICK_OFF:
				moveFriendlyKickoff();
			default:
				if (world.getDistToBall() < 1.5) {
					catchTheBall();
				} else if (world.getDistToBall() < 10) {
					runToBall();
				} else {
					alignToBall();
				}
			}

		}
	}

	private void alignToBall() {
		if (!canSeeBall()) {
			returnToGoal();
			turn(45);
		}

	}

	private void returnToGoal() {
		
		turn(world.getAngleToFriendlyGoal());
		
	}

	private void catchTheBall() {
		catchBall(world.getAngleToBall());
	}
}
