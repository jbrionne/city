Coding dojo : randori : city
===================================

Fourni
------

* Classe Ecosystem + les modules de city (builder, city-view, city-core, city-parent, graph-core, life-core, map-core, sunlight-core, transport)


Steps proposés
--------------

* Step 0 : Créer une source en (100, 0) à l'aide de la méthode createSource(int x, int z). Créer une route de (100,0) à (100,100) à l'aide de la méthode city.createRoad(int x, int z, int xD, int zD, Color color); (Une route doit être connectée à une source) 
* Step 1 : Créer un bâtiment aux coordonnées (99, 90) et de hauteur 0 à l'aide de la méthode city.updateOrCreateBuilding(int x, int z, int height, String color)
* Step 2 : Créer un transport en (100,0) à l'aide de la méthode city.createTransport(Coord2D coord, String color)
* Step 3 : Déplacer le transport à l'aide de la méthode void send(TransportMove transportMove, String name, Address origin, Address destination, Product p). Utiliser cette méthode pour faire bouger le transport de (100, 0) à (100, 90)
* Step 4 : Implémenter la méthode TransportMovePop.finish pour faire evoluer la hauteur du batiment créé en step 1 selon PopProduct.getNbPerson(), supprimer ensuite le transport. Vous pouvez utiliser la méthode city.findBuilding pour retrouver un batiment selon ses coordonnées.
* Step 5 : 


Extensions possibles
--------------------

