package org.ahands.thirdeye.gui.settings;

import org.ahands.thirdeye.gui.About;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class SettingsTabs {

	public SettingsTabs(Composite comp) {
		addComp(comp);
	}

	TabItem settingsTab;

	private void addComp(Composite comp) {

		final TabFolder tabFolder = new TabFolder(comp, SWT.BORDER);

		final TabItem controllersTab = new TabItem(tabFolder, SWT.NONE);
		controllersTab.setText("Controllers");
		final Composite controllersComp = new Composite(tabFolder, SWT.BORDER);
		controllersComp.setLayout(new FillLayout(SWT.VERTICAL));
		new ControllerSettings(controllersComp);
		controllersTab.setControl(controllersComp);

		settingsTab = new TabItem(tabFolder, SWT.NONE);
		settingsTab.setText("Settings");
		final Composite settingsComp = new Composite(tabFolder, SWT.BORDER);
		settingsComp.setLayout(new FillLayout(SWT.VERTICAL));
		new MouseGrabberSettings(settingsComp);
		settingsTab.setControl(settingsComp);

		final TabItem infoTab = new TabItem(tabFolder, SWT.NONE);
		infoTab.setText("Info");

		final TabItem aboutTab = new TabItem(tabFolder, SWT.NONE);
		aboutTab.setText("About");
		final Composite aboutComp = new Composite(tabFolder, SWT.BORDER);
		aboutComp.setLayout(new FillLayout(SWT.VERTICAL));
		new About(aboutComp);
		aboutTab.setControl(aboutComp);

	}

	public void changeSettingsTab(final String text) {
		this.settingsTab.setText(text);
	}
}
