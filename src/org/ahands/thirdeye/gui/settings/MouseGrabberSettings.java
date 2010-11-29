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
		Setting deadZone = new Setting(comp, "Dead zone");
		deadZone.setScale(25);

		Setting xAxis = new Setting(comp, "Acceleration x-axis");
		xAxis.setScale(5);

		Setting yAxis = new Setting(comp, "Acceleration y-axis");
		yAxis.setScale(10);
	}

	private class Setting {
		final Scale settingScale;
		final Text settingValue;

		public Setting(Composite comp, String title) {
			final Composite settingComp = new Composite(comp, SWT.BORDER);
			settingComp.setLayout(new FillLayout(SWT.VERTICAL));

			final RowLayout infoLayout = new RowLayout();
			infoLayout.center = true;

			final Composite infoComp = new Composite(settingComp, SWT.NONE);
			infoComp.setLayout(infoLayout);

			final Label settingLabel = new Label(infoComp, SWT.CENTER);
			settingLabel.setText(title);

			settingValue = new Text(infoComp, SWT.BORDER | SWT.SINGLE);
			settingValue.setText("000");

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

		public void setScale(int val) {
			settingScale.setSelection(val);
			settingValue.setText("" + val);
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
