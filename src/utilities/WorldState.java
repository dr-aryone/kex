package utilities;

public class WorldState {
	public static final int KICK_OFF = 1;
	public static final int PLAY_ON = 2;
	private double angleToBall = Constants.Params.NOT_DEFINED;
	private double distToBall = Constants.Params.NOT_DEFINED;
	private int state = Constants.Params.NOT_DEFINED;
	private double angleToEnemyGoal = Constants.Params.NOT_DEFINED;
	private double distToEnemyGoal = Constants.Params.NOT_DEFINED;
	
	
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
	
	
}
