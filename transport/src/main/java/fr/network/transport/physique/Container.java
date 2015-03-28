package fr.network.transport.physique;

import java.util.ArrayList;
import java.util.List;

import fr.network.transport.api.Coord2D;
import fr.network.transport.liaison.Product;

public class Container extends Product {

	private List<Product> productsContainer = new ArrayList<Product>();
	private Capacity capacity;
	private Coord2D coordinates;
	private String name;

	public Capacity getCapacity() {
		return capacity;
	}

	public void addProductsContainer(Product p) {
		// check capacity
		productsContainer.add(p);
	}

	public List<Product> getProductsContainer() {
		return new ArrayList<Product>(productsContainer);
	}

	public Coord2D getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coord2D coordinates) {
		this.coordinates = coordinates;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
