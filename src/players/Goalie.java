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
				case WorldState.FRIENDLY_FREE_KICK:
					if(world.getDistToBall() < 2) {
						String target = getPassTarget();
						if(target != null) {
							passForward(target);
						} else {
							kick(100, 0);
						}
					}
				default:
					if (world.getDistToBall() < 1.5) {
						catchTheBall();
					} else if (world.getDistToBall() < 20) {
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

	public void dash(int power, double direction) {
		double dist1 = world.getDistanceToObject("f p "+world.getSideChar()+" t");
		double dist2 = world.getDistanceToObject("f p "+world.getSideChar()+" c");
		double dist3 = world.getDistanceToObject("f p "+world.getSideChar()+" b");
		double min = Math.min(dist1, dist2);
		min = Math.min(dist3, min);
		if(min < Constants.Goalie.MIN_DIST_TO_PENALTY_AREA_LINE) {
			returnToGoal();
		} else {
			super.dash(power, direction);
		}
	}
	
	private void alignToBall() {
		if(!canSeeFriendlyGoal()) {
			turn(45);
		} else if (!canSeeBall() || world.getDistToBall() > 50) {
			returnToGoal();
		} else {
			if (Math.abs(world.getAngleToBall()) > 20) {
				turn(world.getAngleToBall());
			} else if (world.getAngleToFriendlyGoal() > 0) { // gå mot höger stolpe
				if (world.isRightSide()) {
					dash(50, world.getAngleToObject("f g r t"));
				} else {
					dash(50, world.getAngleToObject("f g l b"));
				}
			} else { // gå mot vänster stolpe
				if (world.isRightSide()) {
					dash(50, world.getAngleToObject("f g r b"));
				} else {
					dash(50, world.getAngleToObject("f g l t"));
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
				super.dash(100, world.getAngleToFriendlyGoal());
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
