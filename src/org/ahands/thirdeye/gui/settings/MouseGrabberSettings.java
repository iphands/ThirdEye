package org.ahands.thirdeye.gui.settings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

public class MouseGrabberSettings {
	public MouseGrabberSettings(Composite comp) {
		initGUI(comp);
	}

	private void initGUI(Composite comp) {
		Setting dz = new Setting(comp, "Dead zone:");
		dz.addListener();

		Setting xa = new Setting(comp, "Acceleration x-axis:");

		Setting ya = new Setting(comp, "Acceleration y-axis:");
	}

	private class Setting {
		final Scale settingScale;

		public Setting(Composite comp, String title) {
			final Composite settingComp = new Composite(comp, SWT.BORDER);
			settingComp.setLayout(new FillLayout(SWT.VERTICAL));

			final Composite infoComp = new Composite(settingComp, SWT.NONE);
			infoComp.setLayout(new RowLayout());
			final Label settingLabel = new Label(infoComp, SWT.NULL);
			settingLabel.setText(title);
			final Text settingValue = new Text(infoComp, SWT.BORDER | SWT.SINGLE);

			settingScale = new Scale(settingComp, SWT.BORDER);
			settingScale.setSize(200, 64);
			settingScale.setMaximum(50);
			settingScale.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event e) {
					settingValue.setText("" + settingScale.getSelection());
				}
			});
		}

		public void addListener() {
			settingScale.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event arg0) {

				}
			});
		}
	}
}
