package utilities;

public final class Constants {
	public static final class Server {
		public static final String IP = "127.0.0.1";
		public static final int PORT = 6000;
	}

	public static final class Team {
		public static final String NAME = "meinkemakers2";
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
	}

	public static final class CenterForward {
		public static final int START_X = -10;
		public static final int START_Y = 0;
		public static final int FRIENDLY_KICKOFF_X = -1;
		public static final int FRIENDLY_KICKOFF_Y = -1;
		public static final int ENEMY_KICKOFF_X = -10;
		public static final int ENEMY_KICKOFF_Y = 0;
	}

	public static final class Goalie {
		public static final int START_X = 1;
		public static final int START_Y = 0;
	}
}
