package me.ranol.servertransfer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import me.ranol.servertransfer.swtutil.MessageView;

public class AuthDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text id;
	private Text pwd;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public AuthDialog(Shell parent, int style) {
		super(parent, style);
		setText("Auth Creation");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 160);
		shell.setText(getText());

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("맑은 고딕", 14, SWT.NORMAL));
		lblNewLabel.setBounds(10, 10, 115, 30);
		lblNewLabel.setText("Account ID : ");

		Label lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setText("Password : ");
		lblPassword.setFont(SWTResourceManager.getFont("맑은 고딕", 14, SWT.NORMAL));
		lblPassword.setBounds(10, 46, 115, 30);

		id = new Text(shell, SWT.BORDER);
		id.setFont(SWTResourceManager.getFont("맑은 고딕", 14, SWT.NORMAL));
		id.setBounds(131, 10, 135, 30);

		pwd = new Text(shell, SWT.BORDER);
		pwd.setFont(SWTResourceManager.getFont("맑은 고딕", 14, SWT.NORMAL));
		pwd.setBounds(131, 46, 135, 30);

		Button make = new Button(shell, SWT.NONE);
		make.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String idStr = id.getText();
				String pwdStr = pwd.getText();
				if (idStr.isEmpty()) {
					MessageView.info(shell).message("아이디는 공백일 수 없습니다!").title("생성 오류").open();
					return;
				}
				if (pwdStr.isEmpty()) {
					MessageView.info(shell).message("비밀번호는 공백일 수 없습니다!").title("생성 오류").open();
					return;
				}
				if (AuthService.exists(idStr)) {
					MessageView.info(shell).message("이미 존재하는 아이디입니다!").title("생성 오류").open();
					return;
				}
				AuthService.newAccount(idStr, pwdStr);
				MessageView.info(shell).message("계정이 생성되었습니다 \nID : " + idStr + "\nPWD : " + pwdStr).title("생성 성공")
						.open();
				shell.dispose();
			}
		});
		make.setBounds(40, 91, 140, 30);
		make.setText("Make New");

		Button cancel = new Button(shell, SWT.NONE);
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		cancel.setText("Cancel");
		cancel.setBounds(240, 91, 140, 30);

	}

}
