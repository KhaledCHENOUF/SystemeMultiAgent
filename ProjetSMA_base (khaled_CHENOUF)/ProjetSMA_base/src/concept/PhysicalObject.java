package concept;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public abstract class PhysicalObject {

	protected double x;
	protected double y;


	public PhysicalObject(double x, double y) {
		this.x=x;
		this.y=y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public abstract Rectangle2D getShape();

	/** Returns the X-axis position. */
	public double getLeft() { return this.x; }
	/** Returns the Y-axis position. */
	public double getTop() { return this.y; }
	/** Returns the X-axis position, plus width. */
	public abstract double getRight();
	/** Returns the Y-axis position, plus height. */
	public abstract double getBottom();
	/** Returns the X-axis position, plus half the width. */
	public abstract double getCenterX();
	/** Returns the Y-axis position, plus half the height. */
	public abstract double getCenterY();


	public boolean intersect(PhysicalObject other){
		 Area a = new Area(getShape());
		 Area o=new Area(other.getShape());
		 a.intersect(o);
		 return !a.isEmpty();
	}

	public boolean intersect(Shape shape){
		 Area a = new Area(getShape());
		 Area o=new Area(shape);
		 a.intersect(o);
		 return !a.isEmpty();
	}


}
