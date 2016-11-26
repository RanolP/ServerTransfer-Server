package me.ranol.servertransfer;

public class LogManagement {
	private static LogManagement INSTANCE = new LogManagement();
	private StringBuilder log = new StringBuilder();

	public static void info(String log) {
		INSTANCE.log.append("[INFO] " + log + "\n");
	}

	public static String getLogs() {
		return INSTANCE.log.toString();
	}
}
