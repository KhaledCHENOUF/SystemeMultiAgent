package concept;

import java.util.ArrayList;
import java.util.List;

public class Zone {

	private Direction direction;
	private int nbObstacles;
	private List<Integer> idVehicules;
	private double pourcentDecouverte;
	private double densiteDecouverte;
	private boolean bordure;

	public Zone(Direction direction) {
		this.direction=direction;
		idVehicules=new ArrayList<Integer>();
		nbObstacles=0;
		pourcentDecouverte=0;
		densiteDecouverte=0;
		bordure=false;
	}

	public void addVehicule(int id){
		idVehicules.add(id);
	}

	public void addVehicules(List<Integer> ids){
		idVehicules.addAll(ids);
	}

	public int getNbObstacles() {
		return nbObstacles;
	}

	public void addObstacle() {
		this.nbObstacles = nbObstacles+1;
	}

	public double getPourcentDecouverte() {
		return pourcentDecouverte;
	}

	public void setPourcentDecouverte(double pourcentDecouverte) {
		this.pourcentDecouverte = pourcentDecouverte;
	}

	public double getDensiteDecouverte() {
		return densiteDecouverte;
	}

	public void setDensiteDecouverte(double densiteDecouverte) {
		this.densiteDecouverte = densiteDecouverte;
	}

	public boolean isBordure() {
		return bordure;
	}

	public void setBordure(boolean bordure) {
		this.bordure = bordure;
	}

	public Direction getDirection() {
		return direction;
	}

	public List<Integer> getIdVehicules() {
		return idVehicules;
	}

	@Override
	public String toString(){
		String r="Zone dans la direction "+direction;
		r=r.concat("\n Contient "+nbObstacles+" obstacles");
		r=r.concat("\n Contient "+idVehicules.size()+" voisins");
		r=r.concat("\n % non découverte "+pourcentDecouverte);
		r=r.concat("\n densité découverte "+densiteDecouverte);
		r=r.concat("\n contient une bordure ? "+bordure);

		return r;
	}





}
