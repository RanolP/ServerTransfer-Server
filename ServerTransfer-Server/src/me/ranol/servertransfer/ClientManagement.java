package me.ranol.servertransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

import me.ranol.servertransfer.packet.ConnectPacket;
import me.ranol.servertransfer.packet.KickPacket;
import me.ranol.servertransfer.packet.LoginPacket;
import me.ranol.servertransfer.packet.LogoutPacket;
import me.ranol.servertransfer.packet.Packet;
import me.ranol.servertransfer.packet.Packets;
import me.ranol.servertransfer.packet.SendablePacket;

public class ClientManagement implements Runnable {
	Socket client;
	Socket recieve;
	DataOutputStream rout;
	Thread thread;
	DataInputStream in;
	DataOutputStream out;
	Auth auth = new Auth();
	boolean run = true;

	public ClientManagement(Socket soc) {
		this.client = soc;
		thread = new Thread(this);
		try {
			in = new DataInputStream(client.getInputStream());
			out = new DataOutputStream(client.getOutputStream());
		} catch (Exception e) {
		}
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	public Thread asThread() {
		return thread;
	}

	public void start() {
		thread.start();
	}

	@Override
	public void run() {
		while (run) {
			try {
				byte[] data = new byte[4];
				in.readFully(data);
				if (!Arrays.equals(data, Packet.MAGIC)) {
					System.out.println("패킷 Magic이 완전하지 않습니다.");
					System.out.println("받은 Magic: " + Arrays.toString(data));
					continue;
				}
				System.out.println("패킷 Magic이 정상적입니다. 분석을 시도합니다.");
				int type = in.readInt();
				System.out.println("Id: " + type);
				Packet p = Packets.valueOf(type);
				if (p == null)
					continue;
				if (p instanceof ConnectPacket) {
					if (!in.readUTF().equals(Clients.last().auth.salt)) {
						System.out.println("Salt 값이 맞지 않습니다.");
						client.close();
						Clients.removeClient(Clients.last());
						break;
					}
				} else if (!(p instanceof LoginPacket)) {
					if (!in.readUTF().equals(auth.salt)) {
						System.out.println("Salt 값이 맞지 않습니다.");
						client.close();
						Clients.removeClient(this);
						break;
					}
				}
				if (p.ping(in)) {
					p.pong(out, true);
					if (p instanceof ConnectPacket) {
						Clients.last().setRecieve(client);
						System.out.println("Recieve 설정됨!");
						break;
					}
					if (p instanceof LoginPacket) {
						LoginPacket l = (LoginPacket) p;
						setAuth(l.getAuth());
						System.out.println("Auth 설정됨!");
						Clients.addClient(this);
					}
				} else {
					p.pong(out, false);
					if (p instanceof LoginPacket) {
						break;
					}
				}
				byte[] dummy = new byte[in.available()];
				in.readFully(dummy);
				if (p instanceof LogoutPacket) {
					Clients.removeClient(this);
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	private void setRecieve(Socket soc) {
		this.recieve = soc;
		try {
			rout = new DataOutputStream(recieve.getOutputStream());
		} catch (Exception e) {
		}
	}

	@Override
	public String toString() {
		return client.getInetAddress().getHostAddress() + " / " + auth.toString();
	}

	public void sendPacket(SendablePacket packet) {
		try {
			packet.pong(rout, true);
		} catch (Exception e) {
		}
	}

	public void close() {
		sendPacket(new KickPacket());
		run = false;
	}
}
