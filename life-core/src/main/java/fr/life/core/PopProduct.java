package fr.life.core;

import fr.network.transport.liaison.Product;

public class PopProduct implements Product {
	
	private int volume;
	private int nbPerson;
	
	public PopProduct(int volume, int nbPerson) {
		super();
		this.volume = volume;
		this.nbPerson = nbPerson;
	}

	@Override
	public int getVolume() {
		return volume;
	}

	public int getNbPerson() {
		return nbPerson;
	}

	public void setNbPerson(int nbPerson) {
		this.nbPerson = nbPerson;
	}
	
	
}
