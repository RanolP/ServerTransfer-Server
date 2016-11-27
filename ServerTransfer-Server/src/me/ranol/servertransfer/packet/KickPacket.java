package me.ranol.servertransfer.packet;

public class KickPacket implements SendablePacket {

	@Override
	public int id() {
		return 5;
	}
}
