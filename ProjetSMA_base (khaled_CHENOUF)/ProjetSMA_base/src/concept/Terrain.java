package concept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ihm.Controls;
import javafx.application.Platform;
import params.Params;

public class Terrain extends Observable{

	/** Vehicle used on the terrain. */
	private HashMap<Integer,Vehicle> vehicles;

	private HashMap<Integer,Obstacle> obstacles;

	private boolean end=false;


	/** Fog cast over the terrain. */
	private Fog fog;


	private List<Thread> agents;

	public Terrain(Observer observer) {
		addObserver(observer);
		this.fog = new Fog(observer, Params.terrainSizeX, Params.terrainSizeY, Params.nbCellsX, Params.nbCellsY);
		vehicles=new HashMap<>();
		this.agents = new ArrayList<Thread>();

		initObstacles();
		initVehicles();
	}

	private void initObstacles() {
		obstacles=new HashMap<>();
		int i = 0;

		double x = 0;
		double y;
		for(y=0;y<Params.terrainSizeY;y=y+Params.obstacle_height){
			Obstacle left=new Obstacle(x, y);
			left.setBordure(true);
			obstacles.put(i,left);
			i++;
			Obstacle right=new Obstacle(Params.terrainSizeX-Params.obstacle_width, y);
			right.setBordure(true);
			obstacles.put(i,right);
			i++;
		}

		y=0;
		for(x=Params.obstacle_width;x<Params.terrainSizeX;x=x+Params.obstacle_width){
			Obstacle up=new Obstacle(x, y);
			up.setBordure(true);
			obstacles.put(i,up);
			i++;
			Obstacle down=new Obstacle(x, Params.terrainSizeY-Params.obstacle_width);
			down.setBordure(true);
			obstacles.put(i,down);
			i++;
		}


		for(int cpt=i;cpt<Params.OBSTACLE_NBR+i;cpt++){
			Obstacle o;
			do {
				x = Double.valueOf(Math.random() * Params.terrainSizeX);
				y = Double.valueOf(Math.random() * Params.terrainSizeY);
				o=new Obstacle(x, y);
			} while (collidingWithSomething(o));
			obstacles.put(cpt,o);
		}


	}

	private void initVehicles(){
		double xDep;
		double yDep;

		if(Params.START_AT_CENTER){
			double X=Params.terrainSizeX;
			double Y=Params.terrainSizeY;


			if ((X - 1) % 2 == 0) {
				xDep = X / 2;
			} else {
				xDep = (X - 1) / 2;
			}
			if ((Y - 1) % 2 == 0) {
				yDep = Y / 2;
			} else {
				yDep = (Y - 1) / 2;
			}

			double xv = xDep;
			double yv = yDep;

			for(int i=0;i<Params.VEHICULE_NBR;i++){
				Vehicle v = new Vehicle(this, i, xv, yv);
				vehicles.put(i, v);


				if (i == (Params.VEHICULE_NBR - 1) / 2) {
					xv = xDep;
					yv = yv + Params.vehicle_height;
				} else {
					xv = xv + Params.vehicle_width;
				}
			}
		}

		else{
			for(int i=0;i<Params.VEHICULE_NBR;i++){
				Vehicle v;
				do {
					xDep = Double.valueOf(Math.random() * Params.terrainSizeX);
					yDep = Double.valueOf(Math.random() * Params.terrainSizeY);
					v = new Vehicle(this, i, xDep, yDep);
				} while (collidingWithSomething(v));
				vehicles.put(i, v);
			}
		}

	}


	public Obstacle obstacle(int id){
		return obstacles.get(id);
	}

	/** Returns this terrain's vehicle. */
	public Vehicle vehicle(int id) { return vehicles.get(id); }
	/** Returns this terrain's fog. */
	public Fog fog() { return this.fog; }

	/** Returns how efficient the exploration is. */
	public double getEfficiency() {
		return (Params.terrainSizeX * Params.terrainSizeY) / calculCout();
	}

	private double calculCout() {
		double dist = 0;
		for (int id : vehicles.keySet()) {
			dist = dist + vehicles.get(id).getTravelledDistance();
		}
		return dist;
	}

	/** Verifies vehicle movement according to its position, and given input. */
	public void ihmControls(Controls input, int id) {
		move(input.getDirection(),id);
	}


	public synchronized void move(Direction d, int id) {
		if(!end){
			boolean moving = false;
			boolean colliding=false;

			Vehicle v=vehicles.get(id);

			double oldX = v.getLeft();
			double oldY = v.getTop();
			double speed=v.getSpeed();

			switch (d) {
			case Nord:
				v.tryToMove(oldX, oldY-speed);
				moving = true;
				break;
			case NO:
				v.tryToMove(oldX-speed, oldY-speed);
				moving = true;
				break;
			case Ouest:
				v.tryToMove(oldX-speed, oldY);
				moving = true;
				break;
			case SO:
				v.tryToMove(oldX-speed, oldY+speed);
				moving = true;
				break;
			case Sud:
				v.tryToMove(oldX, oldY+speed);
				moving = true;
				break;
			case SE:
				v.tryToMove(oldX+speed, oldY+speed);
				moving = true;
				break;
			case Est:
				v.tryToMove(oldX+speed, oldY);
				moving = true;
				break;
			case NE:
				v.tryToMove(oldX+speed, oldY-speed);
				moving = true;
				break;
			default:
				break;
			}

			if(collidingWithVehicules(v) || collidingWithObstacle(v)){
				v.revert();
				colliding=true;
				v.setSpeed(speed - Params.decel < Params.minSpeed ? Params.minSpeed : speed - Params.decel);
				moving=false;
			}
			else{
				// Horizontal collision with world bounds
				if (v.getRight() > Params.terrainSizeX) {
					v.setX(Params.terrainSizeX - Params.vehicle_width);
					colliding = true;
				} else if (v.getLeft() < 0) {
					v.setX(0);
					colliding = true;
				}

				// Vertical collisions with world bounds
				if (v.getBottom() > Params.terrainSizeY) {
					v.setY(Params.terrainSizeY - Params.vehicle_height);
					colliding = true;
				} else if (v.getTop() < 0) {
					v.setY(0);
					colliding = true;
				}
			}


			if (moving){
				if(!colliding){
					v.setSpeed(speed + Params.accel > Params.maxSpeed ? Params.maxSpeed : speed + Params.accel);
				}
				else{
					v.setSpeed(speed - Params.decel < Params.minSpeed ? Params.minSpeed : speed - Params.decel);
				}

				v.updateTravelledDistance(oldX, oldY);
				requestChange(id);
			}
		}

	}


	private boolean collidingWithSomething(PhysicalObject o){
		for(int i=Params.VEHICULE_NBR - 1;i>=0;i--){
			Vehicle v=vehicles.get(i);
			if(v != null){
				if(vehicles.get(i).intersect(o)){
					return true;
				}
			}
		}

		for(int i=0;i<obstacles.size();i++){
			if(obstacles.get(i).intersect(o)){
				return true;
			}
		}

		return false;
	}


	private boolean collidingWithVehicules(Vehicle v){
		for(int i=Params.VEHICULE_NBR - 1;i>=0;i--){
			if(v.getID() != i){
				if(vehicles.get(i).intersect(v)){
					return true;
				}
			}
		}
		return false;
	}

	private boolean collidingWithObstacle(Vehicle v){
		for(int i=0;i<obstacles.size();i++){
			if(obstacles.get(i).intersect(v)){
				return true;
			}
		}
		return false;
	}

	protected synchronized List<Obstacle> obstaclesEnVue(int id){
		List<Obstacle> enVue=new ArrayList<>();
		Vehicle v=vehicle(id);
		for(int i=0;i<obstacles.size();i++){
			if(obstacles.get(i).intersect(v.getVisionShape())){
				enVue.add(obstacle(i));
			}
		}
		return enVue;
	}

	protected synchronized List<Vehicle> vehiculesEnVue(int id){
		List<Vehicle> enVue=new ArrayList<>();
		Vehicle v=vehicle(id);
		for(int i=Params.VEHICULE_NBR - 1;i>=0;i--){
			if(v.getID() != i){
				if(vehicles.get(i).intersect(v.getVisionShape())){
					enVue.add(vehicles.get(i));
				}
			}
		}
		return enVue;
	}


	public void ready() {
		for(int i=0;i<Params.VEHICULE_NBR;i++){
			requestChange(i);
		}
	}

	/** Notifies observers of a new position. */
	private void requestChange(int id) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				setChanged();
				notifyObservers(id);
			}
		});
	}

	public HashMap<Integer,Obstacle> getObstacles() {
		return obstacles;
	}

	public HashMap<Direction, Double[]> evaluate(double centerX, double centerY) {
		return fog.evaluate(centerX, centerY);
	}

	public void end(){
		end=true;
		this.agents.forEach(agent -> agent.interrupt());
	}

	public void addAgent(Thread a) {
		this.agents.add(a);
	}

	public List<Thread> agents(){
		return this.agents;
	}

}
