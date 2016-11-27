package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import me.ranol.servertransfer.AuthService;

public class FilePacket implements Packet {

	@Override
	public int id() {
		return 7;
	}

	@Override
	public boolean ping(DataInputStream in) throws IOException {
		if (AuthService.validUUID(in.readUTF())) {
			int len = in.readInt();
			String path = in.readUTF();
			byte[] bytes = new byte[len];
			in.readFully(bytes);
			Files.write(new File(path).toPath(), bytes);
			return true;
		}
		return false;
	}

	@Override
	public void pong(DataOutputStream out, boolean ret) throws IOException {
		out.writeBoolean(ret);
	}

}
