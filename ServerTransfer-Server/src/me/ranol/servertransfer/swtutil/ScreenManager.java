package me.ranol.servertransfer.swtutil;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;

public class ScreenManager {

	public static Rectangle getScreenSize() {
		Display d = Display.getCurrent();
		Monitor m = d.getPrimaryMonitor();
		Rectangle ret;
		if (m != null)
			ret = m.getClientArea();
		else
			ret = d.getBounds();
		return ret;
	}
}
