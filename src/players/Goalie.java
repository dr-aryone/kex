package players;

import utilities.Constants;

public class Goalie extends Agent {

	public static void main(String[] args) {
		new Goalie();
	}

	private boolean shouldMove;
	private int angleToMoveBack;

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

			if (world.getDistToBall() < 5) {
				runToBall();
			} else if (world.getDistToBall() < 1.5) {
				catchTheBall();
			} else {
				alignToBall();
			}

		}
	}

	private void alignToBall() {
		if (angleToMoveBack != 0) {
			turn(angleToMoveBack);
			angleToMoveBack = 0;
		}
		if (shouldMove) {
			dash(10);
			shouldMove = false;
		}
		if (!canSeeBall()) {
			turn(90);
		} else if (world.getAngleToBall() > 0) {
			turn(90);
			shouldMove = true;
			angleToMoveBack = -90;
		} else if (world.getAngleToBall() < 0) {
			turn(-90);
			shouldMove = true;
			angleToMoveBack = 90;
		}
	}

	private void catchTheBall() {
		catchBall(world.getAngleToBall());
	}

	private void runToBall() {
		if (Math.abs(world.getAngleToBall()) > 10) {
			turn(world.getAngleToBall());
		} else {
			dash(100);
		}

	}

}
