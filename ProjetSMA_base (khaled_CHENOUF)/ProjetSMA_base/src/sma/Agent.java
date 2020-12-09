package sma;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import concept.Direction;
import concept.VehicleInterface;
import concept.Zone;

public abstract class Agent extends Thread {
	protected VehicleInterface vehicle;
	private int id;

	public Agent(VehicleInterface vehicle, int id) {
		this.vehicle=vehicle;
		this.id=id;
	}

	public VehicleInterface getVehicle() {
		return vehicle;
	}

	public int getAgentId() {
		return id;
	}

	/**
	 * Principale méthode de perception du Véhicule
	 * A APPELER à chaque cycle de vie de l'agent
	 * @return une Map contenant, pour chaque Direction, une Zone avec les infos associées
	 */
	protected HashMap<Direction,Zone> percevoirZones(){
		return vehicle.percevoirZones();
	}

	/**
	 * Permet de déplacer le Véhicule dans la Direction d
	 * @param d la Direction que le Véhicule emprunte
	 */
	protected void move(Direction d){
		vehicle.move(d);
	}

	/**
	 *
	 * @return la liste des Direction dans lesquelles les Zones sont à découvrir (pourcentDecouverte > 0 %)
	 */
	protected List<Direction> undiscovered(){
		return vehicle.undiscovered();
	}
	/**
	 *
	 * @return la liste des Direction dans lesquelles on peut rencontrer un autre véhicule (dans le champ de perception)
	 */
	protected List<Direction> otherVehicules(){
		return vehicle.otherVehicules();
	}

	/**
	 *
	 * @return la liste des Direction dans lesquelles on peut rencontrer un obstacle (dans le champ de perception)
	 * Les bordures ne sont pas prises en compte
	 */
	protected List<Direction> obstacles(){
		return vehicle.obstacles();
	}


	protected Map<Direction, Integer> obstaclesDist(){
		return vehicle.obstaclesDist();
	}


}
