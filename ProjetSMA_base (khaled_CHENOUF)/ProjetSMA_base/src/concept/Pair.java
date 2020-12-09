package concept;

/** Pair of integers. */
public class Pair {

	public final int x;
	public final int y;
	
	Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object that) {		
		boolean equals = (this == that);
		
		if (!equals)
			equals = (this.x == ((Pair) that).x && this.y == ((Pair) that).y);
		
		return equals;
	}
}
