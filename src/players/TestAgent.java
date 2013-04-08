package players;

import utilities.Constants;
import utilities.WorldState;

public class TestAgent extends Agent {

	public TestAgent() {
		super(Constants.Team.CENTER_FORWARD);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		new TestAgent();
	}

	boolean runningLeft;

	@Override
	public void run() {
		runningLeft = true;
		while(true) {
			if (world.hasNewData()) {
				switch(world.getState()) {
				case WorldState.PLAY_ON:
					playLogic();
					break;
				default:
					moveFriendlyKickoff();
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

	private void playLogic() {
		if(runningLeft) {
			if(world.getDistanceToObject("f p l b") < 1) {
				runningLeft=false;
			} else {
				runToSlot("f p l b", 0);
			}
		} else {
			if(world.getDistanceToObject("f p r b") < 1) {
				runningLeft=true;
			} else {
				runToSlot("f p r b", 0);
			}
		}
	}
	
}

