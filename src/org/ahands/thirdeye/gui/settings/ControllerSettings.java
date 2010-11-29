package org.ahands.thirdeye.gui.settings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ControllerSettings {
	final Button[] radioButtons;
	String currentButton;

	public ControllerSettings(Composite comp) {
		this.radioButtons = new Button[3];
		initGUI(comp);
	}

	private void initGUI(final Composite comp) {

		radioButtons[0] = new Button(comp, SWT.RADIO);
		radioButtons[0].setText("Mouse Grabber");
		radioButtons[0].setSelection(true);
		radioButtons[0].addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {

			}
		});

		radioButtons[1] = new Button(comp, SWT.RADIO);
		radioButtons[1].setText("Mouse Joy");

		radioButtons[2] = new Button(comp, SWT.RADIO);
		radioButtons[2].setText("Missle Launcher");

		for (Button button : radioButtons) {
			button.pack();
		}
	}
}
