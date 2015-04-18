package fr.network.transport.physique;

import fr.network.transport.api.TransportMove;
import fr.network.transport.liaison.Product;
import fr.network.transport.network.Address;

public class Container<T extends Product> implements Product {

	private T product;
	private TransportMove transportMove;
	private Capacity capacity;
	private Address coordinates;
	private Address destination;
	private Address finalDestination;
	private String name;
	private int volume = 30;

	public Capacity getCapacity() {
		return capacity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Address coordinates) {
		this.coordinates = coordinates;
	}

	public Address getDestination() {
		return destination;
	}

	public void setDestination(Address destination) {
		this.destination = destination;
	}

	public Address getFinalDestination() {
		return finalDestination;
	}

	public void setFinalDestination(Address finalDestination) {
		this.finalDestination = finalDestination;
	}

	public T getProduct() {
		return product;
	}

	public void setProduct(T product) {
		this.product = product;
	}

	public void setCapacity(Capacity capacity) {
		this.capacity = capacity;
	}

	@Override
	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public TransportMove getTransportMove() {
		return transportMove;
	}

	public void setTransportMove(TransportMove transportMove) {
		this.transportMove = transportMove;
	}

	@Override
	public String toString() {
		return "Container [product=" + product + ", transportMove="
				+ transportMove + ", capacity=" + capacity + ", coordinates="
				+ coordinates + ", destination=" + destination
				+ ", finalDestination=" + finalDestination + ", name=" + name
				+ ", volume=" + volume + "]";
	}


}
