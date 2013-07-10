package it.cybion.influencers.crawler.launcher;

import it.cybion.influencers.crawler.Crawler;
import it.cybion.influencers.crawler.launcher.parsing.ProperitesFileParser;
import org.apache.log4j.Logger;

import java.io.IOException;

public class CommandLineCrawlerLauncher
{
	private static final Logger logger = Logger.getLogger(CommandLineCrawlerLauncher.class);
	
	public static void main(String[] args) throws IOException
	{
		if (args.length<1)
		{
			logger.info("ERROR! Usage: java -jar <this_jar> <configuration_file>");
			System.exit(0);
		}
			
		String configurationFile = args[0];
//		String configurationFile = "/home/godzy/Desktop/trashion_la_perla.properties";

		Crawler influencersDiscoverer = ProperitesFileParser.getCrawlerFromPropertiesFile(configurationFile);
		influencersDiscoverer.getInfluencers();
	}
}
