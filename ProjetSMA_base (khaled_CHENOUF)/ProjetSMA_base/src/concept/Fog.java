package concept;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import params.Params;

public class Fog extends Observable {

	/** Number of cells on the X-axis. */
	private double nbCellsX;
	/** Number of cells on the Y-axis. */
	private double nbCellsY;

	/** Width of a single cell. */
	private double cellSizeX;
	/** Height of a single cell. */
	private double cellSizeY;

	/** Number of discovered cells. */
	private int nbDiscovered;

	/**
	 * 2D array of whether a cell is foggy or discovered. 1 for foggy, 0 for
	 * discovered.
	 */
	private int[][] isFoggy;

	/**
	 * Indexes of the cells to be displayed as discovered by the graphics
	 * engine.
	 */
	private HashSet<Pair> toDiscover;

	/**
	 * Indexes of the cells to be displayed as discovered by the graphics
	 * engine.
	 */
	private HashSet<Pair> inView;


	private HashSet<Pair> notInViewAnymore;

	public Fog(Observer observer, double sizeX, double sizeY, double nbCellsX, double nbCellsY) {
		addObserver(observer);

		sizeX = Math.ceil(sizeX);
		sizeY = Math.ceil(sizeY);
		nbCellsX = Math.ceil(nbCellsX);
		nbCellsY = Math.ceil(nbCellsY);

		this.nbCellsX = nbCellsX;
		this.nbCellsY = nbCellsY;

		this.cellSizeX = sizeX / nbCellsX;
		this.cellSizeY = sizeY / nbCellsY;

		this.nbDiscovered = 0;

		this.initArrays();
	}

	public double getNbCellsX() {
		return this.nbCellsX;
	}

	public double getNbCellsY() {
		return this.nbCellsY;
	}

	public double getCellSizeX() {
		return this.cellSizeX;
	}

	public double getCellSizeY() {
		return this.cellSizeY;
	}

	/**
	 * for (int x = 0; x < nbCellsX; x++) {
			for (int y = 0; y < nbCellsY; y++) {
				setFoggy(x, y, true);
			}
		}
	 * Initializes {@code int[][] isFoggy} and {@code HashSet<Pair> toDiscover}.
	 */
	private void initArrays() {
		this.isFoggy = new int[(int) nbCellsX][(int) nbCellsY];
		this.toDiscover = new HashSet<Pair>();
		inView = new HashSet<>();
		notInViewAnymore=new HashSet<>();

		for (int x = 0; x < nbCellsX; x++) {
			for (int y = 0; y < nbCellsY; y++) {
				if(x == 0 || x == nbCellsX - 1 || y == 0 || y == nbCellsY - 1)
					setFoggy(x, y, false);
				else
					setFoggy(x, y, true);
			}
		}
	}

	private boolean isFoggy(int x, int y) {
		return this.isFoggy[x][y] == 1;
	}

	private void setFoggy(int x, int y, boolean isFoggy) {
		if (isFoggy) { // cover
			this.isFoggy[x][y] = 1;
			this.toDiscover.remove(new Pair(x, y));
		} else { // discover
			this.isFoggy[x][y] = 0;
			this.toDiscover.add(new Pair(x, y));
			this.nbDiscovered++;
		}
	}

	/**
	 * Iterates through cells to find which ones must be discovered.
	 *
	 * @param disX
	 *            Center of the discovery radius on the X-axis
	 * @param disY
	 *            Center of the discovery radius on the Y-axis
	 * @param radius
	 *            Radius of the area to discover
	 * @return True if any cell has been discovered, false otherwise
	 */
	public void discover(double disX, double disY) {
		boolean discovered = false;
		double Xmin=Math.max(0,((disX-1.5*Params.vehicleViewRadius)-cellSizeX / 2.0)/cellSizeX);
		double Ymin=Math.max(0,((disY-1.5*Params.vehicleViewRadius)-cellSizeY / 2.0)/cellSizeY);

		double Xmax=Math.min(Params.nbCellsX,((disX+1.5*Params.vehicleViewRadius)-cellSizeX / 2.0)/cellSizeX);
		double Ymax=Math.min(Params.nbCellsY,((disY+1.5*Params.vehicleViewRadius)-cellSizeY / 2.0)/cellSizeY);

		for (int x = (int) Xmin; x < Xmax; x++) {
			for (int y = (int) Ymin; y < Ymax; y++) {
				if (isFoggy(x, y)) {
					double coord=Math.pow((x * cellSizeX + cellSizeX / 2.0) - (disX), 2)+ Math.pow((y * cellSizeY + cellSizeY / 2.0) - (disY), 2);
					if (coord <= Math.pow(Params.vehicleDiscoveryRadius, 2)) {
						setFoggy(x, y, false);
						discovered = true;
					} else if (coord <= Math.pow(Params.vehicleViewRadius, 2)) {
						this.inView.add(new Pair(x, y));
						discovered = true;
					}
					else{
						notInViewAnymore.add(new Pair(x, y));
					}
				}
			}
		}

		if (discovered)
			requestChange();

	}

	public HashMap<Direction,Double[]> evaluate(double disX, double disY) {
		HashMap<Direction,Double[]> stats=new HashMap<>();

		double Xmin=Math.max(0,((disX-Params.vehicleViewRadius)-cellSizeX / 2.0)/cellSizeX);
		double Ymin=Math.max(0,((disY-Params.vehicleViewRadius)-cellSizeY / 2.0)/cellSizeY);

		double Xmax=Math.min(Params.nbCellsX,((disX+Params.vehicleViewRadius)-cellSizeX / 2.0)/cellSizeX);
		double Ymax=Math.min(Params.nbCellsY,((disY+Params.vehicleViewRadius)-cellSizeY / 2.0)/cellSizeY);

		/*double Xmin=Math.max(1,((disX-Params.vehicleViewRadius)-cellSizeX / 2.0)/cellSizeX);
		double Ymin=Math.max(1,((disY-Params.vehicleViewRadius)-cellSizeY / 2.0)/cellSizeY);

		double Xmax=Math.min(Params.nbCellsX-1,((disX+Params.vehicleViewRadius)-cellSizeX / 2.0)/cellSizeX);
		double Ymax=Math.min(Params.nbCellsY-1,((disY+Params.vehicleViewRadius)-cellSizeY / 2.0)/cellSizeY);*/


		for (int x = (int) Xmin; x < Xmax; x++) {
			for (int y = (int) Ymin; y < Ymax; y++) {
				double coord=Math.pow((x * cellSizeX + cellSizeX / 2.0) - (disX), 2)+ Math.pow((y * cellSizeY + cellSizeY / 2.0) - (disY), 2);
				// coord dans le champ de vision
				if (coord < Math.pow(Params.vehicleViewRadius, 2)) {

					// mais pas dans le champ de découverte
					if (coord >= Math.pow(Params.vehicleDiscoveryRadius, 2)) {
						Direction d=Direction.getDirectionTOFrom((x * cellSizeX + cellSizeX / 2.0),(y * cellSizeY + cellSizeY / 2.0), disX, disY,Params.vehicleDiscoveryRadius/2);
						Double[] statD=stats.get(d);

						if(statD == null){
							statD=new Double[]{0.,0.};
						}

						if(isFoggy(x, y)){
							statD[0]=statD[0]+1;
						}
						statD[1]=statD[1]+1;

						stats.put(d, statD);
					}
				}
			}
		}

		for(Direction d:stats.keySet()){
			Double[] statD=stats.get(d);
			statD[0]=statD[0]/statD[1]*100;
			stats.put(d, statD);
		}


		return stats;

	}

	/** Returns whether all cells have been discovered or not. */
	public boolean hasWon() {
		return nbDiscovered == nbCellsX * nbCellsY;
	}

	/** Notifies observers of new discovered cells. */
	public void requestChange() {
		setChanged();
		notifyObservers(new Object[] { toDiscover, inView,notInViewAnymore });
	}
}
