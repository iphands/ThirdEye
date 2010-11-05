package org.ahands.thirdeye.gui;

import java.awt.Choice;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LiveSettings extends Panel {
	private static final long serialVersionUID = 8635373202393976754L;
	Choice choice;
	Scrollbar thresholdScroll;

	public LiveSettings() {
		setLayout(new GridLayout(2, 1));
		choice = new Choice();
		final File dev = new File("/dev/");
		List<File> tmpList = Arrays.asList(dev.listFiles());
		Collections.reverse(tmpList);
		for (File file : tmpList) {
			final String fileName = file.getName();
			if (fileName.startsWith("video")) {
				choice.add("/dev/" + fileName);
			}
		}
		choice.setEnabled(false);
		add(choice);

		thresholdScroll = new Scrollbar(Scrollbar.HORIZONTAL, 0, 100, 1, 100);
		// thresholdScroll.addAdjustmentListener(this);
		this.add(thresholdScroll);
	}

	public boolean action(Event event, Object object) {
		if (event.target == choice) {
			// ThirdEye.camPath = choice.getSelectedItem();
			return (true);
		} else {
			return (false);
		}
	}
}
