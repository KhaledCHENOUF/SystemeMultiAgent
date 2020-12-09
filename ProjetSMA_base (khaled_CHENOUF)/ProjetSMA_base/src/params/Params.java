package params;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public final class Params {

	// Global
	public static final int VEHICULE_NBR = 4;
	public static final int OBSTACLE_NBR = 0;
	public static final boolean START_AT_CENTER=true;

	// Terrain
	public static final double terrainSizeX = 800;
	public static final double terrainSizeY = 600;
	private static final int fuzzy=8;


	// Stages
	public static final String globalStageTitle = "Carte";
	public static final String localStageTitle = "Zoom";

	// Fog
	public static final double nbCellsX = terrainSizeX/fuzzy;
	public static final double nbCellsY = terrainSizeY/fuzzy;

	// Obstacle
	/** Physical and visual width. */
	public static final double obstacle_width=10;
	/** Physical and visual height. */
	public static final double obstacle_height=10;



	// Vehicle
	public static final double vehicleDiscoveryRadius = 40;
	public static final double vehicleViewRadius = 60;
	/** Maximum speed the vehicle can reach. */
	public static final double maxSpeed=8;
	/** Minimum speed the vehicle can reach. */
	public static final double minSpeed=8;
	/** How much speed the vehicle can gain per frame. */
	public static final double accel=0.25;
	/** How much speed the vehicle can lose per frame. */
	public static final double decel=0.45;

	/** Physical and visual width. */
	public static final double vehicle_width=15;
	/** Physical and visual height. */
	public static final double vehicle_height=15;

	// Colors
	public static final Color colorVehicle[] = new Color[]{Color.CORNFLOWERBLUE,Color.BLUE,Color.CHARTREUSE,Color.DARKRED,
			Color.FUCHSIA,Color.OLIVE,Color.LIGHTSALMON,Color.ANTIQUEWHITE};
	public static final Color colorBackground = Color.ALICEBLUE;
	public static final Color colorFoggy = Color.DARKSLATEBLUE;
	public static final Color colorDiscovered = Color.TRANSPARENT;
	public static final Color randomColor() { return Color.color(Math.random(), Math.random(), Math.random()); }

	// Keyboard Inputs
	public static final KeyCode keyToggleLocalStage = KeyCode.Z;

}
