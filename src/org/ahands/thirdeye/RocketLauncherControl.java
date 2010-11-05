package org.ahands.thirdeye;

import java.awt.Point;
import java.io.IOException;

public class RocketLauncherControl {

	private Runtime rt = Runtime.getRuntime();
	final private int mid_x;
	final private int mid_y;

	public RocketLauncherControl(int mid_x, int mid_y) {
		this.mid_x = mid_x;
		this.mid_y = mid_y;
	}

	public void move(final Point target) {
		final int xRate = target.x - mid_x;
		final int yRate = target.y - mid_y;

		final int xRateAbs = Math.abs(target.x - mid_x);
		final int yRateAbs = Math.abs(target.y - mid_y);

		if (xRateAbs > yRateAbs) {
			if (xRateAbs > 30) {
				System.out.println("doing straight move!");
				moveX(xRate);
				return;
			}
			moveY(yRate);
			moveX(xRate);
		} else {
			moveX(xRate);
			moveY(yRate);
		}
	}

	int xThreshold = 10;
	int yThreshold = 10;

	private void moveX(int distance) {
		System.out.println("moving rocket launcher x: " + distance);
		try {
			if (distance < (xThreshold * -1)) {
				rt.exec("rocket_launcher right " + Math.abs(distance));
			} else if (distance > (xThreshold)) {
				rt.exec("rocket_launcher left " + Math.abs(distance));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void moveY(int distance) {
		System.out.println("moving rocket launcher y: " + distance);

		try {
			if (distance < (yThreshold * -1)) {
				rt.exec("rocket_launcher up " + Math.abs(distance * 2));
			} else if (distance > (yThreshold)) {
				rt.exec("rocket_launcher down " + Math.abs(distance * 2));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
