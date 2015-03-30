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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphUtils {

	private static final Logger LOG = LoggerFactory.getLogger(GraphUtils.class);

	private GraphUtils() {
		throw new AssertionError("Don't instantiated this class");
	}

	public static Relationship createRelationship(final Node start, final Node end, String lengthName, int length) {
		if(start == null) {
			throw new NullPointerException("start is null");
		}
		if(end == null) {
			throw new NullPointerException("end is null");
		}
		
		Relationship r = start.createRelationshipTo(end, RelTypes.EVENT);		
		r.setProperty(lengthName, length);		
		return r;
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
	
	public static Node createNode(GraphDatabaseService graphDb,
			Index<Node> nodeIndex, String name, String name2, String x, int x2,
			String y, int y2, String custom, String json) {
		Node entity = graphDb.createNode();		
		entity.setProperty(name, name2);
		entity.setProperty(x, x2);	
		entity.setProperty(y, y2);
		entity.setProperty(custom, json);		
		nodeIndex.add(entity, name, name2);
		return entity;
	}
	
	public static Node createNode(GraphDatabaseService graphDb,
			Index<Node> nodeIndex, String name, String name2, String x, int x2,
			String y, int y2) {
		Node entity = graphDb.createNode();		
		entity.setProperty(name, name2);
		entity.setProperty(x, x2);	
		entity.setProperty(y, y2);		
		nodeIndex.add(entity, name, name2);
		return entity;
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
			Iterable<Node> hits = nodeIndex.query(Graph.X, x);				
			Node foundEvent = null;
			if (hits.iterator().hasNext()) {
				foundEvent = hits.iterator().next();
				Integer o = (Integer) foundEvent.getProperty(Graph.Y);
				if(o != null) {
					if(o.intValue() == y)  {					
						return foundEvent.getProperty(property);
					}
				}				
			} else {
				return null;
			}
			
			tx.success();
			return foundEvent.getProperty(property);
		}
	}
	
	public static Node findEventXYNode(GraphDatabaseService graphDb, Index<Node> nodeIndex, int x, int y) {
		try (Transaction tx = graphDb.beginTx()) {		
			Iterable<Node> hits = nodeIndex.query(Graph.X, x);				
			Node foundEvent = null;
			if (hits.iterator().hasNext()) {
				foundEvent = hits.iterator().next();
				Integer o = (Integer) foundEvent.getProperty(Graph.Y);
				if(o != null) {
					if(o.intValue() == y)  {					
						return foundEvent;
					}
				}				
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

	

	
}
