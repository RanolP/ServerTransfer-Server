package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.ranol.servertransfer.AuthService;
import me.ranol.servertransfer.LogManagement;

public class LogPacket implements Packet {

	@Override
	public int id() {
		return 1;
	}

	@Override
	public boolean ping(DataInputStream in) throws IOException {
		return AuthService.validUUID(in.readUTF());
	}

	@Override
	public void pong(DataOutputStream out, boolean ret) throws IOException {
		out.writeUTF(LogManagement.getLogs());
	}

}
