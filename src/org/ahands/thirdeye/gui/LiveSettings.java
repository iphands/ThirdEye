package org.ahands.thirdeye.gui;

import java.io.IOException;

import org.ahands.thirdeye.input.Cam;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class LiveSettings implements Runnable {
	Canvas canvas;
	final Cam cam = new Cam("/dev/video0");

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
				canvas.redraw();
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
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				try {
					Image image = cam.getSwtImg();
					image = new Image(Display.getCurrent(), image.getImageData().scaledTo(w, h));
					e.gc.drawImage(image, 0, 0);
					image.dispose();
				} catch (V4L4JException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

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