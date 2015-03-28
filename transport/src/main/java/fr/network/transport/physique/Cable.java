package fr.network.transport.physique;

import java.util.HashSet;
import java.util.Set;

import fr.network.transport.api.Coord2D;

public class Cable {

	private Coord2D ori;
	private Coord2D dest;
	private boolean twoWays;
	private Set<Cable> exitOri = new HashSet<>();
	private Set<Cable> exitDest = new HashSet<>();

	public Coord2D getOri() {
		return ori;
	}

	public Cable() {
	}

	public Cable(Coord2D ori, Coord2D dest, boolean twoWays) {
		super();
		this.ori = ori;
		this.dest = dest;
		this.twoWays = twoWays;
	}

	public void setOri(Coord2D ori) {
		this.ori = ori;
	}

	public Coord2D getDest() {
		return dest;
	}

	public void setDest(Coord2D dest) {
		this.dest = dest;
	}

	public boolean isTwoWays() {
		return twoWays;
	}

	public void setTwoWays(boolean twoWays) {
		this.twoWays = twoWays;
	}

	public Set<Cable> getExitOri() {
		return exitOri;
	}

	public Set<Cable> getExitDest() {
		return exitDest;
	}

}
