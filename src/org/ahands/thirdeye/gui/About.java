package org.ahands.thirdeye.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class About {
	public About(Composite comp) {
		initGUI(comp);
	}

	private void initGUI(Composite comp) {
		final Label label = new Label(comp, SWT.NONE);
		label.setText("By Ian Page Hands\nFor Yo Adrian!");
	}
}
