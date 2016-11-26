package me.ranol.servertransfer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

import me.ranol.servertransfer.swtutil.MessageView;

public class ServerManagement extends Thread {
	ServerSocket server;
	boolean checkEnabled = false;

	public ServerManagement(int port, Runnable run, Shell opener) {
		super(run);
		try {
			server = new ServerSocket(port);
		} catch (Exception e) {
			MessageView.info(opener).title("서버 종료").message("서버를 여는데에 실패했습니다. \n" + e.getMessage()).open();
			System.exit(-1);
		}
	}

	@Override
	public synchronized void start() {
		startWork();
		super.start();
	}

	public void run() {
		while (true) {
			try {
				Socket soc = server.accept();
				if (!checkEnabled)
					continue;
				ClientManagement manage = new ClientManagement(soc);
				manage.start();
				System.out.println(soc.getInetAddress().getHostAddress() + ":" + soc.getPort() + "에서 계정 로그인되었습니다.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isStarted() {
		return checkEnabled;
	}

	public void stopWork() {
		new ArrayList<>(Clients.clients()).forEach(c -> {
			Clients.removeClient(c);
		});
		checkEnabled = false;
	}

	public void startWork() {
		checkEnabled = true;
	}
}
