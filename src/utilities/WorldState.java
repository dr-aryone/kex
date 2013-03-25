package utilities;

import java.util.HashMap;

public class WorldState {
	public static final int PLAY_ON = 1;
	public static final int ENEMY_KICK_OFF = 2;
	public static final int FRIENDLY_KICK_OFF = 3;
	public static final int BEFORE_KICK_OFF = 4;
	private double angleToBall = Constants.Params.NOT_DEFINED;
	private double distToBall = Constants.Params.NOT_DEFINED;
	private int state = Constants.Params.NOT_DEFINED;
	private double angleToEnemyGoal = Constants.Params.NOT_DEFINED;
	private double distToEnemyGoal = Constants.Params.NOT_DEFINED;
	private boolean isLeftSide;
	private double angleToFriendlyGoal;
	private double distToFriendlyGoal;
	private HashMap<String, Integer> lastSeen = new HashMap<String, Integer>();
	private HashMap<String, Double> angleToObjects = new HashMap<String, Double>();
	private HashMap<String, Integer> distanceToObjects = new HashMap<String, Integer>();


	public void sawObjectAtTime(String object, int time) {
		lastSeen.put(object, time);
	}
	
	public void angleToObject(String object, double angle) {
		angleToObjects.put(object, angle);
	}
	
	public void distanceToObject(String object, int distance) {
		distanceToObjects.put(object, distance);
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
