package concept;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import params.Params;

public class Vehicle extends PhysicalObject implements VehicleInterface {


	private int id;

	private Terrain terrain;

	/** Current speed. */
	private double speed;

	/** How far the vehicle can see, in a radius.
	 * Should logically be higher than {@code discoveryRadius}. */
	private double viewRadius;

	/** Traveled Distance by the vehicle. */
	private double traveledDistance;

	private double sX=0;
	private double sY=0;

	private HashMap<Direction,Zone> perceptions;

	public Vehicle(Terrain t, int id, double x, double y) {
		super(x,y);
		this.terrain=t;

		this.id=id;

		this.x = x;
		this.y = y;

		this.speed = Params.minSpeed;

		this.viewRadius = Params.vehicleViewRadius;

		this.traveledDistance = 0;

		perceptions=new HashMap<>();
	}


	public int getID(){
		return id;
	}

	public double getRight() { return this.x + Params.vehicle_width; }
	/** Returns the Y-axis position, plus height. */
	public double getBottom() { return this.y + Params.vehicle_height; }
	/** Returns the X-axis position, plus half the width. */
	public double getCenterX() { return this.x + (Params.vehicle_width/2.0); }
	/** Returns the Y-axis position, plus half the height. */
	public double getCenterY() { return this.y + (Params.vehicle_height/2.0); }


	public void tryToMove(double x, double y) {
		sX=this.x;
		sY=this.y;

		this.x = x;
		this.y = y;
	}


	public void revert(){
		if((sX != 0)&&(sY!=0)){
			x=sX;
			y=sY;
		}
	}


	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getSpeed() { return this.speed; }

	public double getViewRadius() { return this.viewRadius; }
	public double getTravelledDistance() { return this.traveledDistance; }

	/** Updates position based on given input. */
	@Override
	public void move(Direction d) {
		terrain.move(d, id);
	}

	protected void updateTravelledDistance(double oldX, double oldY) {
		this.traveledDistance +=
				Math.abs(oldX - this.x) +
				Math.abs(oldY - this.y);
	}

	@Override
	public List<Direction> undiscovered() {
		 List<Direction> result=new ArrayList<>();
		for(Direction d: perceptions.keySet()){
			Zone zone=perceptions.get(d);
			if(zone.getPourcentDecouverte() > 0){
				result.add(d);
			}
		}

		return result;
	}

	public HashMap<Direction,Zone> percevoirZones(){
		perceptions=new HashMap<>();

		HashMap<Direction,Double[]> stats=terrain.evaluate(getCenterX(), getCenterY());

		for(Direction d:stats.keySet()){
			Zone z=perceptions.get(d);
			if(z == null){
				z=new Zone(d);
			}
			z.setPourcentDecouverte(stats.get(d)[0]);
			z.setDensiteDecouverte(stats.get(d)[1]);
			perceptions.put(d, z);
		}


		List<Obstacle> OenVue=terrain.obstaclesEnVue(id);
		for(Obstacle o:OenVue){
			Direction d=getDirectionTo(o.getCenterX(), o.getCenterY());
			if(d != null){
				Zone z=perceptions.get(d);
				if(z == null){
					z=new Zone(d);
				}

				if(o.isBordure())
					z.setBordure(true);
				else z.addObstacle();
				perceptions.put(d, z);
			}
		}


		List<Vehicle> VenVue=terrain.vehiculesEnVue(id);
		for(Vehicle v:VenVue){
			Direction d=getDirectionTo(v.getCenterX(), v.getCenterY());
			if(d != null){
				Zone z=perceptions.get(d);
				if(z == null){
					z=new Zone(d);
				}

				z.addVehicule(v.getID());
				perceptions.put(d, z);
			}
		}

		return perceptions;
	}

	@Override
	public List<Direction> otherVehicules() {
		List<Vehicle> enVue=terrain.vehiculesEnVue(id);
		List<Direction> r=new ArrayList<Direction>();
		for(Vehicle v:enVue){
			Direction d=getDirectionTo(v.getCenterX(), v.getCenterY());
			if(d != null)
				r.add(d);
		}
		return r;
	}

	@Override
	public List<Direction> obstacles() {
		List<Obstacle> enVue=terrain.obstaclesEnVue(id);
		List<Direction> r=new ArrayList<Direction>();
		for(Obstacle o:enVue){
			Direction d=getDirectionTo(o.getCenterX(), o.getCenterY());
			if(d != null && !o.isBordure())
				r.add(d);
		}
		return r;
	}


	private Double dist(double x1, double y1, double x2, double y2){
		Double res = Math.sqrt((y1 - y2)*(y1 - y2) + (x1 - x2)*(x1 - x2));
		return res;
	}


	@SuppressWarnings("incomplete-switch")
	@Override
	public Map<Direction, Integer> obstaclesDist() {
		List<Obstacle> enVue=terrain.obstaclesEnVue(id);
		Map<Direction, Integer> r=new HashMap<Direction, Integer>();
		for(Obstacle o:enVue){
			Direction d=getDirectionTo(o.getCenterX(), o.getCenterY());
			if(d != null){
				switch (d) {
				case Nord :
					r.put(d, (int)(this.getTop() - o.getBottom()));
					break;
				case NE :
					r.put(d, this.dist(this.getRight(), this.getTop(), o.getLeft(), o.getBottom()).intValue());
					break;
				case Est :
					r.put(d, (int)(o.getLeft() - this.getRight()));
					break;
				case SE :
					r.put(d, this.dist(this.getRight(), this.getBottom(), o.getLeft(), o.getTop()).intValue());
					break;
				case Sud :
					r.put(d, (int)(o.getTop() - this.getBottom()));
					break;
				case SO :
					r.put(d, this.dist(this.getLeft(), this.getBottom(), o.getRight(), o.getTop()).intValue());
					break;
				case Ouest :
					r.put(d, (int)(this.getLeft() - o.getRight()));
					break;
				case NO :
					r.put(d, this.dist(this.getLeft(), this.getTop(), o.getRight(), o.getBottom()).intValue());
				}
			}
		}
		return r;
	}


	@Override
	public Rectangle2D getShape() {
		return new Rectangle2D.Double(x,y,Params.vehicle_width,Params.vehicle_height);
	}

	public Ellipse2D getVisionShape(){
		return new Ellipse2D.Double(getCenterX()-Params.vehicleViewRadius, getCenterY()-Params.vehicleViewRadius, Params.vehicleViewRadius * 2, Params.vehicleViewRadius * 2);
	}


	public void setX(double d){
		this.x=d;
	}


	public void setY(double d) {
		this.y=d;
	}


	private Direction getDirectionTo(double x, double y) {
		return Direction.getDirectionTOFrom( x, y, getCenterX(), getCenterY(),Double.max(Params.vehicle_height, Params.vehicle_width));
	}


}
