package fr.life.core.building;

public interface BuildingSpecFactory {

	BuildingSpec createBuildingSpec(BuildingSpecType type, int x, int z,
			int height);
}
