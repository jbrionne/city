package fr.network.transport.physique;

import java.util.stream.Collector.Characteristics;

import fr.network.transport.liaison.Product;

class ProductElm extends Product {
	
	private Characteristics characteristics;

	public Characteristics getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(Characteristics characteristics) {
		this.characteristics = characteristics;
	}

}
