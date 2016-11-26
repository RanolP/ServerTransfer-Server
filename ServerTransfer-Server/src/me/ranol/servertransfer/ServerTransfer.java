package me.ranol.servertransfer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ServerTransfer {
	public static Shell shell;
	private ServerManagement manager;
	Image imgStart = SWTResourceManager.getImage(ServerTransfer.class, "/image/start.png");
	Image imgStarted = SWTResourceManager.getImage(ServerTransfer.class, "/image/started.png");
	private Text text;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerTransfer window = new ServerTransfer();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.TITLE | SWT.CLOSE | SWT.MIN);
		shell.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/icon.png"));
		shell.setSize(600, 450);
		shell.setText("Server Transfer - Server");

		shell.addListener(SWT.Close, e -> {
			System.exit(-1);
		});

		TabFolder allTabs = new TabFolder(shell, SWT.NONE);
		allTabs.setBounds(0, 0, 594, 421);

		TabItem manageTab = new TabItem(allTabs, SWT.NONE);
		manageTab.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/management.png"));
		manageTab.setText("Management");

		Composite composite = new Composite(allTabs, SWT.NONE);
		manageTab.setControl(composite);

		Button start = new Button(composite, SWT.NONE);
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (manager.isStarted()) {
					manager.stopWork();
					start.setImage(imgStart);
				} else {
					manager.startWork();
					start.setImage(imgStarted);
				}
			}
		});
		start.setImage(imgStart);
		start.setBounds(10, 10, 75, 75);

		TabItem tbtmUserList = new TabItem(allTabs, SWT.NONE);
		tbtmUserList.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/list.png"));
		tbtmUserList.setText("User List");

		Composite composite_2 = new Composite(allTabs, SWT.NONE);
		tbtmUserList.setControl(composite_2);

		List users = new List(composite_2, SWT.BORDER);
		Clients.updator(users);
		users.setBounds(0, 0, 300, 378);

		TabItem tbtmAccountManagement = new TabItem(allTabs, SWT.NONE);
		tbtmAccountManagement.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/account.png"));
		tbtmAccountManagement.setText("Accounts");

		Composite composite_1 = new Composite(allTabs, SWT.NONE);
		tbtmAccountManagement.setControl(composite_1);

		List accounts = new List(composite_1, SWT.BORDER);
		accounts.setBounds(0, 0, 280, 378);

		text = new Text(composite_1, SWT.BORDER);
		text.setFont(SWTResourceManager.getFont("맑은 고딕", 12, SWT.NORMAL));
		text.setBounds(286, 40, 200, 35);

		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setBounds(492, 38, 84, 35);
		btnNewButton.setText("Apply");

		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("맑은 고딕", 12, SWT.BOLD));
		lblNewLabel.setBounds(286, 10, 84, 24);
		lblNewLabel.setText("Password");

		Button newAccount = new Button(composite_1, SWT.NONE);
		newAccount.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AuthDialog ad = new AuthDialog(shell, SWT.NONE);
				ad.open();
			}
		});
		newAccount.setBounds(282, 343, 150, 35);
		newAccount.setText("Create new..");

		Button delAccount = new Button(composite_1, SWT.NONE);
		delAccount.setText("Delete Account");
		delAccount.setBounds(436, 343, 150, 35);

		manager = new ServerManagement(2929, manager::run, shell);
		manager.start();
		manager.stopWork();
	}
}
