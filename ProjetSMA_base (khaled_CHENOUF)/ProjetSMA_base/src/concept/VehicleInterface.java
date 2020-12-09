package concept;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VehicleInterface {

	/**
	 * Permet de déplacer le Véhicule dans la Direction d
	 * @param d la Direction que le Véhicule emprunte
	 */
	public void move(Direction d);

	/**
	 *
	 * @return la liste des Direction dans lesquelles les Zones sont à découvrir (pourcentDecouverte > 0 %)
	 */
	public List<Direction> undiscovered();
	/**
	 *
	 * @return la liste des Direction dans lesquelles on peut rencontrer un autre véhicule (dans le champ de perception)
	 */
	public List<Direction> otherVehicules();

	/**
	 *
	 * @return la liste des Direction dans lesquelles on peut rencontrer un obstacle (dans le champ de perception)
	 * Les bordures ne sont pas prises en compte
	 */
	public List<Direction> obstacles();

	/**
	 *
	 * @return une Map des Direction dans lesquelles on peut rencontrer un obstacle (dans le champ de perception),
	 * avec la distance associée
	 */
	public Map<Direction, Integer> obstaclesDist();

	/**
	 * Principale méthode de perception du Véhicule
	 * A APPELER à chaque cycle de vie de l'agent
	 * @return une Map contenant, pour chaque Direction, une Zone avec les infos associées
	 */
	public HashMap<Direction,Zone> percevoirZones();


}
