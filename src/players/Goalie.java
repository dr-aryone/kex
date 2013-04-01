package players;

import utilities.Constants;
import utilities.WorldState;

public class Goalie extends Agent {

	public static void main(String[] args) {
		new Goalie();
	}

	private int cyclesSinceCatch;

	public Goalie() {
		super(Constants.Team.GOALIE);
	}

	@Override
	public void run() {
		int turns = 0;
		while (true) {
			if (world.hasNewData()) {
				cyclesSinceCatch++;
				switch (world.getState()) {
				case WorldState.BEFORE_KICK_OFF:
					moveFriendlyKickoff();
					break;
				case WorldState.ENEMY_KICK_OFF:
					break;
				case WorldState.FRIENDLY_KICK_OFF:
					break;
				case WorldState.FRIENDLY_GOAL_KICK:
					if(world.getDistToBall() > Double.parseDouble(world.getServerParam("kickable_margin"))) {
						runToBall();
					} else if(turns >= 4) {
						turns = 0;
						passOrKickAway();
					} else {
						turn(Constants.Params.TURNING_LOOKING_ANGLE);
						turns++;
					}
					break;
				case WorldState.FRIENDLY_FREE_KICK:
					if(world.getDistToBall() < 30) {
						if(world.getDistToBall() > Double.parseDouble(world.getServerParam("kickable_margin"))) {
							runToBall();
						} else if(turns >= 4) {
							turns = 0;
							passOrKickAway();
						} else {
							turn(Constants.Params.TURNING_LOOKING_ANGLE);
							turns++;
						}
					} else {
						returnToGoal();
					}
					break;
				default:
					if (world.getDistToBall() < Double.parseDouble(world.getServerParam("catchable_area_l"))) {
						if(canCatch()) {
							cyclesSinceCatch = 0;
							catchTheBall();
						} else {
							passOrKickAway();
						}
					} else if (world.getDistToBall() < 15) {
						runToBall();
					} else {
						alignToBall();
					}
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

	public void dash(int power, double direction) {
		double distToCenter = world.getDistanceToObject("f c");
		if(distToCenter < Constants.Goalie.MIN_DIST_TO_CENTER) {
			returnToGoal();
		} else {
			super.dash(power, direction);
		}
	}
	
	private void alignToBall() {
		if(!canSeeFriendlyGoal()) {
			turn(Constants.Params.TURNING_LOOKING_ANGLE);
		} else if (!canSeeBall() || world.getDistToBall() > 50) {
			returnToGoal();
		} else {
			if (Math.abs(world.getAngleToBall()) > 20) {
				turn(world.getAngleToBall());
			} else if (world.getAngleToFriendlyGoal() > 0) { // gå mot höger stolpe
				if (world.isRightSide()) {
					dash(100, world.getAngleToObject("f g r t"));
				} else {
					dash(100, world.getAngleToObject("f g l b"));
				}
			} else { // gå mot vänster stolpe
				if (world.isRightSide()) {
					dash(100, world.getAngleToObject("f g r b"));
				} else {
					dash(100, world.getAngleToObject("f g l t"));
				}
			}
		}
	}

	private void returnToGoal() {
		if (world.getAngleToFriendlyGoal() != Constants.Params.NOT_DEFINED) {
			if (Math.abs(world.getAngleToFriendlyGoal()) > 10 && world.getDistToFriendlyGoal() > 5) {
				turn(world.getAngleToFriendlyGoal());
			} else if (world.getDistToFriendlyGoal() > 5) {
				super.dash(100, world.getAngleToFriendlyGoal());
			} else {
				turn(Constants.Params.TURNING_LOOKING_ANGLE);
			}
		} else {
			turn(Constants.Params.TURNING_LOOKING_ANGLE);
		}
	}
	
	private void passOrKickAway() {
		if(world.getDistToBall() < Double.parseDouble(world.getServerParam("kickable_margin"))) {
			String target = getPassTarget();
			if(target != null) {
				pass(target);
			} else {
				if(canSeeEnemyGoal()) {
					kick(100, world.getAngleToEnemyGoal());
				} else if(isLookingBack()) {
					kick(100, 180);
				} else if(isLookingLeft()){
					kick(100, 90);
				} else if(isLookingRight()){
					kick(100, -90);
				} else {
					kick(100, 0);
				}
			}
		} else {
			runToBall();
		}
	}
	
	private boolean canCatch() {
		return cyclesSinceCatch >= 5 ? true : false;
	}

	private void catchTheBall() {
		catchBall(world.getAngleToBall());
	}
}
