package model;

import javafx.animation.Animation;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Turtle {
	private boolean lifted = false;
	private double currentX = 0, currentY = 600;
	private double rotation = 0;
	private Animation animation;

	private final int minXBound = 0;
	private final int minYBound = 0;
	private final int maxXBound = 1200;
	private final int maxYBound = 600;
	
	private ImageView imageTurtle;

	public void Turtle() {
	}

	public void setImageView(ImageView imageTurtle){
		this.imageTurtle = imageTurtle;
	}
	
	public void set(int x, int y) {
		this.currentX = checkXCoordinateForSet(x);
		this.currentY = checkYCoordinateForSet(y);
		moveImageTurtle();
	}

	private void moveImageTurtle() {
		imageTurtle.setLayoutX(currentX-20);  // odj¹æ/dodaæ wymiary obrazka
		imageTurtle.setLayoutY(currentY-20);
	}

	private int checkXCoordinateForSet(int x) {
		if (x < minXBound)
			x = minXBound;
		else if (x > maxXBound)
			x = maxXBound;
		else
			x = x;
		return x;
	}

	private int checkYCoordinateForSet(int y) {
		if (y < minYBound)
			y = 600 - minYBound;
		else if (y > maxYBound)
			y = 600 - maxYBound;
		else
			y = 600 - y;
		return y;
	}

	public Path forward(int forward, Color color) {
		Path path = new Path();
		MoveTo moveTo = new MoveTo(currentX, currentY);

		double newX = currentX + (forward * Math.sin(rotation));
		double newY = currentY - (forward * Math.cos(rotation));
		checkCoordinatesForForward(newX, newY); //sprawdz czy nie przekraczaja granic pane
		if (this.isLifted()) {
			MoveTo moveTo2 = new MoveTo(currentX, currentY);
			moveImageTurtle();
			path.getElements().addAll(moveTo, moveTo2);
		} else {
			LineTo lineTo = new LineTo(currentX, currentY);
			moveImageTurtle();
			path.setStroke(color);
			path.getElements().addAll(moveTo, lineTo);
		}
		return path;
	}

	private void checkCoordinatesForForward(double newX, double newY) {
		double a = (newY - currentY) / (newX - currentX);
		double b = currentY - a * currentX;
		if (newX < minXBound) {
			newX = minXBound;
			newY = a * newX + b;
			if (newY < minYBound) {
				newY = minYBound;
				newX = (newY - b) / a;
			} else if (newY > maxYBound) {
				newY = maxYBound;
				newX = (newY - b) / a;
			}
		} else if (newX > maxXBound) {
			newX = maxXBound;
			newY = a * newX + b;
			if (newY < minYBound) {
				newY = minYBound;
				newX = (newY - b) / a;
			} else if (newY > maxYBound) {
				newY = maxYBound;
				newX = (newY - b) / a;
			}
		} else if (newY > maxYBound) {
			newY = maxYBound;
			newX = (newY - b) / a;
		} else if (newY < minYBound) {
			newY = minYBound;
			newX = (newY - b) / a;
		}

		currentX = newX;
		currentY = newY;
	}

	public void rotate(int angle) {
		this.rotation = rotation + Math.toRadians(angle);
		imageTurtle.setRotate(Math.toDegrees(rotation));
	}

	public boolean isLifted() {
		return lifted;
	}

	public void setLifted(boolean lifted) {
		this.lifted = lifted;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

}
