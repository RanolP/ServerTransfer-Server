package me.ranol.servertransfer.packet;

import org.eclipse.swt.widgets.Display;

public class SWTRun {
	public static void runAsync(Runnable run) {
		Display.getDefault().asyncExec(run);
	}
}
