package org.ahands.thirdeye.gui;

import org.ahands.thirdeye.gui.settings.SettingsTabs;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MainGUI implements Runnable {
	public static MainGUI instance = null;
	private Shell shell;
	private Canvas canvas;
	private CamImg camImg;
	private DetectionImg detectionImg;
	private Group imgsGroup;

	protected MainGUI() {
	}

	public static MainGUI getInstance() {
		if (instance == null) {
			instance = new MainGUI();
		}
		return instance;
	}

	@Override
	public void run() {
		Display display = new Display();
		shell = new Shell(display);
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

	public void toggleCamImg(boolean bool) {
		camImg.setVisible(bool);
	}

	public void toggleDetectionImg(boolean bool) {

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

		imgsGroup = new Group(shell, SWT.SHADOW_ETCHED_IN | SWT.CENTER);
		imgsGroup.setText("Images");
		imgsGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
		imgsGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		camImg = new CamImg(imgsGroup);
		detectionImg = new DetectionImg(imgsGroup);

		final Group settingsGroup = new Group(shell, SWT.SHADOW_ETCHED_IN | SWT.CENTER);
		settingsGroup.setText("Settings");
		settingsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		new SettingsTabs(settingsGroup);
	}

	private class DetectionImg {
		final Group detectionImgGroup;

		public DetectionImg(Composite comp) {
			detectionImgGroup = new Group(imgsGroup, SWT.SHADOW_ETCHED_IN | SWT.CENTER);
			detectionImgGroup.setText("Detected Pixels");
			detectionImgGroup.setLayout(new FillLayout(SWT.CENTER));
			detectionImgGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		}

		public void dispose() {
			detectionImgGroup.dispose();
		}
	}

	private class CamImg {
		final Group camImgGroup;

		public CamImg(Composite comp) {
			camImgGroup = new Group(comp, SWT.SHADOW_ETCHED_IN | SWT.CENTER);
			camImgGroup.setText("Camera Image");
			camImgGroup.setLayout(new FillLayout(SWT.CENTER));
			camImgGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		}

		public void setVisible(boolean visible) {
			camImgGroup.setVisible(visible);
		}
	}
}