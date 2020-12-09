package ihm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import concept.Exploration;
import concept.Fog;
import concept.Pair;
import concept.Terrain;
import concept.Vehicle;
import javafx.scene.ParallelCamera;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import params.Params;

public class GlobalStage extends Stage implements Observer {

	/** Controls to be used by the program. */
	private Controls c;
	/** Terrain to be handled by the program. */
	private Terrain t;

	private Exploration expl;

	/** Local stage to be used as zoom window. */
	private LocalStage localStage;

	/** Rectangles representing the vehicle. */
	private HashMap<Integer,Rectangle> vehicles;


	/** Rectangles representing the obstacles. */
	private Rectangle[] obstacles;

	/** 2D array of Rectangle representing the fog. */
	private Rectangle[][] fogRects;

	private Label winLabel = new Label();



	public GlobalStage(Stage stage) {
		setTitle(Params.globalStageTitle);
		setResizable(false);
		initModality(Modality.NONE);
		initStyle(StageStyle.UNIFIED);

		this.localStage = (LocalStage) stage;

		expl=new Exploration(this);
		t = expl.getTerrain();


		vehicles=new HashMap<>();
		for(int i=0;i<Params.VEHICULE_NBR;i++){
			Rectangle vRect = new Rectangle(Params.vehicle_width,Params.vehicle_height);
			vehicles.put(i, vRect);
		}

		obstacles=new Rectangle[t.getObstacles().size()];
		for(int i=0;i<t.getObstacles().size();i++){
			Rectangle vRect = new Rectangle(Params.obstacle_width,Params.obstacle_height);
			obstacles[i]=vRect;
		}

		fogRects = new Rectangle[(int) t.fog().getNbCellsX()][(int) t.fog().getNbCellsY()];

		Scene scene = new Scene(createContent(), Params.terrainSizeX, Params.terrainSizeY, Params.colorBackground);
		createSetOns(scene);

		c= new Controls(t);

		this.localStage.watch(t.vehicle(0));
		setScene(scene);
		sizeToScene();

		expl.ready();
		t.ready();
	}

	/** Returns a Parent containing every Node to display. */
	Parent createContent() {
		Pane root = new Pane(winLabel);
		root.setCenterShape(true);

		// Vehicle
		for(int i=0;i<Params.VEHICULE_NBR;i++){
			Rectangle vRect = vehicles.get(i);
			vRect.setFill(Params.colorVehicle[i % Params.colorVehicle.length]);
			root.getChildren().add(vRect);
		}

		// Obstacles
		for(int i=0;i<t.getObstacles().size();i++){
			Rectangle vRect = obstacles[i];
			vRect.setFill(Color.BLACK);
			vRect.setLayoutX(t.obstacle(i).getLeft());
			vRect.setLayoutY(t.obstacle(i).getTop());
			root.getChildren().add(vRect);
		}

		// Fog
		for (int x = 0; x < t.fog().getNbCellsX(); x++) {
			for (int y = 0; y < t.fog().getNbCellsY(); y++) {
				fogRects[x][y] = new Rectangle(t.fog().getCellSizeX(), t.fog().getCellSizeY());
				root.getChildren().add(fogRects[x][y]);
				fogRects[x][y].setLayoutX(x * t.fog().getCellSizeX());
				fogRects[x][y].setLayoutY(y * t.fog().getCellSizeY());
				fogRects[x][y].setFill(Params.colorFoggy);
			}
		}


		// Win Text
		winLabel.setTextAlignment(TextAlignment.CENTER);

		return root;
	}

	/** Creates the required reflexes. */
	private void createSetOns(Scene scene) {
		// Keyboard Handling
		scene.setOnKeyPressed(e -> handlePressedArrowKeys(e));
		scene.setOnKeyReleased(e -> c.handleReleasedArrowKeys(e));

		// Close Request Handling
		this.setOnCloseRequest(e -> this.handleCloseRequest(e));
		localStage.setOnCloseRequest(e -> this.handleCloseRequest(e));
	}

	/** Calls {@code Controls.handlePressedArrowKeys()}, then handles local stage toggling. */
	@SuppressWarnings("unused")
	private void handlePressedArrowKeys(KeyEvent e) {
		c.handlePressedArrowKeys(e);

		if (e.getCode() == Params.keyToggleLocalStage) {
			if (localStage.isShowing()) {
				localStage.hide();
			} else {
				localStage.show();
				this.requestFocus();
			}
		}
		else if (e.getCode() == KeyCode.NUMPAD0) {
			if(Params.VEHICULE_NBR > 0){
				this.localStage.watch(t.vehicle(0));
				c.setIdCurrentVehicle(t.vehicle(0).getID());
				updateLocalStage();
			}

		}
		else if (e.getCode() == KeyCode.NUMPAD1) {
			if(Params.VEHICULE_NBR > 1){
				this.localStage.watch(t.vehicle(1));
				c.setIdCurrentVehicle(t.vehicle(1).getID());
				updateLocalStage();
			}
		}
		else if (e.getCode() == KeyCode.NUMPAD2) {
			if(Params.VEHICULE_NBR > 2){
				this.localStage.watch(t.vehicle(2));
				c.setIdCurrentVehicle(t.vehicle(2).getID());
				updateLocalStage();
			}
		}
		else if (e.getCode() == KeyCode.NUMPAD3) {
			if(Params.VEHICULE_NBR > 3){
				this.localStage.watch(t.vehicle(3));
				c.setIdCurrentVehicle(t.vehicle(3).getID());
				updateLocalStage();
			}
		}
		else if (e.getCode() == KeyCode.NUMPAD4) {
			if(Params.VEHICULE_NBR > 4){
				this.localStage.watch(t.vehicle(4));
				updateLocalStage();
				c.setIdCurrentVehicle(t.vehicle(4).getID());
			}
		}
		else if (e.getCode() == KeyCode.NUMPAD5) {
			if(Params.VEHICULE_NBR > 5){
				this.localStage.watch(t.vehicle(5));
				c.setIdCurrentVehicle(t.vehicle(5).getID());
				updateLocalStage();
			}
		}
	}

	/** Closes all windows when any is closed. */
	public void handleCloseRequest(WindowEvent e) {
		this.close();
		localStage.close();
	}


	/** If local stage is showing, updates its display. */
	private void updateLocalStage() {
		if (localStage.isShowing()) {
			localStage.update(snapshotVehicle(localStage.watchedVehicle()));
		}
	}

	/** Takes a snapshot of the area surrounding the given vehicle. */
	private Image snapshotVehicle(Vehicle v) {
		SnapshotParameters params = new SnapshotParameters();
		ParallelCamera camera = new ParallelCamera();

		// Calculate wanted camera position
		double newX = v.getCenterX() - (v.getViewRadius() );
		double newY = v.getCenterY() - (v.getViewRadius() );

		// Check X-axis camera collision
		double viewRadius = v.getViewRadius();

		if (newX < 0) {
			newX = 0;
		} else if (newX > this.getWidth() - viewRadius) {
			newX = this.getWidth() - viewRadius;
		}

		// Check Y-axis camera collision
		if (newY < 0) {
			newY = 0;
		} else if (newY > this.getHeight() - viewRadius) {
			newY = this.getHeight() - viewRadius;
		}

		// Move camera
		camera.setLayoutX(newX);
		camera.setLayoutY(newY);

		params.setFill(Color.ALICEBLUE);
		params.setCamera(camera);

		return this.getScene().getRoot().snapshot(params, null);
	}

	/** Requests discovery and checks if the game has been won. */
	private void handleDiscovery(int id) {
		t.fog().discover(t.vehicle(id).getCenterX(), t.vehicle(id).getCenterY());

		if (t.fog().hasWon()) {
			String victory = "Congratulations!\nYour efficiency was "+ (double) Math.round(t.getEfficiency() * 1000) / 1000;
			winLabel.setText(victory);

			winLabel.setLayoutX(t.vehicle(id).getCenterX());
			winLabel.setLayoutY(t.vehicle(id).getCenterY());
			System.out.println(victory);
			t.end();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		if (o instanceof Fog) { // update fog
			Object[] param=(Object[]) arg;
			HashSet<Pair> toDiscover = (HashSet<Pair>) param[0];
			HashSet<Pair> inView = (HashSet<Pair>) param[1];
			HashSet<Pair> notInViewAnymore= (HashSet<Pair>) param[2];

			if(fogRects == null){
				fogRects = new Rectangle[(int) t.fog().getNbCellsX()][(int) t.fog().getNbCellsY()];
			}

			for (Pair c : toDiscover)
				this.fogRects[c.x][c.y].setFill(Params.colorDiscovered);


			for(Pair c:notInViewAnymore){
				this.fogRects[c.x][c.y].setOpacity(1);
			}

			for (Pair c : inView)
				this.fogRects[c.x][c.y].setOpacity(0.5);

			notInViewAnymore.removeAll(notInViewAnymore);
			inView.removeAll(inView);
			toDiscover.removeAll(toDiscover);

		} else if (o instanceof Terrain) { // update vehicle
			int id=(int)arg;
			Rectangle vRect = vehicles.get(id);

			if(vRect == null){
				vRect = new Rectangle(Params.vehicle_width,Params.vehicle_height);
			}
			vRect.setLayoutX(t.vehicle(id).getLeft());
			vRect.setLayoutY(t.vehicle(id).getTop());
			handleDiscovery(id);
		}

		updateLocalStage();
	}
}
