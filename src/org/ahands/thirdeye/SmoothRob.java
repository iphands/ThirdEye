package org.ahands.thirdeye;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public class SmoothRob {

	final Robot rob;
	final Point origin;
	final int threshold;

	public SmoothRob(final Point origin, final int threshold) throws AWTException {
		this.rob = new Robot();
		this.origin = origin;
		this.threshold = threshold;
	}

	private void sMoveX(Point current, int distance) {
		for (int i = 0, j = Math.abs(distance); i < j; i++) {
			if (distance < 0) {
				current.x++;
			} else {
				current.x--;
			}
			rob.mouseMove(current.x, current.y);
		}
	}

	private void sMoveY(Point current, int distance) {
		for (int i = 0, j = Math.abs(distance); i < j; i++) {
			if (distance > 0) {
				current.y++;
			} else {
				current.y--;
			}
			rob.mouseMove(current.x, current.y);
		}
	}

	public void smoothMouseMove(Point dotLocation) {
		Point current = MouseInfo.getPointerInfo().getLocation();

		if (Math.abs(dotLocation.x - origin.x) > threshold) {
			int distance = dotLocation.x - origin.x;
			distance = distance / threshold;
			this.sMoveX(current, distance);
		}

		current = MouseInfo.getPointerInfo().getLocation();
		if (Math.abs(dotLocation.y - origin.y) > threshold) {
			int distance = dotLocation.y - origin.y;
			distance = distance / threshold;
			this.sMoveY(current, distance);
		}
	}

	public void moveMouse(Point dotLocation) {
		Point current = MouseInfo.getPointerInfo().getLocation();

		if (dotLocation.x > (origin.x + threshold)) {
			int rate = dotLocation.x - origin.x;
			rob.mouseMove(current.x - (rate), current.y);
		} else if (dotLocation.x < (origin.x - threshold)) {
			int rate = origin.x - dotLocation.x;
			rob.mouseMove(current.x + (rate), current.y);
		}

		current = MouseInfo.getPointerInfo().getLocation();
		if (dotLocation.y > (origin.y + threshold)) {
			int rate = dotLocation.y - origin.y;
			rob.mouseMove(current.x, (current.y + rate));
		} else if (dotLocation.y < (origin.y - threshold)) {
			int rate = origin.y - dotLocation.y;
			rob.mouseMove(current.x, (current.y - rate));
		}
	}
}
