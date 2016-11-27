package me.ranol.servertransfer.packet;

import java.io.DataOutputStream;
import java.io.IOException;

public interface SendablePacket {
	static final byte[] SENDMAGIC = { -100, 100, -100, 100 };

	public int id();

	public default void pong(DataOutputStream out, boolean ret) throws IOException {
		out.write(SENDMAGIC);
		out.writeInt(id());
	}
}
