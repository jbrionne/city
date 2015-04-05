package fr.graph.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphalgo.CommonEvaluators;
import org.neo4j.graphalgo.EstimateEvaluator;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.graph.api.InfoAddress;
import fr.graph.api.InfoNode;
import fr.graph.api.InfoRelationShip;

public class Graph {

	private static final Logger LOG = LoggerFactory.getLogger(Graph.class);

	private GraphDatabaseService graphDb;

	public static final String LENGTH = "length";
	public static final String NAME = "name";
	public static final String X = "x";
	public static final String Y = "y";
	public static final String CUSTOM = "custom";

	private EstimateEvaluator<Double> estimateEvaluator;

	private Map<String, Index<Node>> nodesIndexMap = new HashMap<>();

	public Graph(GraphDatabaseService graphDb, String dbPath, String... nodesIndex) {
		this.graphDb = graphDb;
		try (Transaction tx = graphDb.beginTx()) {
			registerShutdownHook(graphDb);
			for (String s : nodesIndex) {
				Index<Node> nodeIndex = graphDb.index().forNodes(s);
				nodesIndexMap.put(s, nodeIndex);
			}
			tx.success();
		}

		this.estimateEvaluator = new EstimateEvaluator<Double>() {
			@Override
			public Double getCost(final Node node, final Node goal) {
				double dx = (double) (Integer) node.getProperty(X)-  (double)(Integer) goal.getProperty(X);
				double dy =  (double) (Integer) node.getProperty(Y) -   (double)(Integer) goal.getProperty(Y);
				return (Double) (Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
			}
		};
	}

	public void createIndex(String indexName) {
		try (Transaction tx = graphDb.beginTx()) {
			Index<Node> nodeIndex = graphDb.index().forNodes(indexName);
			nodesIndexMap.put(indexName, nodeIndex);
			tx.success();
		}
	}

	public Node createNode(String indexName, String name, int x, int y, String json) {
		
		if(!name.startsWith(indexName)){
			throw new IllegalArgumentException(
					"name must start with index name " + indexName + " "+ name);
		}
		
		if(name.equals(indexName)){
			throw new IllegalArgumentException(
					"name must start with index name but not equals to index name " + indexName + " "+ name);
		}
		
		Node node = null;
		try (Transaction tx = graphDb.beginTx()) {

			Index<Node> nodeIndex = nodesIndexMap.get(indexName);
			if (nodeIndex == null) {
				throw new IllegalArgumentException("The node index doesn't exist " + indexName);
			}

			Node n = find(indexName, name);
			if (n != null) {
				LOG.warn("Already exist " + name);
				GraphUtils.removeEventNode(graphDb, nodeIndex, name);
			}

			if (json != null) {
				node = GraphUtils.createNode(graphDb, nodeIndex, NAME, name, X, x, Y, y, CUSTOM, json);
			} else {
				node = GraphUtils.createNode(graphDb, nodeIndex, NAME, name, X, x, Y, y);
			}
			tx.success();
		}
		return node;
	}

	public void createRelationship(String indexName, String nameA, String nameB, int length) {
		try (Transaction tx = graphDb.beginTx()) {
			Node nodeA = find(indexName, nameA);
			if(nodeA == null){
				throw new NullPointerException("No node for name " + indexName + " " + nameA);
			}
			Node nodeB = find(indexName, nameB);
			if(nodeB == null){
				throw new NullPointerException("No node for name " + indexName + " " + nameB);
			}
			GraphUtils.createRelationship(nodeA, nodeB, LENGTH, length);
			tx.success();
		}

	}
	
	public Relationship createRelationship(Node nodeA, Node nodeB, int length) {
		Relationship r = null;
		try (Transaction tx = graphDb.beginTx()) {			
			r = GraphUtils.createRelationship(nodeA, nodeB, LENGTH, length);
			tx.success();
		}
		return r;
	}

	public boolean hasRelationshipWithNode(final Node nodeA, final Node nodeB) {
		boolean res = false;
		try (Transaction tx = graphDb.beginTx()) {
			res = GraphUtils.hasRelationshipWithNode(nodeA, nodeB);
			tx.success();
		}
		return res;
	}

	private Node find(String indexName, String name) {
		Node res = null;
		Index<Node> nodeIndex = nodesIndexMap.get(indexName);
		if (nodeIndex == null) {
			throw new IllegalArgumentException("The node index doesn't exist " + indexName);
		}

		res = GraphUtils.findEventNode(graphDb, nodeIndex, name);
		return res;
	}

	public void remove(String indexName, String name) {
		Index<Node> nodeIndex = nodesIndexMap.get(indexName);
		if (nodeIndex == null) {
			throw new IllegalArgumentException("The node index doesn't exist " + indexName);
		}

		GraphUtils.removeEventNode(graphDb, nodeIndex, name);
	}

	public List<Node> findAll(String indexName) {
		List<Node> res = null;
		try (Transaction tx = graphDb.beginTx()) {

			Index<Node> nodeIndex = nodesIndexMap.get(indexName);
			if (nodeIndex == null) {
				throw new IllegalArgumentException("The node index doesn't exist " + indexName);
			}

			res = GraphUtils.findAllEventNode(nodeIndex);
			tx.success();
		}
		return res;
	}
	
	public Relationship findRelationshipByName(RelationshipType type, String indexName, String name) {
		Relationship res = null;
		try (Transaction tx = graphDb.beginTx()) {
			res = GraphUtils.findRelationshipByName(graphDb, indexName, type, name);
			tx.success();
		}
		return res;
	}

	public List<Relationship> findAllRelationship(RelationshipType type) {
		List<Relationship> res = null;
		try (Transaction tx = graphDb.beginTx()) {
			res = GraphUtils.findAllRelationship(graphDb, type);
			tx.success();
		}
		return res;
	}

	public List<Object> findAll(String indexName, String property) {
		List<Object> res = null;
		try (Transaction tx = graphDb.beginTx()) {

			Index<Node> nodeIndex = nodesIndexMap.get(indexName);
			if (nodeIndex == null) {
				throw new IllegalArgumentException("The node index doesn't exist " + indexName);
			}

			res = GraphUtils.findAllEventNodeProperty(graphDb, nodeIndex, property);
			tx.success();
		}
		return res;
	}

	public Object findEventXYNodeProperty(String typeName, int x, int y, String property) {		
		return GraphUtils.findEventXYNodeProperty(graphDb, typeName, x, y, property);

	}

	public Node findEventXYNode(String typeName, int x, int y) {		
		return GraphUtils.findEventXYNode(graphDb, typeName, x, y);
	}
	
	public Object getProperty(Node node, String property) {	
		Object res = null;
		try (Transaction tx = graphDb.beginTx()) {
			res = node.getProperty(property);			
			tx.success();
		}
		return res;		
	}
	
	public Node getStartNode(Relationship r) {	
		Node res = null;
		try (Transaction tx = graphDb.beginTx()) {
			res = r.getStartNode();			
			tx.success();
		}
		return res;		
	}
	
	public Node getEndNode(Relationship r) {	
		Node res = null;
		try (Transaction tx = graphDb.beginTx()) {
			res = r.getEndNode();			
			tx.success();
		}
		return res;		
	}
		

	public Object find(String indexName, String name, String property) {
		Object res = null;
		Index<Node> nodeIndex = nodesIndexMap.get(indexName);
		if (nodeIndex == null) {
			throw new IllegalArgumentException("The node index doesn't exist " + indexName);
		}

		res = GraphUtils.findEventNodeProperty(graphDb, nodeIndex, name, property);
		return res;
	}

	public void shutdown() {
		graphDb.shutdown();
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running example before it's completed)
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

	public List<InfoNode> findPath(String indexName, InfoAddress origin, InfoAddress destination) {
		List<InfoNode> pathNames = new LinkedList<>();
		
			
			boolean isA = true;
			boolean isB = true;
			Node nodeA = findEventXYNode(indexName, origin.getX(), origin.getZ());
			Node nodeB = findEventXYNode(indexName, destination.getX(), destination.getZ());
			
			if (nodeA == null) {
				nodeA = createNode(indexName,
						GraphUtils.getNodeNameRoad(indexName, origin.getX(), origin.getZ()), origin.getX(), origin.getZ(), null);
				isA = false;
			}
			if (nodeB == null) {
				nodeB = createNode(indexName,
						GraphUtils.getNodeNameRoad(indexName, destination.getX(), destination.getZ()), destination.getX(), destination.getZ(), null);
				isB = false;
			}
			
			InfoRelationShip iA = GraphUtils.getRelationShipInfoByName(origin.getRoadName());
			String nameNodeA = GraphUtils.getNodeNameRoad(indexName, iA.getX(), iA.getZ());	
			String nameNodeABis = GraphUtils.getNodeNameRoad(indexName, iA.getxD(), iA.getzD());	
			
			InfoRelationShip iB = GraphUtils.getRelationShipInfoByName(destination.getRoadName());			
			String nameNodeB = GraphUtils.getNodeNameRoad(indexName, iB.getX(), iB.getZ());
			String nameNodeBBis = GraphUtils.getNodeNameRoad(indexName, iB.getxD(), iB.getzD());
			
			Node rA = find(indexName, nameNodeA);
			if(rA == null){
				throw new NullPointerException("No node for name " + indexName + " " + nameNodeA);
			}
			Node rABis = find(indexName, nameNodeABis);
			if(rABis == null){
				throw new NullPointerException("No node for name " + indexName + " " + nameNodeABis);
			}
			
			Node rB = find(indexName, nameNodeB);			
			if(rB == null){
				throw new NullPointerException("No node for name " + indexName + " " + nameNodeB);
			}
			Node rBBis = find(indexName, nameNodeBBis);			
			if(rBBis == null){
				throw new NullPointerException("No node for name " + indexName + " " + nameNodeBBis);
			}
			
			Relationship relA = null;
			if(getCost(iA.getX(), iA.getZ(), origin.getX(), origin.getZ()) < getCost(iA.getxD(), iA.getzD(), origin.getX(), origin.getZ())) {
				relA = createRelationship(rA, nodeA, getCost(iA.getX(), iA.getZ(), origin.getX(), origin.getZ()));
			} else {
				relA = createRelationship(rABis, nodeA, getCost(iA.getxD(), iA.getzD(), origin.getX(), origin.getZ()));
			}
			
			Relationship relB = null;
			if(getCost(iB.getX(), iB.getZ(), destination.getX(), destination.getZ()) < getCost(iB.getxD(), iB.getzD(), destination.getX(), destination.getZ())) {
				relB = createRelationship(rB, nodeB, getCost(iB.getX(), iB.getZ(), destination.getX(), destination.getZ()));
			} else {
				relB = createRelationship(rBBis, nodeB, getCost(iB.getxD(), iB.getzD(), destination.getX(), destination.getZ()));
			}
			
			Relationship relC = null;
			if(origin.getRoadName().equals(destination.getRoadName())) {
				relC = createRelationship(nodeA, nodeB, getCost(origin.getX(), origin.getZ(), destination.getX(), destination.getZ()));
			}
		
			try (Transaction tx = graphDb.beginTx()) {	
				WeightedPath path = findWeightedPath(nodeA, nodeB);
				Iterator<PropertyContainer> i = path.iterator();
				while (i.hasNext()) {
					InfoNode infoNode = new InfoNode();
					PropertyContainer p = i.next();
					if(p.hasProperty(Graph.NAME)) {
						infoNode.setName((String) p.getProperty(Graph.NAME));
					}
					if(p.hasProperty(Graph.X)) {
						infoNode.setX((int) p.getProperty(Graph.X));
					}
					if(p.hasProperty(Graph.Y)) {
						infoNode.setZ((int) p.getProperty(Graph.Y));
					}
					pathNames.add(infoNode);
					
				}			
						
				relA.delete();
				relB.delete();	
				if(relC != null) {
					relC.delete();
				}
				
				if(!isB){
					nodeB.delete();
				}
				if(!isA){
					nodeA.delete();	
				}		
				
				
			tx.success();
		}
		return pathNames;
	}
	
	public void createRelation(Node nmin, Node nX) {
		LOG.info("create relationship {} | {} ", nmin.toString(),
				nX.toString());
		if (!hasRelationshipWithNode(nmin, nX)) {
			LOG.info("create relationship 2 {} | {} ", nmin.toString(),
					nX.toString());
			createRelationship(
					nmin,
					nX,
					getCost((int) getProperty(nmin, Graph.X),
							(int) getProperty(nmin, Graph.Y),
							(int) getProperty(nX, Graph.X),
							(int) getProperty(nX, Graph.Y)));

		}
	}
	
	public int getCost(final int xN, final int yN, final int xG, final int yG) {
		int dx = xN - xG;
		int dy = yN - yG;
		return (int) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}

	private WeightedPath findWeightedPath(Node nodeA, Node nodeB) {
		WeightedPath path = null;
		try (Transaction tx = graphDb.beginTx()) {
			PathFinder<WeightedPath> astar = GraphAlgoFactory.aStar(PathExpanders.allTypesAndDirections(),
					CommonEvaluators.doubleCostEvaluator(LENGTH), estimateEvaluator);
			path = astar.findSinglePath(nodeA, nodeB);
			tx.success();
		}
		return path;
	}

}
