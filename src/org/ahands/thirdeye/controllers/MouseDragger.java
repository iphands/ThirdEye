package org.ahands.thirdeye.controllers;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Toolkit;
import java.util.Calendar;

public class MouseDragger {
	final private Robot rob;
	final int camWidth;
	final int camHeight;
	final int screenWidth;
	final int screenHeight;
	final int defaultXSpeed;
	final int defaultYSpeed;
	final Point camCenter;
	final Point screenCenter;

	private float deadzone = (float) 1.5;
	private float xAccel = (float) 1.5;
	private float yAccel = (float) 1.2;

	public float getDeadzone() {
		return deadzone;
	}

	public void setDeadzone(float deadzone) {
		this.deadzone = deadzone;
	}

	public float getxAccel() {
		return xAccel;
	}

	public void setxAccel(float xAccel) {
		this.xAccel = xAccel;
	}

	public float getyAccel() {
		return yAccel;
	}

	public void setyAccel(float yAccel) {
		this.yAccel = yAccel;
	}

	private Shape bounds = null;
	private Shape bounds2 = null;

	public MouseDragger(int camWidth, int camHeight) throws AWTException {
		this.rob = new Robot();
		this.camHeight = camHeight;
		this.camWidth = camWidth;

		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenWidth = (int) screenSize.getWidth();
		this.screenHeight = (int) screenSize.getHeight();

		this.defaultXSpeed = screenWidth / camWidth;
		this.defaultYSpeed = (screenHeight / camHeight) * 2;

		this.camCenter = new Point();
		this.camCenter.x = camWidth / 2;
		this.camCenter.y = camHeight / 2;

		this.screenCenter = new Point();
		this.screenCenter.x = screenWidth / 2;
		this.screenCenter.y = screenHeight / 2;

	}

	public Shape getBounds() {
		return bounds;
	}

	public void setBounds(Shape bounds) {
		this.bounds = bounds;
	}

	private Point oldLocation = MouseInfo.getPointerInfo().getLocation();
	private long oldTime = Calendar.getInstance().getTimeInMillis();
	private long time = 0;

	public void move(final Point dotLocation) {
		time = Calendar.getInstance().getTimeInMillis() - oldTime;
		final float speed = (float) (((Math.abs(oldLocation.distance(dotLocation)) / time) * 100) / 1.2);

		if (speed > deadzone) {
			final Point mousePos = MouseInfo.getPointerInfo().getLocation();
			final int x_move;
			final int y_move;

			x_move = (int) ((dotLocation.x - oldLocation.x) * (speed * (speed / xAccel)));
			y_move = (int) ((dotLocation.y - oldLocation.y) * (speed * (speed / yAccel)));

			rob.mouseMove(x_move + mousePos.x, y_move + mousePos.y);
		}

		oldLocation = dotLocation;
		oldTime = Calendar.getInstance().getTimeInMillis();
	}

	public void setBounds2(Shape bounds2) {
		this.bounds2 = bounds2;
	}

	public Shape getBounds2() {
		return bounds2;
	}
}
