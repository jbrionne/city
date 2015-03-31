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

public class GraphEntry {

	private Graph graph;

	private static final Logger LOG = LoggerFactory.getLogger(GraphEntry.class);

	public GraphEntry(String dbPath, boolean test, String... index) {
		if(test) {			
			this.graph = new Graph(new TestGraphDatabaseFactory().newImpermanentDatabase(), dbPath, index);
		} else {
			this.graph = new Graph(new GraphDatabaseFactory().newEmbeddedDatabase(dbPath), dbPath, index);
		}
		
	}

	public void createIndex(String indexName) {
		graph.createIndex(indexName);
	}

	public void createRoad(String indexName, String name, int xA, int yA,
			int xB, int yB) {
		LOG.info("createRoad " + xA + "," + yA + "," + xB + "," + yB);
		// TODO improve !!!!
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
				nA = graph.createNode(indexName, name + xA + ":" + yA, xA, yA,
						null);
			}
			if (nB == null) {
				nB = graph.createNode(indexName, name + xB + ":" + yB, xB, yB,
						null);
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

			findNodeAndMakeNetworkX(indexName, nmin, nmax, minX, minY,
					maxY);
		} else if (yA == yB) {
			minX = xA;
			maxX = xB;
			if (xA > xB) {
				minX = xB;
				maxX = xA;

				nmin = nB;
				nmax = nA;
			}
			findNodeAndMakeNetworkY(indexName, nmin, nmax, minX, maxX,
					minY);
		} else {
			throw new IllegalArgumentException(
					"you must have xA == xB or yA == yB");
		}
	}

	private void findNodeAndMakeNetworkY(String indexName,
			Node nmin, Node nmax, int minX, int maxX, int minY) {
		Node nX = nmin;
		Node prev = nmin;
		for (int i = minX + 1; i < maxX; i++) {
			Node r = graph.findEventXYNode(indexName, i, minY);
			if (r != null) {
				nX = r;
				createRelation(prev, nX);
				prev = nX;
			}
		}
		createRelation(nX, nmax);
	}

	private void findNodeAndMakeNetworkX(String indexName, 
			Node nmin, Node nmax, int minX, int minY, int maxY) {
		Node nX = nmin;
		Node prev = nmin;
		for (int i = minY + 1; i < maxY; i++) {
			Node r = graph.findEventXYNode(indexName, minX, i);
			if (r != null) {
				nX = r;
				createRelation(prev, nX);
				prev = nX;
			}
		}
		createRelation(nX, nmax);
	}

	private Node findOrCreateNode(String indexName, String name, int xB, int yB) {
		Node nB = graph.findEventXYNode(indexName, xB, yB);
		if (nB != null) {
			LOG.info("Node already exist " + xB + yB);
		} else {
			nB = graph
					.createNode(indexName, name + xB + ":" + yB, xB, yB, null);
		}
		return nB;
	}

	private void createRelation(Node nmin, Node nX) {
		if (!graph.hasRelationshipWithNode(nmin, nX)) {
			LOG.info("create relationship {} | {} ", nmin.toString(),
					nX.toString());
			graph.createRelationship(
					nmin,
					nX,
					getCost((int) graph.getProperty(nmin, Graph.X),
							(int) graph.getProperty(nmin, Graph.Y),
							(int) graph.getProperty(nX, Graph.X),
							(int) graph.getProperty(nX, Graph.Y)));

		}
	}

	public int getCost(final int xN, final int yN, final int xG, final int yG) {
		int dx = xN - xG;
		int dy = yN - yG;
		return (int) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
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

	public Object findEventXYNodeProperty(String indexName, int x, int y,
			String property) {
		return graph.findEventXYNodeProperty(indexName, x, y, property);
	}

	public List<InfoNode> findPath(String indexName, String nameA, String nameB) {
		return graph.findPath(indexName, nameA, nameB);
	}

	public void shutdown() {
		graph.shutdown();
	}

}
