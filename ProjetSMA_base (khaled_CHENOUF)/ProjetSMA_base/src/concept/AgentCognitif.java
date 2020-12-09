package concept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sma.Agent;

public class AgentCognitif extends Agent {
	HashMap<Direction, Zone> Map = new HashMap<Direction, Zone>();

	public AgentCognitif(VehicleInterface vehicle, int id) {
		super(vehicle, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		int i = 0;
		//initialisation des directions
		List<Direction> List_dir1 = new ArrayList<Direction>();
		List_dir1.add(Direction.Ouest);
		List<Direction> List_dir2 = new ArrayList<Direction>();
		List_dir2.add(Direction.Nord);
		List<Direction> List_dir3 = new ArrayList<Direction>();
		List_dir3.add(Direction.Est);
		List<Direction> List_dir4 = new ArrayList<Direction>();
		List_dir4.add(Direction.Sud);

		while (!Thread.currentThread().isInterrupted()) {
			Direction directionCourante = null;
			percevoirZones();
			if (getAgentId() % 2 != 0) { // -- Agents id impair
				if (((getAgentId() + 1) / 2) % 2 != 0) { // -- 1 er groupe
					directionCourante = List_dir1.get(i);
					if (undiscovered().contains(List_dir1.get(i)))
						move(List_dir1.get(i));
					else {
						i = i + 1;
						List_dir1.add(directionCourante.next());
					}
				} else { // -- 2 eme groupe
					directionCourante = List_dir2.get(i);
					if (undiscovered().contains(List_dir2.get(i)))
						move(List_dir2.get(i));
					else {
						i = i + 1;
						List_dir2.add(directionCourante.next());
					}
				}
			} else { // -- Agents id pair
				if (((getAgentId()) / 2) % 2 == 0) { // -- 1 er groupe
					directionCourante = List_dir3.get(i);
					if (undiscovered().contains(List_dir3.get(i)))
						move(List_dir3.get(i));
					else {
						i = i + 1;
						List_dir3.add(directionCourante.next());
					}
				} else { // -- 2 eme groupe
					directionCourante = List_dir4.get(i);
					if (undiscovered().contains(List_dir4.get(i)))
						move(List_dir4.get(i));
					else {
						i = i + 1;
						List_dir4.add(directionCourante.next());
					}
				}
			}
		   if(undiscovered().isEmpty())
			   move(Direction.getRandom());

			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}