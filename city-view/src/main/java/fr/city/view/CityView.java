package fr.city.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import fr.city.view.observer.ThreeObserverCity;
import fr.life.core.Ecosystem;

@ManagedBean
@RequestScoped
public class CityView {	

	private static Ecosystem ecosystem = Ecosystem.getInstance(ThreeObserverCity.getInstance());

	private String cityName = ecosystem.getCity().getCityName();
	private int[] data = ecosystem.getCity().getTerrainLayout().getData();
	private int maxheight = ecosystem.getCity().getTerrainLayout().getMaxheight();
	private int waterheight =  ecosystem.getCity().getTerrainLayout().getWaterheight();
	
	private static int size = 500;
	private static int step = (size * 2 ) / ecosystem.getCity().getMax();
	private static int worldWidth = size / step * 2;
	private static int worldDepth = size / step * 2;
	

	public String getCityName() {
		return cityName;
	}

	public int getMaxheight() {
		return maxheight;
	}

	public void setMaxheight(int maxheight) {
		this.maxheight = maxheight;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public List<Integer> getData() {
		List<Integer> lst = new ArrayList<>(data.length);
		for(int i = 0; i < data.length; i++){
			lst.add(data[i]);
		}
		return lst;
	}

	public int getWorldWidth() {
		return worldWidth;
	}

	public void setWorldWidth(int worldWidth) {
		this.worldWidth = worldWidth;
	}

	public int getWorldDepth() {
		return worldDepth;
	}

	public void setWorldDepth(int worldDepth) {
		this.worldDepth = worldDepth;
	}

	public int getWaterheight() {
		return waterheight;
	}

	public void setWaterheight(int waterheight) {
		this.waterheight = waterheight;
	}

	public static CoordinatesView getNewXZ(int indexX, int indexY) {
		int x = -size + step / 2 + indexX * step;
		int z = -size + step / 2 + indexY * step;
		return new CoordinatesView(x, z);
	}
	
}
