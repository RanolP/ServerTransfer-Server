package me.ranol.servertransfer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.ranol.servertransfer.packet.KickPacket;
import me.ranol.servertransfer.packet.SWTRun;

public class Clients {
	private static Clients instance = new Clients();
	private List<ClientManagement> clients = new ArrayList<>();
	private org.eclipse.swt.widgets.List l;

	public static void addClient(ClientManagement client) {
		instance.clients.add(client);
		SWTRun.runAsync(() -> instance.l.add(client.client.getInetAddress().getHostAddress() + "/" + client.auth
				+ " [Salt=" + client.auth.salt + "]"));
	}

	public static void forceUpdate() {
		instance.l.removeAll();
		instance.l.setItems(Clients.clients().stream()
				.map(c -> c.client.getInetAddress().getHostAddress() + "/" + c.auth + " [Salt=" + c.auth.salt + "]")
				.collect(Collectors.toList()).toArray(new String[0]));
	}

	public static void removeClient(ClientManagement client) {
		instance.clients.remove(client);
		client.close();
		SWTRun.runAsync(Clients::forceUpdate);
	}

	public static List<ClientManagement> clients() {
		return instance.clients;
	}

	public static ClientManagement last() {
		return instance.clients.get(instance.clients.size() - 1);
	}

	public static void updator(org.eclipse.swt.widgets.List users) {
		instance.l = users;
	}
}
