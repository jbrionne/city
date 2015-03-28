package fr.graph.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.tooling.GlobalGraphOperations;

public class GraphUtils {

	private GraphUtils() {
		throw new AssertionError("Don't instantiated this class");
	}

	public static Relationship createRelationship(final Node start, final Node end, final Object... properties) {
		if(start == null) {
			throw new NullPointerException("start is null");
		}
		if(end == null) {
			throw new NullPointerException("end is null");
		}
		return setProperties(start.createRelationshipTo(end, RelTypes.EVENT), properties);
	}
	
	public static boolean hasRelationshipWithNode(final Node start, final Node end) {		
		if(!start.hasRelationship()) {
			return false;
		}
		
		Iterable<Relationship> it = start.getRelationships();
		for(Relationship r : it){
			if(r.getEndNode().equals(end)) {
				return true;
			}
		}		
		return false;
	}

	public static Node createNode(GraphDatabaseService graphDb, Index<Node> nodeIndex, final Object... properties) {
		Node n = setProperties(graphDb.createNode(), properties);
		nodeIndex.add(n, properties[0].toString(), properties[1]);
		return n;
	}

	public static Node findEventNode(GraphDatabaseService graphDb, Index<Node> nodeIndex, String idToFind) {
		try (Transaction tx = graphDb.beginTx()) {
			Node foundEvent = nodeIndex.get(Graph.NAME, idToFind).getSingle();
			tx.success();
			return foundEvent;
		}
	}
	
	public static List<Relationship> findAllRelationship(GraphDatabaseService graphDb, RelationshipType type) {
		List<Relationship>	rs = new ArrayList<>();
		Iterable<Relationship> relationships = GlobalGraphOperations.at(graphDb).getAllRelationships();
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

	public static List<Object> findAllEventNodeProperty(GraphDatabaseService graphDb, Index<Node> nodeIndex,
			String property) {
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

	public static Object findEventXYNodeProperty(GraphDatabaseService graphDb, Index<Node> nodeIndex, int x, int y,
			String property) {
		try (Transaction tx = graphDb.beginTx()) {
			Iterable<Node> hits = nodeIndex.query(Graph.X + ":" + x + " AND " + Graph.Y + ":" + y);
			Node foundEvent = null;
			if (hits.iterator().hasNext()) {
				foundEvent = hits.iterator().next();
			} else {
				return null;
			}
			tx.success();
			return foundEvent.getProperty(property);
		}
	}
	
	public static Node findEventXYNode(GraphDatabaseService graphDb, Index<Node> nodeIndex, int x, int y) {
		try (Transaction tx = graphDb.beginTx()) {
			Iterable<Node> hits = nodeIndex.query(Graph.X + ":" + x + " AND " + Graph.Y + ":" + y);
			Node foundEvent = null;
			if (hits.iterator().hasNext()) {
				foundEvent = hits.iterator().next();
			} else {
				return null;
			}
			tx.success();
			return foundEvent;
		}
	}

	public static Object findEventNodeProperty(GraphDatabaseService graphDb, Index<Node> nodeIndex, String idToFind,
			String property) {
		try (Transaction tx = graphDb.beginTx()) {
			Node foundEvent = nodeIndex.get(Graph.NAME, idToFind).getSingle();
			tx.success();
			return foundEvent.getProperty(property);
		}
	}

	public static void removeEventNode(GraphDatabaseService graphDb, Index<Node> nodeIndex, String idToRemove) {
		try (Transaction tx = graphDb.beginTx()) {
			Node current = nodeIndex.get(Graph.NAME, idToRemove).getSingle();
			if (current != null) {
				nodeIndex.remove(current);
			}
			tx.success();
		}
	}

	public static <T extends PropertyContainer> T setProperties(final T entity, final Object[] properties) {
		for (int i = 0; i < properties.length; i = i + 2) {
			String key = properties[i].toString();
			Object value = properties[i + 1];
			entity.setProperty(key, value);
		}
		return entity;
	}
}
