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
		} else {
			if(Math.abs(world.getAngleToBall()) > 10) {
				turn(world.getAngleToBall());
			} else if(world.getAngleToFriendlyGoal() > 0){ // gå mot höger stolpe
				if(world.isRightSide()) {
					dash(10, world.getAngleToObject("flag g r t"));
				} else {
					dash(10, world.getAngleToObject("flag g l b"));
				}
			} else { // gå mot vänster stolpe
				if(world.isRightSide()) {
					dash(10, world.getAngleToObject("flag g r b"));
				} else {
					dash(10, world.getAngleToObject("flag g l t"));
				}
			}
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
