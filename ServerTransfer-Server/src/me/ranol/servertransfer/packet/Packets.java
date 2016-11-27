package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public enum Packets {
	LOGIN(new LoginPacket()),

	LOG(new LogPacket()),

	LOGOUT(new LogoutPacket()),

	CONNECT(new ConnectPacket()),

	FILE(new FilePacket()),

	FILELIST(new FileListPacket());
	Packet packet;

	Packets(Packet packet) {
		this.packet = packet;
	}

	public boolean ping(DataInputStream in) throws IOException {
		return packet.ping(in);
	}

	public void pong(DataOutputStream out, boolean ret) throws IOException {
		packet.pong(out, ret);
	}

	public static Packet valueOf(int type) {
		Packet result = null;
		for (Packets p : values()) {
			if (p.packet.id() == type) {
				result = p.packet;
				break;
			}
		}
		return result;
	}
}
