package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.ranol.servertransfer.AuthService;
import me.ranol.servertransfer.LogManagement;

public class ServerStartPacket implements Packet {

	@Override
	public int id() {
		return 3;
	}

	@Override
	public boolean ping(DataInputStream in) throws IOException {
		String changer = in.readUTF();
		if(AuthService.validUUID(changer)){
			LogManagement.info("UUID "+changer+"["+AuthService.getNickname(changer)+"가 서버를 시작했습니다.");
		}
		return false;
	}

	@Override
	public void pong(DataOutputStream out, boolean ret) throws IOException {
		out.writeBoolean(ret);
	}

}
