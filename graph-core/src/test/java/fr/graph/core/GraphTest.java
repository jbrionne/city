package fr.graph.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.neo4j.kernel.impl.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import fr.graph.api.GraphEntry;
import fr.graph.api.InfoNode;

public class GraphTest {

	private static final Logger LOG = LoggerFactory.getLogger(GraphTest.class);

	private static final String INDEXNAME = "BUILDING";

	@Test
	public void f() {

		GraphEntry graph = new GraphEntry("TEST", INDEXNAME);

		graph.create(INDEXNAME, "A", 0, 0, null);
		graph.create(INDEXNAME, "B", 7, 0, null);
		graph.create(INDEXNAME, "C", 2, 1, null);
		graph.createRelationship(INDEXNAME, "A", "C", 2);
		graph.createRelationship(INDEXNAME, "C", "B", 3);
		graph.createRelationship(INDEXNAME, "A", "B", 10);

		List<InfoNode> infos = graph.findPath(INDEXNAME, "A", "B");
		for(InfoNode info : infos){
			LOG.info("{} {} {}", info.getName(), info.getX(), info.getZ());
		}
		
		graph.shutdown();

		try {
			FileUtils.deleteRecursively(new File("TEST"));
		} catch (IOException e) {
			LOG.error("graph deletion", e);
		}

	}
}
