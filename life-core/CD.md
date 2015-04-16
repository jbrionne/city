Coding dojo : randori : city
===================================

Fourni
------

* Classe Ecosystem + les modules de city (builder, city-view, city-core, city-parent, graph-core, life-core, map-core, sunlight-core, transport)
* jetty:run

Steps proposés
--------------

* Initialisation

**S1) Placer un building A rouge de hauteur 70 à l'emplacement 100, 50

**S2) Placer un building B bleu de hauteur 10 à l'emplacement 100, 10

**S3) Placer une route reliant les deux buildings

**S4) Faire apparaitre un transport se déplacant sur la route, de A en B

**S5) Le transport doit faire l'aller-retour entre A et B (sans fin, sans pause)

**S6) Le transport en A récupère des produits (simuler par la baisse de la hauteur du building A)
 pour les remettre au building B (simuler  par une augmentation de la hauteur du building B).
Le transport ensuite retourne vers le building A rechercher des produits et la boucle recommence.
Le building est contruit lorsque sa hauteur atteint 50 de haut ; Le transport disparait alors

**S7) Placer un building C vert de hauteur 10 à l'emplacement 100, 30. 
Créer un deuxième transport entre A en C à l'instar du transport entre A et B.

**S8) Si un transport revient prendre des matériaux, mais que la pile A est à 0, le camion reste immobile en attente de matériaux

**S9) La pile A est rechargée de 10 toutes les 35 s (ce qui débloque les camions en attente)


* Conception

**S1 : Créer de quoi fabriquer des routes droites, des carrefours, des blocs (rectangles).
Pour les routes droites : un point d'entrée + un vecteur (horizontal ou vertical (0,Z) ou (X,0))
Pour les carrefours : un point d'entrée + un vecteur (horizontal ou vertical (0,Z) ou (X,0), creation de 3 branches)
Pour les blocs : un point d'entrée + un vecteur (avec X et Z différent de 0). Si on a un point d'entrée de 0,0 alors on construira (0,0),(X,0) | (0,0),(0,Z) | (0,Z),(X,Z) | (X,0),(X,Z)
On doit avoir en retour les nouveaux points d'entrée.


* REST communication avec une autre ville




Extensions possibles
--------------------

