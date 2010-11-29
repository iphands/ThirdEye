package org.ahands.thirdeye.gui.settings;

import org.ahands.thirdeye.gui.MainGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ImageSettings {

	public ImageSettings(Composite comp) {
		initGUI(comp);
	}

	private void initGUI(final Composite comp) {
		Button[] checkBoxes = new Button[2];
		checkBoxes[0] = new Button(comp, SWT.CHECK);
		checkBoxes[0].setText("Toggle camera image");
		checkBoxes[0].setSelection(true);
		checkBoxes[0].addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				MainGUI.getInstance().toggleCamImg(((Button) e.widget).getSelection());
			}
		});

		checkBoxes[1] = new Button(comp, SWT.CHECK);
		checkBoxes[1].setText("Toggle detection image");
		checkBoxes[1].setSelection(true);
		for (Button button : checkBoxes) {
			button.pack();
		}

	}
}
