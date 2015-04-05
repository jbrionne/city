package fr.graph.api;

import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.graph.core.Graph;
import fr.graph.core.GraphUtils;
import fr.graph.core.RelTypes;

public class GraphEntry {

	private Graph graph;

	private static final Logger LOG = LoggerFactory.getLogger(GraphEntry.class);

	public GraphEntry(String dbPath, boolean test, String... index) {
		// TODO make a configuration file !
		if (test) {
			this.graph = new Graph(
					new TestGraphDatabaseFactory().newImpermanentDatabase(),
					dbPath, index);
		} else {
			this.graph = new Graph(
					new GraphDatabaseFactory().newEmbeddedDatabase(dbPath),
					dbPath, index);
		}

	}

	public void createIndex(String indexName) {
		graph.createIndex(indexName);
	}

	public boolean checkIfRoadExists(String indexName, int xA,
			int yA, int xB, int yB) {
		Node nA = graph.findEventXYNode(indexName, xA, yA);
		Node nB = graph.findEventXYNode(indexName, xB, yB);
		LOG.info(nA + " " + xA + "," + yA);
		LOG.info(nB + " " + xB + "," + yB);
		if (nA != null && nB != null) {
			if (graph.hasRelationshipWithNode(nA, nB)) {
				return true;
			}			
		}
		return false;
	}

	public InfoRelationShip findRoad(String indexName, String name) {
		Relationship r = graph.findRelationshipByName(RelTypes.EVENT, indexName, name);
		if(r != null) {
			return GraphUtils.getRelationShipInfoByName(name);
		} else {
			return null;
		}
	}

	public void createRoad(String indexName, int xA, int yA, int xB, int yB) {

		LOG.info("createRoad " + xA + "," + yA + "," + xB + "," + yB);
		if (xA != xB && yA != yB) {
			throw new IllegalArgumentException(
					"you must have xA == xB or yA == yB");
		}

		Node nA = graph.findEventXYNode(indexName, xA, yA);
		Node nB = graph.findEventXYNode(indexName, xB, yB);
		LOG.info(nA + " " + xA + "," + yA);
		LOG.info(nB + " " + xB + "," + yB);
		if (nA != null && nB != null) {
			if (graph.hasRelationshipWithNode(nA, nB)) {
				throw new IllegalArgumentException("the road already exist "
						+ xA + "," + yA + "," + xB + "," + yB);
			}
		} else {
			if (nA == null) {
				nA = graph.createNode(indexName,
						getNodeNameRoad(indexName, xA, yA), xA, yA, null);
			}
			if (nB == null) {
				nB = graph.createNode(indexName,
						getNodeNameRoad(indexName, xB, yB), xB, yB, null);
			}
		}

		Node nmin = nA;
		Node nmax = nB;

		int minX = xA;
		int maxX = xA;

		int minY = yA;
		int maxY = yA;

		if (xA == xB) {
			minY = yA;
			maxY = yB;
			if (yA > yB) {
				minY = yB;
				maxY = yA;

				nmin = nB;
				nmax = nA;
			}

			findNodeAndMakeNetworkX(indexName, nmin, nmax, minX, minY, maxY);
		} else if (yA == yB) {
			minX = xA;
			maxX = xB;
			if (xA > xB) {
				minX = xB;
				maxX = xA;

				nmin = nB;
				nmax = nA;
			}
			findNodeAndMakeNetworkY(indexName, nmin, nmax, minX, maxX, minY);
		} else {
			throw new IllegalArgumentException(
					"you must have xA == xB or yA == yB");
		}
	}

	private void findNodeAndMakeNetworkY(String typeName, Node nmin, Node nmax,
			int minX, int maxX, int minY) {
		Node nX = nmin;
		Node prev = nmin;
		for (int i = minX + 1; i < maxX; i++) {
			Node r = graph.findEventXYNode(typeName, i, minY);
			if (r != null) {
				nX = r;
				createRelation(prev, nX);
				prev = nX;
			}
		}
		createRelation(nX, nmax);
	}

	private void findNodeAndMakeNetworkX(String typeName, Node nmin, Node nmax,
			int minX, int minY, int maxY) {
		Node nX = nmin;
		Node prev = nmin;
		for (int i = minY + 1; i < maxY; i++) {
			Node r = graph.findEventXYNode(typeName, minX, i);
			if (r != null) {
				nX = r;
				createRelation(prev, nX);
				prev = nX;
			}
		}
		createRelation(nX, nmax);
	}

	private void createRelation(Node nmin, Node nX) {
		graph.createRelation(nmin, nX);		
	}

	public String getNodeName(String indexName, String id) {
		return GraphUtils.getNodeName(indexName, id);
	}

	private String getNodeNameRoad(String indexName, int xA, int yA) {
		return GraphUtils.getNodeNameRoad(indexName, xA, yA);
	}

	public String getRelationShipName(int x, int y, int xD, int yD) {
		return GraphUtils.getRelationShipName(x, y, xD, yD);
	}
	

	public void create(String indexName, String name, int x, int y, String json) {
		graph.createNode(indexName, name, x, y, json);
	}

	public void createRelationship(String indexName, String nameA,
			String nameB, int length) {
		graph.createRelationship(indexName, nameA, nameB, length);
	}

	public void remove(String indexName, String name) {
		graph.remove(indexName, name);
	}

	public List<Object> findAll(String indexName, String property) {
		return graph.findAll(indexName, property);
	}

	public List<Node> findAll(String indexName) {
		return graph.findAll(indexName);
	}

	public List<Relationship> findAllRelation(RelationshipType type) {
		return graph.findAllRelationship(type);
	}

	public Node getStartNode(Relationship node) {
		return graph.getStartNode(node);
	}

	public Node getEndNode(Relationship node) {
		return graph.getEndNode(node);
	}

	public Object getProperty(Node node, String property) {
		return graph.getProperty(node, property);
	}

	public Object find(String indexName, String name, String property) {
		return graph.find(indexName, name, property);
	}

	public Object findEventXYNodeProperty(String typeName, int x, int y,
			String property) {
		return graph.findEventXYNodeProperty(typeName, x, y, property);
	}

	public List<InfoNode> findPath(String indexName, InfoAddress origin, InfoAddress destination) {
		return graph.findPath(indexName, origin, destination);
	}

	public void shutdown() {
		graph.shutdown();
	}

}
