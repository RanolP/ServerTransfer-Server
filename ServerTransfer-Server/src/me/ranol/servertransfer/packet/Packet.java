package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Packet extends SendablePacket {

	static final byte[] MAGIC = { 127, -128, 127, -128 };

	public boolean ping(DataInputStream in) throws IOException;

	public void pong(DataOutputStream out, boolean ret) throws IOException;

}
