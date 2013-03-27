package utilities;

import java.util.HashMap;

public class WorldState {
	public static final int PLAY_ON = 1;
	public static final int ENEMY_KICK_OFF = 2;
	public static final int FRIENDLY_KICK_OFF = 3;
	public static final int BEFORE_KICK_OFF = 4;
	private boolean isLeftSide;
	private HashMap<String, Integer> lastSeen = new HashMap<String, Integer>();
	private HashMap<String, Integer> angleToObjects = new HashMap<String, Integer>();
	private HashMap<String, Double> distanceToObjects = new HashMap<String, Double>();
	private HashMap<String, String> serverParams = new HashMap<String, String>();
	private boolean newData;
	private int state;
	private int currentTime;


	public int getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public int getAngleToObject(String key) {
		Integer angle = angleToObjects.get(key);
		if(angle == null) {
			return Constants.Params.NOT_DEFINED;
		}
		
		Integer time = lastSeen.get(key);
		if(time == null) {
			return Constants.Params.NOT_DEFINED;
		}

		if(getCurrentTime() - time > 5) {
			return Constants.Params.NOT_DEFINED;
		}
		
		return angle;
	}
	
	public double getDistanceToObject(String key) {
		Double dist = distanceToObjects.get(key);
		if(dist == null) {
			return Constants.Params.NOT_DEFINED;
		}
		
		Integer time = lastSeen.get(key);
		if(time == null) {
			return Constants.Params.NOT_DEFINED;
		}
		
		if(getCurrentTime() - time > 5) {
			return Constants.Params.NOT_DEFINED;
		}
		return dist;
	}

	public String getServerParam(String key) {
		String serverParam = serverParams.get(key);
		if(serverParam == null) {
			return "";
		}
		return serverParam;
	}

	public void putServerParam(String key, String obj) {
		serverParams.put(key, obj);
	}

	public HashMap<String, Integer> getAngleToObjects() {
		return angleToObjects;
	}

	public void sawObjectAtTime(String object, int time) {
		lastSeen.put(object, time);
	}

	public void angleToObject(String object, int angle) {
		angleToObjects.put(object, angle);
	}

	public void distanceToObject(String object, double distance) {
		distanceToObjects.put(object, distance);
	}

	public double getDistToFriendlyGoal() {
		if (isLeftSide()) {
			return getDistanceToObject("g l");
		} else {
			return getDistanceToObject("g r");
		}
	}

	public int getAngleToFriendlyGoal() {
		if (isLeftSide()) {
			return getAngleToObject("g l");
		} else {
			return getAngleToObject("g r");
		}	}

	public boolean isLeftSide() {
		return isLeftSide;
	}

	public void setLeftSide() {
		isLeftSide = true;
	}

	public void setRightSide() {
		isLeftSide = false;
	}

	public double getDistToEnemyGoal() {
		if (isLeftSide()) {
			return getDistanceToObject("g r");
		} else {
			return getDistanceToObject("g l");
		}
	}

	public double getDistToBall() {
		return getDistanceToObject("b");
	}


	public int getAngleToBall() {
		return getAngleToObject("b");
	}


	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public double getAngleToEnemyGoal() {
		if (isLeftSide()) {
			return getAngleToObject("g r");
		} else {
			return getAngleToObject("g l");
		}
	}



	public boolean isRightSide() {
		return !isLeftSide;
	}

	public synchronized void newData() {
		newData = true;
	}

	public synchronized boolean hasNewData() {
		return newData;
	}

	public synchronized void dataProcessed() {
		newData = false;
	}

}
