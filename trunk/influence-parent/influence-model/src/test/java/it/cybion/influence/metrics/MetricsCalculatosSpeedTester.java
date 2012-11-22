package it.cybion.influence.metrics;

import it.cybion.influence.model.Tweet;
import it.cybion.influence.util.JsonDeserializer;
import it.cybion.influence.util.MysqlConnector;

import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

public class MetricsCalculatosSpeedTester {
	
	private static final Logger logger = Logger.getLogger(MetricsCalculatosSpeedTester.class);

	/*
	 * This is not a test, it only measure the computational time difference 
	 * between the old MetricsCalculator and the new AtomicMetricsCalculator
	 */
	@Test
	public void testTheSpeedOfThe2MetricsCalculators() {
		List<Tweet> tweets = JsonDeserializer.jsons2tweets(MysqlConnector.getAllTwitterJsons());
		
		MetricsCalculator metricCalculator = new MetricsCalculator(tweets);
		AtomicMetricsCalculator atomicMetricCalculator = new AtomicMetricsCalculator(tweets);
		
		
		long start = System.currentTimeMillis();
		metricCalculator.getReport();
		long end = System.currentTimeMillis();
		logger.info("6000 tweets - metricCalculator: "+ (end-start));
		
		
		start = System.currentTimeMillis();
		atomicMetricCalculator.getReport();
		end = System.currentTimeMillis();
		logger.info("6000 tweets - atomicMetricCalculator: "+ (end-start));

		
		
		
		
		tweets.addAll(tweets); //6000+6000
		metricCalculator = new MetricsCalculator(tweets);
		atomicMetricCalculator = new AtomicMetricsCalculator(tweets);
		
		start = System.currentTimeMillis();
		metricCalculator.getReport();
		end = System.currentTimeMillis();
		logger.info("12000 - metricCalculator: "+ (end-start));
		
		
		start = System.currentTimeMillis();
		atomicMetricCalculator.getReport();
		end = System.currentTimeMillis();
		logger.info("12000 tweets - atomicMetricCalculator: "+ (end-start));

		
		
		
		
		tweets.addAll(tweets); //12000+6000
		tweets.addAll(tweets); //18000+6000
		metricCalculator = new MetricsCalculator(tweets);
		atomicMetricCalculator = new AtomicMetricsCalculator(tweets);
		
		start = System.currentTimeMillis();
		metricCalculator.getReport();
		end = System.currentTimeMillis();
		logger.info("24000 - metricCalculator: "+ (end-start));
		
		
		start = System.currentTimeMillis();
		atomicMetricCalculator.getReport();
		end = System.currentTimeMillis();
		logger.info("24000 tweets - atomicMetricCalculator: "+ (end-start));
	}

}
