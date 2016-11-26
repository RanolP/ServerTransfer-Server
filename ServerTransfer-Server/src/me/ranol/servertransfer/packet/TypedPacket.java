package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface TypedPacket<T> {
	static final byte[] MAGIC = { 127, -128, 127, -128 };

	public int id();

	public boolean ping(DataInputStream in) throws IOException;

	public T pong(DataOutputStream out, boolean ret) throws IOException;

}
