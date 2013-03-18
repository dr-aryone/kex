package utilities;

public class WorldState {
	public static final int KICK_OFF = 1;
	public static final int PLAY_ON = 2;
	private double angleToBall = Constants.Params.NOT_DEFINED;
	private double distToBall = Constants.Params.NOT_DEFINED;
	private int state = Constants.Params.NOT_DEFINED;
	private double angleToEnemyGoal = Constants.Params.NOT_DEFINED;
	private double distToEnemyGoal = Constants.Params.NOT_DEFINED;
	private boolean isLeftSide;
	private double angleToFriendlyGoal;
	private double distToFriendlyGoal;


	public boolean canSeeEnemyGoal() {
		return angleToEnemyGoal == Constants.Params.NOT_DEFINED;
	}


	public double getDistToFriendlyGoal() {
		return distToFriendlyGoal;
	}

	public double getAngleToFriendlyGoal() {
		return angleToFriendlyGoal;
	}

	public boolean isLeftSide() {
		return isLeftSide;
	}

	public void setLeftSide(boolean isLeftSide) {
		this.isLeftSide = isLeftSide;
	}
	
	
	public double getDistToEnemyGoal() {
		return distToEnemyGoal;
	}

	public double getDistToBall() {
		return distToBall;
	}

	public void setDistToBall(double distToBall) {
		this.distToBall = distToBall;
	}


	public double getAngleToBall() {
		return angleToBall;
	}

	public void setAngleToBall(double angleToBall) {
		this.angleToBall = angleToBall;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}

	public double getAngleToEnemyGoal() {
		return angleToEnemyGoal;
	}

	public void setAngleToEnemyGoal(double angle) {
		angleToEnemyGoal = angle;
		
	}

	public void setDistToEnemyGoal(double dist) {
		distToEnemyGoal = dist;
		
	}

	public void setDistToFriendlyGoal(double dist) {
		distToFriendlyGoal = dist;
	}

	public void setAngleToFriendlyGoal(double angle) {
		angleToFriendlyGoal = angle;
		
	}


	public boolean isRightSide() {
		return !isLeftSide;
	}
	
	
}
