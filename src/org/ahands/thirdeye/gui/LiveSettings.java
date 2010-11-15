package org.ahands.thirdeye.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

public class LiveSettings implements Runnable {

	public LiveSettings() {

	}

	@Override
	public void run() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("ThirdEye");

		initGUI(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private void initGUI(final Shell shell) {
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		final Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		final MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
		fileItem.setText("&File");

		final Menu fileSubMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(fileSubMenu);

		new MenuItem(fileSubMenu, SWT.SEPARATOR);

		final MenuItem exitMenuItem = new MenuItem(fileSubMenu, SWT.PUSH);
		exitMenuItem.setText("E&xit");
		exitMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				// System.exit(0);
				shell.dispose();
			}
		});

		final Group imgsGroup = new Group(shell, SWT.SHADOW_ETCHED_IN | SWT.CENTER);
		imgsGroup.setText("Images");
		imgsGroup.setLayout(new GridLayout());
		imgsGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Group settingsGroup = new Group(shell, SWT.SHADOW_ETCHED_IN | SWT.CENTER);
		settingsGroup.setText("Settings");
		settingsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		doSettings(settingsGroup);
	}

	private void doSettings(Group settingsGroup) {
		final Scale deadzoneScale = new Scale(settingsGroup, SWT.BORDER);
		deadzoneScale.setSize(200, 64);
		deadzoneScale.setMaximum(50);
		deadzoneScale.setPageIncrement(1);

		final Scale xAccelScale = new Scale(settingsGroup, SWT.BORDER);
		xAccelScale.setSize(200, 64);
		xAccelScale.setMaximum(50);
		xAccelScale.setPageIncrement(1);

		final Scale yAccelScale = new Scale(settingsGroup, SWT.BORDER);
		yAccelScale.setSize(200, 64);
		yAccelScale.setMaximum(50);
		yAccelScale.setPageIncrement(1);

	}
}