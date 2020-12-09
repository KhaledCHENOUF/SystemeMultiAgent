package concept;

import java.awt.geom.Rectangle2D;

import params.Params;

public class Obstacle extends PhysicalObject {
	private Rectangle2D shape;
	private boolean bordure=false;

	public Obstacle(double x, double y) {
		super(x, y);
		shape=new Rectangle2D.Double(x,y,Params.obstacle_width,Params.obstacle_height);

	}


	/** Returns the X-axis position, plus width. */
	public double getRight() { return this.x + Params.obstacle_width; }
	/** Returns the Y-axis position, plus height. */
	public double getBottom() { return this.y + Params.obstacle_height; }
	/** Returns the X-axis position, plus half the width. */
	public double getCenterX() { return this.x + (Params.obstacle_width/2.0); }
	/** Returns the Y-axis position, plus half the height. */
	public double getCenterY() { return this.y + (Params.obstacle_height/2.0); }


	@Override
	public Rectangle2D getShape() {
		return shape;
	}


	public boolean isBordure() {
		return bordure;
	}


	public void setBordure(boolean bordure) {
		this.bordure = bordure;
	}



}
