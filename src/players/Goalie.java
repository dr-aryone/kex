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
				if(world.getAngleToBall() != Constants.Params.NOT_DEFINED) {
					returnToGoal();
				} else if (world.getDistToBall() < 1.5) {
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
		if (world.getAngleToFriendlyGoal() != Constants.Params.NOT_DEFINED) {
			if (Math.abs(world.getAngleToFriendlyGoal()) > 10 && world.getDistToFriendlyGoal() > 5) {
				turn(world.getAngleToFriendlyGoal()); 
			} else if(world.getDistToFriendlyGoal() > 5){
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
