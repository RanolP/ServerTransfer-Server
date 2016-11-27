package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import me.ranol.servertransfer.AuthService;

public class FileListPacket implements Packet {
	String directory;

	@Override
	public int id() {
		return 8;
	}

	@Override
	public boolean ping(DataInputStream in) throws IOException {
		boolean valid = AuthService.validUUID(in.readUTF());
		directory = in.readUTF();
		return valid;
	}

	@Override
	public void pong(DataOutputStream out, boolean ret) throws IOException {
		if (!ret) {
			out.writeInt(0);
			return;
		}
		File dir = new File(directory);
		if (!dir.exists()) {
			out.writeInt(1);
			out.writeUTF("해당 경로가 존재하지 않습니다 :(");
			return;
		}
		File[] files = dir.listFiles();
		out.writeInt(files.length);
		for (File f : files)
			out.writeUTF((f.isFile() ? "I" : "O") + f.getName());
	}

}
