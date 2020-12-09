/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concept;

import java.util.List;

/**
 *
 * @author riviere
 */
public enum Direction {
	NONE(-1),Nord(0),NE(1),Est(2),SE(3),Sud(4),SO(5), Ouest(6), NO(7);

	private int pos;
	private Direction(int pos) {
		this.pos=pos;
	}



	/**
	 * Prochaine direction dans le sens horaire
	 * @return
	 */
	public Direction next() {
		int size = Direction.values().length;
		Direction res = Direction.values()[(pos + 2) % size];
		if(res == Direction.NONE)
			return res.next();
		return res;
	}

	/**
	 * Direction précédente dans le sens horaire
	 * @return
	 */
	public Direction prev() {
		Direction res = Direction.values()[pos];
		if(res == Direction.NONE)
			return Direction.NO;
		return res;
	}

/**
 * Renvoie une direction aléatoire
 * @return
 */
	public static Direction getRandom() {
		Double rand = Math.random() * 8;
		if (rand < 1.) {
			return Sud;
		}
		if (rand < 2.) {
			return SO;
		}
		if (rand < 3.) {
			return Ouest;
		}
		if (rand < 4.) {
			return NO;
		}
		if (rand < 5.) {
			return Nord;
		}
		if (rand < 6.) {
			return NE;
		}
		if (rand < 7.) {
			return Est;
		} else {
			return SE;
		}
	}

	/**
	 * Vrai si la Direction dir est à l'opposée
	 * @param dir
	 * @return
	 */
	public boolean isOpposite(Direction dir) {
		switch (dir) {
		case Nord:
			return this == Sud;
		case NE:
			return this == SO;
		case Est:
			return this == Ouest;
		case SE:
			return this == NO;
		case Sud:
			return this == Nord;
		case SO:
			return this == NE;
		case Ouest:
			return this == Est;
		default:
			return this == SE;
		}
	}

	public Direction opposite(){
		Direction res = this;
		for(int i=0;i<4;i++)
			res = res.next();
		return res;
	}


/**
 * Calcule et renvoie la direction opposée à une liste de Directions
 * @param directions
 * @return
 */
	public static Direction getOpposite(List<Direction> directions){
		int somme=0;
		for(int i=0;i<directions.size();i++){
			somme=somme+directions.get(i).pos;
		}

		int moy=somme/directions.size();

		Direction dmoy;
		switch(moy%8){
		case 0:dmoy=Nord;
		break;
		case 1:dmoy=NE;
		break;
		case 2:dmoy=Est;
		break;
		case 3:dmoy=SE;
		break;
		case 4:dmoy=Sud;
		break;
		case 5:dmoy=SO;
		break;
		case 6:dmoy=Ouest;
		break;
		default:dmoy=NO;
		break;
		}

		return dmoy.opposite();
	}

/**
 * Renvoie la direction générale à partir d'un point f de coordonnées (fx,fy)
 * vers un point t de coordonnées (tx,ty), plus ou moins l'erreur
 * @param tx
 * @param ty
 * @param fx
 * @param fy
 * @param error
 * @return
 */
	public static Direction getDirectionTOFrom(double tx, double ty, double fx, double fy, double error){
		if(tx - fx > error){
			if(ty - fy > error){
				return SE;
			}
			else if (ty - fy < -error){
				return NE;
			}
			else return Est;
		}
		else if (tx - fx < -error){
			if(ty - fy > error){
				return SO;
			}
			else if (ty - fy < -error){
				return NO;
			}
			else return Ouest;
		}
		else{
			if(ty - fy > error){
				return Sud;
			}
			else if (ty - fy < -error){
				return Nord;
			}
			else return null;
		}
	}

}
