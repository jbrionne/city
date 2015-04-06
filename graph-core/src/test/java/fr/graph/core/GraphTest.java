package fr.graph.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.neo4j.io.fs.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import fr.graph.api.GraphEntry;
import fr.graph.api.InfoAddress;
import fr.graph.api.InfoNode;

public class GraphTest {

	private static final Logger LOG = LoggerFactory.getLogger(GraphTest.class);

	private static final String INDEXNAME = "BUILDING";

	@Test
	public void f() {

//		GraphEntry graph = new GraphEntry("TEST", true, INDEXNAME);		
//		
//		graph.create(INDEXNAME, graph.getNodeName(INDEXNAME, "A"), 0, 0, null);
//		graph.create(INDEXNAME, graph.getNodeName(INDEXNAME, "B"), 7, 0, null);
//		graph.create(INDEXNAME, graph.getNodeName(INDEXNAME, "C"), 2, 1, null);
//		
//		graph.createRelationship(INDEXNAME, graph.getNodeName(INDEXNAME, "A"), graph.getNodeName(INDEXNAME, "C"), 2, "");
//		graph.createRelationship(INDEXNAME, graph.getNodeName(INDEXNAME, "C"), graph.getNodeName(INDEXNAME, "B"), 3, "");
//		graph.createRelationship(INDEXNAME, graph.getNodeName(INDEXNAME, "A"), graph.getNodeName(INDEXNAME, "B"), 10, "");
//
//		InfoAddress infoA = new InfoAddress();
//		infoA.setRoadName(graph.getRelationShipName(1, 1, 2, 2));
//		infoA.setX(1);
//		infoA.setZ(1);
//		
//		InfoAddress infoB = new InfoAddress();
//		infoB.setRoadName(graph.getRelationShipName(1, 1, 2, 2));
//		infoB.setX(1);
//		infoB.setZ(1);
//		
//		List<InfoNode> infos = graph.findPath(INDEXNAME, infoA, infoB);
//		for(InfoNode info : infos){
//			LOG.info("{} {} {}", info.getName(), info.getX(), info.getZ());
//		}
//		
//		graph.shutdown();
//
//		try {
//			FileUtils.deleteRecursively(new File("TEST"));
//		} catch (IOException e) {
//			LOG.error("graph deletion", e);
//		}

	}
}
