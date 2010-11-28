package org.ahands.thirdeye.gui;

import org.ahands.thirdeye.gui.settings.SettingsTabs;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MainGUI implements Runnable {
	Canvas canvas;

	// final Cam cam = new Cam("/dev/video0");

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

		final int w = 400;
		final int h = 300;

		final Group imgsGroup = new Group(shell, SWT.SHADOW_ETCHED_IN | SWT.CENTER);
		imgsGroup.setText("Images");
		imgsGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
		imgsGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Group camImgGroup = new Group(imgsGroup, SWT.SHADOW_ETCHED_IN | SWT.CENTER);
		camImgGroup.setText("Cam");
		camImgGroup.setLayout(new FillLayout(SWT.CENTER));
		camImgGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		camImgGroup.setSize(w, h);

		canvas = new Canvas(camImgGroup, SWT.NO_REDRAW_RESIZE);
		canvas.setSize(w, h);

		final Group settingsGroup = new Group(shell, SWT.SHADOW_ETCHED_IN | SWT.CENTER);
		settingsGroup.setText("Settings");
		settingsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		new SettingsTabs(settingsGroup);
	}

}