package org.ahands.thirdeye.controllers;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;

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
		this.defaultYSpeed = screenHeight / camHeight;

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

	private Shape getCenteredBox(final Point mousePoint) {
		int bsize = 50;
		return new Ellipse2D.Float(mousePoint.x - (bsize / 2), mousePoint.y - (bsize / 2), bsize, bsize);
	}

	private boolean inBounds(final Point mousePoint) {
		if (bounds.contains(mousePoint)) {
			return true;
		}
		return false;
	}

	private Point oldLocation = new Point(0, 0);

	private int getDirection(final Point current, final Point old) {
		int dir = 0;

		if (current.y < old.y) {
			dir += 1;
		} else if (current.y > old.y) {
			dir += 4;
		}

		if (current.x > old.x) {
			dir += 2;
		} else if (current.x < old.x) {
			dir += 8;
		}

		return dir;
	}

	private boolean oob = false;

	// public void move(final Point dotLocation) {
	// if (bounds == null) {
	// bounds = getCenteredBox(dotLocation);
	// }
	//
	// if (!inBounds(dotLocation) || oob) {
	// oob = true;
	// int dist = (int) (bounds.getBounds().width * .80);
	// int x_point = (int) bounds.getBounds().getCenterX() - (dist / 2);
	// int y_point = (int) bounds.getBounds().getCenterY() - (dist / 2);
	// bounds2 = new Ellipse2D.Float(x_point, y_point, dist, dist);
	//
	// if (bounds2.contains(dotLocation)) {
	// oob = false;
	// System.out.println("resetting bounds -- " + new Date());
	// bounds = getCenteredBox(dotLocation);
	// bounds2 = null;
	// }
	//
	// oldLocation = dotLocation;
	// return;
	// }
	//
	// // int new_x = bounds.getBounds().x - dotLocation.x;
	// // int new_y = bounds.getBounds().y - dotLocation.y;
	// //
	// // Point mouse = MouseInfo.getPointerInfo().getLocation();
	// // rob.mouseMove(mouse.x + new_x, mouse.y + new_y);
	//
	// oldLocation = dotLocation;
	// return;
	// }

	// public void move(final Point dotLocation) {
	// if (bounds == null) {
	// bounds = getCenteredBox(dotLocation);
	// }
	//
	// if (!inBounds(dotLocation)) {
	// int x_dist = (int) (oldLocation.x - bounds.getBounds().getCenterX());
	// x_dist *= x_dist;
	// int y_dist = (int) (oldLocation.y - bounds.getBounds().getCenterY());
	// y_dist *= y_dist;
	// int dist = (((int) Math.sqrt(x_dist + y_dist)) * 2) - 2;
	// int x_point = (int) bounds.getBounds().getCenterX() - (dist / 2);
	// int y_point = (int) bounds.getBounds().getCenterY() - (dist / 2);
	// bounds2 = new Ellipse2D.Float(x_point, y_point, dist, dist);
	//
	// if (bounds2.contains(dotLocation)) {
	// System.out.println("resetting bounds -- " + new Date());
	// bounds = getCenteredBox(dotLocation);
	// bounds2 = null;
	// }
	//
	// oldLocation = dotLocation;
	// return;
	// }
	//
	// int new_x = dotLocation.x - bounds.getBounds().x;
	// int new_y = dotLocation.y - bounds.getBounds().y;
	//
	// Point mouse = MouseInfo.getPointerInfo().getLocation();
	// rob.mouseMove(mouse.x + new_x, mouse.y + new_y);
	//
	// oldLocation = dotLocation;
	// return;
	// }

	public void move(final Point dotLocation) {
		rob.mouseMove((dotLocation.x * defaultXSpeed) * 2, (dotLocation.y * defaultYSpeed) * 2);
	}

	public void setBounds2(Shape bounds2) {
		this.bounds2 = bounds2;
	}

	public Shape getBounds2() {
		return bounds2;
	}
}
