package me.ranol.servertransfer.swtutil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MessageView {
	MessageBox box;

	public MessageView(Shell shell, int type) {
		box = new MessageBox(shell, type);
	}

	public MessageView title(String title) {
		box.setText(title);
		return this;
	}

	public MessageView message(String message) {
		box.setMessage(message);
		return this;
	}

	public int open() {
		return box.open();
	}

	public static MessageView info(Shell shell) {
		return new MessageView(shell, SWT.ICON_INFORMATION | SWT.OK);
	}
}
