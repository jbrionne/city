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

	public Object findRoad(String indexName, String name) {
		return graph.findRelationshipByNameProperty(RelTypes.EVENT, indexName, name, Graph.CUSTOM);		
	}
	
	public void createSource(String indexName, int xA, int yA) {
		Node nA = graph.findEventXYNode(indexName, xA, yA);
		if (nA != null) {
			throw new IllegalArgumentException("the source already exist ");			
		} else {
			nA = graph.createNode(indexName,
					getNodeNameRoad(indexName, xA, yA), xA, yA, null);
		}		
	}
	
	public boolean checkIfSourceExists(String indexName, int xA, int yA) {
		Node nA = graph.findEventXYNode(indexName, xA, yA);
		if (nA != null) {
			return	true;		
		} else {
			return false;
		}		
	}

	public void createRoad(String indexName, int xA, int yA, int xB, int yB, String json) {

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
			if(nA == null && nB == null){					
				throw new IllegalArgumentException("A road must be connect to an another road endpoint or a source"
						+ xA + "," + yA + "," + xB + "," + yB);
			}
			if (nA == null) {
				nA = graph.createNode(indexName,
						getNodeNameRoad(indexName, xA, yA), xA, yA, null);
			}
			if (nB == null) {
				nB = graph.createNode(indexName,
						getNodeNameRoad(indexName, xB, yB), xB, yB, null);
			}
		}
		
		createRelation(nA, nB, json);
	}

	private void createRelation(Node nmin, Node nX, String json) {
		graph.createRelation(nmin, nX, json);		
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
	
	public void update(String indexName, String name, String json) {
		graph.updateNode(indexName, name, json);
	}

	public void createRelationship(String indexName, String nameA,
			String nameB, int length, String json) {
		graph.createRelationship(indexName, nameA, nameB, length, json);
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

	public List<Object> findAllRelationProperty(RelationshipType type) {
		return graph.findAllRelationshipProperty(type);
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
