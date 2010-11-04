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
		moveX(target.x - mid_x);
		moveY(target.y - mid_y);
	}

	int threshold = 15;

	private void moveX(int distance) {
		System.out.println("moving rocket launcher x: " + distance);
		try {
			if (distance < (threshold * -1)) {
				rt.exec("rocket_launcher right " + Math.abs(distance));
			} else if (distance > (threshold)) {
				rt.exec("rocket_launcher left " + Math.abs(distance));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void moveY(int distance) {
		distance = distance * 2;
		System.out.println("moving rocket launcher y: " + distance);
		try {
			if (distance < (threshold * -1)) {
				rt.exec("rocket_launcher up " + Math.abs(distance));
			} else if (distance > (threshold)) {
				rt.exec("rocket_launcher down " + Math.abs(distance));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
