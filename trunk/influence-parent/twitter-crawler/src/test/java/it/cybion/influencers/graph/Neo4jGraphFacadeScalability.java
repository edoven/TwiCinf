package it.cybion.influencers.graph;

import it.cybion.influencers.crawler.graph.Neo4jGraphFacade;
import it.cybion.influencers.crawler.graph.exceptions.UserVertexNotPresentException;
import it.cybion.influencers.crawler.graph.indexes.GraphIndexType;
import it.cybion.influencers.crawler.utils.FilesDeleter;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;



public class Neo4jGraphFacadeScalability
{

	private static final Logger logger = Logger.getLogger(Neo4jGraphFacadeScalability.class);

	private int USERS_COUNT = 1000000;

	@Test(enabled = false)
	public void massiveInsertionsWithLucene() throws IOException, UserVertexNotPresentException
	{
		String graphDirPath = "src/test/resources/graphs/massiveInsertionsTEST";
		FilesDeleter.delete(new File(graphDirPath));
		Neo4jGraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath, GraphIndexType.LUCENE_INDEX);
		int tenPercent = Math.round((float) USERS_COUNT / 10);
		int percentCompleted = 0;
		long start = System.currentTimeMillis();
		for (long i = 0; i < USERS_COUNT; i++)
		{
			if (i % tenPercent == 0)
			{
				logger.info("completed " + percentCompleted + "%");
				percentCompleted = percentCompleted + 10;
			}
			graphFacade.getOrPutUser(i);
		}
		long end = System.currentTimeMillis();
		long time = end - start;
		logger.info("TIME = " + time / 1000.0);
	}

	@Test(enabled = true)
	public void massiveInsertionsWithTreeMap() throws IOException, UserVertexNotPresentException
	{
		String graphDirPath = "src/test/resources/graphs/massiveInsertionsTEST";
		FilesDeleter.delete(new File(graphDirPath));
		Neo4jGraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath, GraphIndexType.TREEMAP);
		int tenPercent = Math.round((float) USERS_COUNT / 10);
		int percentCompleted = 0;
		long start = System.currentTimeMillis();
		for (long i = 0; i < USERS_COUNT; i++)
		{
			if (i % tenPercent == 0)
			{
				logger.info("completed " + percentCompleted + "%");
				percentCompleted = percentCompleted + 10;
			}
			graphFacade.getOrPutUser(i);
		}
		long end = System.currentTimeMillis();
		long time = end - start;
		logger.info("TIME = " + time / 1000.0);
	}

}
