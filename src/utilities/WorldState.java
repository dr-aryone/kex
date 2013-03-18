package utilities;

public class WorldState {
	public static final int KICK_OFF = 1;
	public static final int PLAY_ON = 2;
	private double angleToBall = Constants.Params.NOT_DEFINED;
	private double distToBall = Constants.Params.NOT_DEFINED;
	public double getDistToBall() {
		return distToBall;
	}

	public void setDistToBall(double distToBall) {
		this.distToBall = distToBall;
	}

	private int state;

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
	
	
}
