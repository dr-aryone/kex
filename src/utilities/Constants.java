package utilities;

import java.net.InetAddress;

public final class Constants {
	public static final class Server {
		public static final String INIT_ADDRESS = "127.0.0.1";
		public static InetAddress IP;
		public static int PORT = 6000;
	}

	public static final class Team {
		public static final String NAME = "meinkemakers";
		public static final int GOALIE = 1;
		public static final int OUTER_RIGHT_DEFENDER = 2;
		public static final int INNER_RIGHT_DEFENDER = 3;
		public static final int OUTER_LEFT_DEFENDER = 4;
		public static final int INNER_LEFT_DEFENDER = 5;
		public static final int RIGHT_MID = 6;
		public static final int MID_MID = 7;
		public static final int LEFT_MID = 8;
		public static final int RIGHT_WING = 9;
		public static final int LEFT_WING = 10;
		public static final int CENTER_FORWARD = 11;
	}

	public static final class Params {
		public static final int NOT_DEFINED = Integer.MAX_VALUE;
		public static final double SCORING_DISTANCE = 25;
		public static final int DRIBBLING_KICK_POWER = 30;
		public static final int FORWARD_PASSING_ANGLE = 30;
		public static final double FORWARD_PASSING_DISTANCE = 5;
		public static final double CLOSE_TO_EDGE = 10;
		public static final int CLOSE_ANGLE = 10;
		public static final double TAKE_BALL_DISTANCE = 12;
		public static final int FRIENDLY_GOAL_INVALIDATE_TIME = 25;
		public static final int DEFAULT_INVALIDATE_TIME = 5;
		public static final int JOGGING_SPEED = 75;
	}

	public static final class CenterForward {
		public static final double FRIENDLY_KICKOFF_X = 0;
		public static final double FRIENDLY_KICKOFF_Y = -0.5;
		public static final double ENEMY_KICKOFF_X = -10;
		public static final double ENEMY_KICKOFF_Y = 0;
	}


	public static final class RightWing {
		public static final double FRIENDLY_KICKOFF_X = -1;
		public static final double FRIENDLY_KICKOFF_Y = 10;
		public static final double ENEMY_KICKOFF_X = -10;
		public static final double ENEMY_KICKOFF_Y = 10;
	}


	public static final class LeftWing {
		public static final double FRIENDLY_KICKOFF_X = -1;
		public static final double FRIENDLY_KICKOFF_Y = -10;
		public static final double ENEMY_KICKOFF_X = -10;
		public static final double ENEMY_KICKOFF_Y = -10;
	}

	public static final class Goalie {
		public static final int START_X = -50;
		public static final int START_Y = 0;
		public static final int MIN_DIST_TO_PENALTY_AREA_LINE = 3;
		public static final int MIN_DIST_TO_CENTER = 37;
	}

	public static final class InnerRightDefender {
		public static final int START_X = -35;
		public static final int START_Y = 10;
	}

	public static final class InnerLeftDefender {
		public static final int START_X = -35;
		public static final int START_Y = -10;
	}
	
	public static final class OuterLeftDefender {
		public static final int START_X = -30;
		public static final int START_Y = -30;
	}
	
	public static final class OuterRightDefender {
		public static final int START_X = -30;
		public static final int START_Y = 30;
	}

	public static final class RightMid {
		public static final int START_X = -15;
		public static final int START_Y = 0;
		
	}
	public static final class LeftMid {
		public static final int START_X = -15;
		public static final int START_Y = -20;
		
	}
	public static final class MidMid {
		public static final int START_X = -15;
		public static final int START_Y = 20;
		
	}
}
