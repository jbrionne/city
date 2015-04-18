package fr.life.core;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import fr.city.core.Building;
import fr.city.core.CityColor;
import fr.city.core.Road;
import fr.network.transport.network.Address;
import fr.network.transport.physique.Container;

public class CityBuildTest extends CityViewTest {



	@Test
	public void firstBuilding() {
		//**S1) Placer un building A rouge de hauteur 10 à l'emplacement 100, 50		
		Building b = life.getCityBuild().createBuildingA();
		assertEquals(b.getX(), 100);
		assertEquals(b.getZ(), 50);
		assertEquals(b.getHeight(), 70);
		assertEquals(b.getColor(), CityColor.RED);		
		
		//trust the framework ?		
		Building bf = CityRequest.getInstance().findBuilding(100, 50);
		assertEquals(bf.getX(), 100);
		assertEquals(bf.getZ(), 50);
		assertEquals(bf.getHeight(), 70);		
		assertEquals(b.getColor(), CityColor.RED);
	}
	
	@Test
	public void secondBuilding() {
		//**S2) Placer un building B bleu de hauteur 10 à l'emplacement 100, 10		
		Building b = life.getCityBuild().createBuildingB();
		assertEquals(b.getX(), 100);
		assertEquals(b.getZ(), 10);
		assertEquals(b.getHeight(), 10);
		assertEquals(b.getColor(), CityColor.BLUE);		
		
		//trust the framework ?		
		Building bf = CityRequest.getInstance().findBuilding(100, 10);
		assertEquals(bf.getX(), 100);
		assertEquals(bf.getZ(), 10);
		assertEquals(bf.getHeight(), 10);		
		assertEquals(b.getColor(), CityColor.BLUE);
	}
	
	@Test
	public void createRoadAB() {
		//**S3) Placer une route reliant les deux buildings	
		Road r = life.getCityBuild().createRoadAB();
		assertEquals(r.getXa(), 99);
		assertEquals(r.getXb(), 99);
		assertEquals(r.getZa(), 0);
		assertEquals(r.getZb(), 100);		
		
	
//		//trust the framework ?			
		Road rf = CityRequest.getInstance().findRoad(99, 0, 99, 100);
		assertEquals(rf.getXa(), 99);
		assertEquals(rf.getXb(), 99);
		assertEquals(rf.getZa(), 0);
		assertEquals(rf.getZb(), 100);	
	}
	
	
	@Test
	public void moveTransport() {
		//**S4) Faire apparaitre un transport se déplacant sur la route, de A en B
		Road r = life.getCityBuild().createRoadAB();
		MockTransportMovePop tm = new MockTransportMovePop();
		Building t = life.getCityBuild().launchTransportAB(r.getName(), tm);		
		Building t2 = CityRequest.getInstance().findTransportByName(t.getName());
		
		assertEquals(t2.getX(), 99);
		assertEquals(t2.getZ(), 50);
		assertEquals(t2.getName(), t.getName());		
	}
	
	
	@Test
	public void infiniteTransport()  {
		//**S5) Le transport doit faire l'aller-retour entre A et B (sans fin, sans pause)		
		Address oriAB = new Address();
		oriAB.setX(0);
		oriAB.setZ(0);
		oriAB.setRoadName("test");	
		
		Address oriBA = new Address();
		oriBA.setX(0);
		oriBA.setZ(100);
		oriBA.setRoadName("test");	
				
		//????	
		//tAB.launchBA(container, tm);		
	}
	
	@Test
	public void exhangeTransport()  {	
		//	**S6) Le transport en A récupère des produits (simuler par la baisse de la hauteur du building A)
		//	 pour les remettre au building B (simuler  par une augmentation de la hauteur du building B).
		//	Le transport ensuite retourne vers le building A rechercher des produits et la boucle recommence.
		//	Le building est contruit lorsque sa hauteur atteint 50 de haut ; Le transport disparait alors
		
		
		
	
	}
	
	

	
	

}
