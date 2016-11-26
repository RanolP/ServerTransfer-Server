package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.ranol.servertransfer.AuthService;
import me.ranol.servertransfer.AuthService.Auth;
import me.ranol.servertransfer.Connectors;

public class LoginPacket implements Packet {
	String id;
	String pwd;

	static class LoginResponse {
		public static final byte PWD_NOT_EQUAL = 0;
		public static final byte ACCOUNT_NOT_EXISTS = 1;
		public static final byte SUCESS_LOGIN = 2;
	}

	@Override
	public int id() {
		return 0;
	}

	@Override
	public boolean ping(DataInputStream in) throws IOException {
		id = in.readUTF();
		pwd = in.readUTF();
		AuthService.getAccount(id, pwd).setSalt(in.readUTF());
		return AuthService.canLogin(id, pwd);
	}

	@Override
	public void pong(DataOutputStream out, boolean ret) throws IOException {
		if (!AuthService.exists(id))
			out.writeInt(LoginResponse.ACCOUNT_NOT_EXISTS);
		else if (!ret)
			out.writeInt(LoginResponse.PWD_NOT_EQUAL);
		else
			out.writeInt(LoginResponse.SUCESS_LOGIN);
		if (!ret)
			return;
		String uuid = AuthService.getUUID(id);
		out.writeUTF(uuid);
		Connectors.add(uuid);
	}

	public Auth getAuth() {
		return AuthService.getAccount(id, pwd);
	}

}
