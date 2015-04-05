package fr.graph.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.tooling.GlobalGraphOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.graph.api.InfoRelationShip;

public class GraphUtils {

	private static final Logger LOG = LoggerFactory.getLogger(GraphUtils.class);

	private GraphUtils() {
		throw new AssertionError("Don't instantiate this class");
	}

	public static Relationship createRelationship(final Node start,
			final Node end, String lengthName, int length) {
		if (start == null) {
			throw new NullPointerException("start is null");
		}
		if (end == null) {
			throw new NullPointerException("end is null");
		}

		Relationship r = start.createRelationshipTo(end, RelTypes.EVENT);
		r.setProperty(Graph.NAME, getRelationShipName((Integer) start.getProperty(Graph.X), (Integer)start.getProperty(Graph.Y), (Integer)end.getProperty(Graph.X), (Integer)end.getProperty(Graph.Y)));
		r.setProperty(lengthName, length);
		return r;
	}
	
	public static String getRelationShipName(int x, int y, int xD, int yD) {
		return x + ":" + y + ":" + xD + ":" + yD;
	}
	public static InfoRelationShip getRelationShipInfoByName(String name) {
		InfoRelationShip i = new InfoRelationShip();
		String[] s = name.split(":");
		i.setX(Integer.valueOf(s[0]));
		i.setZ(Integer.valueOf(s[1]));
		i.setxD(Integer.valueOf(s[2]));
		i.setzD(Integer.valueOf(s[3]));
		return i;
	}
	
	public static String getNodeName(String indexName, String id) {
		return indexName + ":" + id;
	}

	public static String getNodeNameRoad(String indexName, int xA, int yA) {
		return indexName + xA + ":" + yA;
	}
	
	
	public static boolean hasRelationshipWithNode(final Node start,
			final Node end) {
		if (!start.hasRelationship()) {
			return false;
		}
		
		if (!end.hasRelationship()) {
			return false;
		}

		Iterable<Relationship> it = start.getRelationships();
		for (Relationship r : it) {
			LOG.info("hasRelationshipWithNode " + start +  " " + r.getEndNode() + " " + end + " " + r.getStartNode());
			if (r.getEndNode().equals(end) && r.getStartNode().equals(start)) {
				return true;
			}
			if (r.getStartNode().equals(end) && r.getEndNode().equals(start)) {
				return true;
			}
		}
		return false;
	}

	public static Node createNode(GraphDatabaseService graphDb,
			Index<Node> nodeIndex, String name, String name2, String x, int x2,
			String y, int y2, String custom, String json) {
		
		if(!name2.startsWith(nodeIndex.getName())){
			throw new IllegalArgumentException(
					"name must start with index name " + nodeIndex.getName() + " "+ name2);
		}
		
		if(name2.equals(nodeIndex.getName())){
			throw new IllegalArgumentException(
					"name must start with index name " + nodeIndex.getName() + " "+ name2);
		}
		
		Node entity = graphDb.createNode();
		entity.setProperty(name, name2);
		entity.setProperty(x, x2);
		entity.setProperty(y, y2);
		entity.setProperty(custom, json);
		nodeIndex.add(entity, name, name2);
		LOG.debug("createNode with custom {} {}", name, name2);
		return entity;
	}

	public static Node createNode(GraphDatabaseService graphDb,
			Index<Node> nodeIndex, String name, String name2, String x, int x2,
			String y, int y2) {
		
		if(!name2.startsWith(nodeIndex.getName())){
			throw new IllegalArgumentException(
					"name must start with index name " + nodeIndex.getName() + " "+ name2);
		}
		
		if(name2.equals(nodeIndex.getName())){
			throw new IllegalArgumentException(
					"name must start with index name " + nodeIndex.getName() + " "+ name2);
		}
		
		
		Node entity = graphDb.createNode();
		entity.setProperty(name, name2);
		entity.setProperty(x, x2);
		entity.setProperty(y, y2);
		nodeIndex.add(entity, name, name2);
		LOG.debug("createNode {} {}", name, name2);
		return entity;
	}

	public static Node findEventNode(GraphDatabaseService graphDb,
			Index<Node> nodeIndex, String idToFind) {
		try (Transaction tx = graphDb.beginTx()) {
			Node foundEvent = nodeIndex.get(Graph.NAME, idToFind).getSingle();
			tx.success();
			return foundEvent;
		}
	}
	
	public static Relationship findRelationshipByName(
		GraphDatabaseService graphDb, String indexName, RelationshipType typeName, String name) {		
			InfoRelationShip i = getRelationShipInfoByName(name);			
		String nameNode = getNodeNameRoad(indexName, i.getX(), i.getZ());			
		String query = "MATCH (n { " + Graph.NAME +": '"+ nameNode + "' })-[r:" + typeName.name() + "]->(c) RETURN r";
		Result result = graphDb.execute(query);
		LOG.debug("findRelationshipByName " + " query " + query + " result "+ result.toString());			
		while (result.hasNext()) {
			Map<String, Object> row = result.next();
			for (Entry<String, Object> column : row.entrySet()) {
				Relationship foundEvent = (Relationship) column.getValue();
				return foundEvent;
			}
		}	
		
		String nameNode2 = getNodeNameRoad(indexName, i.getxD(), i.getzD());	
		String query2 = "MATCH (n { " + Graph.NAME +": '"+ nameNode2 + "' })-[r:" + typeName.name() + "]->(c) RETURN r";
		Result result2 = graphDb.execute(query2);
		LOG.debug("findRelationshipByName " + " query " + query2 + " result "+ result2.toString());			
		while (result2.hasNext()) {
			Map<String, Object> row = result2.next();
			for (Entry<String, Object> column : row.entrySet()) {
				Relationship foundEvent = (Relationship) column.getValue();
				return foundEvent;
			}
		}
		
		return null;	
	}

	public static List<Relationship> findAllRelationship(
			GraphDatabaseService graphDb, RelationshipType type) {
		List<Relationship> rs = new ArrayList<>();
		Iterable<Relationship> relationships = GlobalGraphOperations
				.at(graphDb).getAllRelationships();
		Iterator<Relationship> it = relationships.iterator();
		while (it.hasNext()) {
			Relationship r = it.next();
			if (r.isType(type)) {
				rs.add(r);
			}
		}
		return rs;
	}

	public static List<Node> findAllEventNode(Index<Node> nodeIndex) {
		List<Node> res = new ArrayList<>();
		Iterator<Node> it = nodeIndex.query("*:*").iterator();
		while (it.hasNext()) {
			res.add(it.next());
		}
		return res;
	}

	public static List<Object> findAllEventNodeProperty(
			GraphDatabaseService graphDb, Index<Node> nodeIndex, String property) {
		List<Object> res = new ArrayList<>();
		try (Transaction tx = graphDb.beginTx()) {
			Iterator<Node> it = nodeIndex.query("*:*").iterator();
			while (it.hasNext()) {
				res.add(it.next().getProperty(property));
			}
			tx.success();
			return res;
		}
	}

	public static Object findEventXYNodeProperty(GraphDatabaseService graphDb, String typeName, int x, int y, String property) {
		try (Transaction tx = graphDb.beginTx()) {
			String query = "match (n) where n." + Graph.NAME + "=~'"
					+ typeName + ".*' AND n." + Graph.X + "="
					+ x + " AND n." + Graph.Y + "=" + y + " return n";
			Result result = graphDb.execute(query);
			LOG.debug("findEventXYNodeProperty " + " query " + query + " result "+ result.toString());
			while (result.hasNext()) {
				Map<String, Object> row = result.next();
				LOG.debug("findEventXYNodeProperty " + row.toString());
				for (Entry<String, Object> column : row.entrySet()) {
					Node foundEvent = (Node) column.getValue();
					return foundEvent.getProperty(property);
				}
			}
			tx.success();
			return null;
		}
	}

	public static Node findEventXYNode(GraphDatabaseService graphDb, String typeName, int x, int y) {
		try (Transaction tx = graphDb.beginTx()) {
			String query = "match (n) where n." + Graph.NAME + "=~'"
					+ typeName + ".*' AND n." + Graph.X + "="
					+ x + " AND n." + Graph.Y + "=" + y + " return n";
			Result result = graphDb.execute(query);
			LOG.debug("findEventXYNode " + " query " + query + " result "+ result.toString());			
			while (result.hasNext()) {
				Map<String, Object> row = result.next();
				for (Entry<String, Object> column : row.entrySet()) {
					Node foundEvent = (Node) column.getValue();
					return foundEvent;
				}
			}
			tx.success();
			return null;
		}
	}

	public static Object findEventNodeProperty(GraphDatabaseService graphDb,
			Index<Node> nodeIndex, String idToFind, String property) {
		try (Transaction tx = graphDb.beginTx()) {
			Node foundEvent = nodeIndex.get(Graph.NAME, idToFind).getSingle();
			tx.success();
			return foundEvent.getProperty(property);
		}
	}

	public static void removeEventNode(GraphDatabaseService graphDb,
			Index<Node> nodeIndex, String idToRemove) {
		try (Transaction tx = graphDb.beginTx()) {
			Node current = nodeIndex.get(Graph.NAME, idToRemove).getSingle();
			if (current != null) {
				nodeIndex.remove(current);					
				current.delete();
			}
			tx.success();
		}
	}
	
	


}
