package me.ranol.servertransfer;

import java.util.ArrayList;
import java.util.List;

public class Connectors {
	private static List<String> connectedUUID = new ArrayList<>();

	private Connectors() {
	}

	public static void remove(String uuid) {
		connectedUUID.remove(uuid);
	}

	public static void add(String uuid) {
		connectedUUID.add(uuid);
	}
}
