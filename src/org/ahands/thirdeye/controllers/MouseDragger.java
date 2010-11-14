package org.ahands.thirdeye.controllers;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.util.Calendar;
import java.util.Date;

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
		final int speed = (int) (((Math.abs(oldLocation.distance(dotLocation)) / time) * 100) / 1.5);

		if (speed > 1) {
			final Point mousePos = MouseInfo.getPointerInfo().getLocation();
			final int x_move;
			final int y_move;

			if (speed > 3) {
				x_move = (((dotLocation.x - oldLocation.x) * defaultXSpeed) * speed) + mousePos.x;
				y_move = (((dotLocation.y - oldLocation.y) * defaultYSpeed) * speed) + mousePos.y;
			} else {
				x_move = (((dotLocation.x - oldLocation.x) * defaultXSpeed)) + mousePos.x;
				y_move = (((dotLocation.y - oldLocation.y) * defaultYSpeed)) + mousePos.y;
			}

			rob.mouseMove(x_move, y_move);
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
