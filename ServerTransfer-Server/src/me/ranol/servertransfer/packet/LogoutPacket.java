package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.ranol.servertransfer.AuthService;

public class LogoutPacket implements Packet {
	String id;
	String pwd;

	@Override
	public int id() {
		return 2;
	}

	@Override
	public boolean ping(DataInputStream in) throws IOException {
		String uuid = in.readUTF();
		AuthService.logout(uuid);
		return true;
	}

	@Override
	public void pong(DataOutputStream out, boolean ret) throws IOException {
		out.writeBoolean(ret);
	}

}
