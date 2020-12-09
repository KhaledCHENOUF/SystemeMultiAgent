package ihm;

import concept.Direction;
import concept.Terrain;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controls {


	/** Current direction input. */
	private Direction dir;

	private Terrain t;

	/** Whether {@code keyUp} is held. */
	private boolean holdsUp;
	/** Whether {@code keyDown} is held. */
	private boolean holdsDown;
	/** Whether {@code keyLeft} is held. */
	private boolean holdsLeft;
	/** Whether {@code keyRight} is held. */
	private boolean holdsRight;

	/** Key to be interpreted as UP. */
	private KeyCode keyUp;
	/** Key to be interpreted as DOWN. */
	private KeyCode keyDown;
	/** Key to be interpreted as LEFT. */
	private KeyCode keyLeft;
	/** Key to be interpreted as RIGHT. */
	private KeyCode keyRight;

	private int idCurrentVehicle=0;

	public Controls(Terrain t) {
		dir = Direction.NONE;
		this.t=t;

		this.holdsUp = false;
		this.holdsDown = false;
		this.holdsLeft = false;
		this.holdsRight = false;

		this.keyUp = KeyCode.UP;
		this.keyDown = KeyCode.DOWN;
		this.keyLeft = KeyCode.LEFT;
		this.keyRight = KeyCode.RIGHT;
	}

	public Controls(KeyCode up, KeyCode down, KeyCode left, KeyCode right) {
		this.holdsUp = false;
		this.holdsDown = false;
		this.holdsLeft = false;
		this.holdsRight = false;

		this.keyUp = up;
		this.keyDown = down;
		this.keyLeft = left;
		this.keyRight = right;
	}

	/** Updates hold variables based on a given pressed key, and calls {@code updateDirections()}. */
	public void handlePressedArrowKeys(KeyEvent e) {
		KeyCode k = e.getCode();

		if (k == keyUp) holdsUp = true;
		if (k == keyDown) holdsDown = true;
		if (k == keyLeft) holdsLeft = true;
		if (k == keyRight) holdsRight = true;

		updateDirections();
	}


	public void move(Direction d){
		dir=d;
	}

	public void stop(){
		dir=Direction.NONE;
	}

	public int getIdCurrentVehicle() {
		return idCurrentVehicle;
	}



	public void setIdCurrentVehicle(int idCurrentVehicle) {
		this.idCurrentVehicle = idCurrentVehicle;
	}

	/** Updates hold variables based on a given released key, and calls {@code updateDirections()}. */
	public void handleReleasedArrowKeys(KeyEvent e) {
		KeyCode k = e.getCode();

		if (k == keyUp) holdsUp = false;
		if (k == keyDown) holdsDown = false;
		if (k == keyLeft) holdsLeft = false;
		if (k == keyRight) holdsRight = false;

	}

	/** Updates direction inputs based on current hold variables. */
	private void updateDirections() {
		if (holdsUp && !holdsDown) {
			if(holdsLeft && !holdsRight){
				this.dir=Direction.NO;
			}
			else if(holdsRight && !holdsLeft){
				this.dir=Direction.NE;
			}
			else this.dir = Direction.Nord;
		} else if (holdsDown && !holdsUp) {
			if(holdsLeft && !holdsRight){
				this.dir=Direction.SO;
			}
			else if(holdsRight && !holdsLeft){
				this.dir=Direction.SE;
			}
			else this.dir = Direction.Sud;
		} else if (holdsLeft && !holdsRight) {
			this.dir = Direction.Ouest;
		} else if (holdsRight && !holdsLeft) {
			this.dir = Direction.Est;
		} else {
			this.dir = Direction.NONE;
		}

		t.ihmControls(this, idCurrentVehicle);
	}

	public Direction getDirection() { return this.dir; }

	/** Returns whether any key is currently held. */
	public boolean isMoving() {
		return this.holdsUp || this.holdsDown || this.holdsLeft || this.holdsRight;
	}
}
