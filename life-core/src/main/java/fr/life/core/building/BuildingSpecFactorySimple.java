package fr.life.core.building;

public class BuildingSpecFactorySimple implements BuildingSpecFactory {

	public BuildingSpec createBuildingSpec(BuildingSpecType type, int x, int z,
			int height) {

		BuildingSpec buildingSpec = null;
		switch (type) {
		case ACCOMODATION:
			buildingSpec = new Accomodation();
			break;
		case INDUSTRIAL:
			buildingSpec = new Industrial();
			break;
		case COMMERCE:
			buildingSpec = new Commerce();
			break;
		}
		return buildingSpec;
	}

}
