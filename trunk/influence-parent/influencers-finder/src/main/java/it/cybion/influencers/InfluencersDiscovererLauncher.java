package it.cybion.influencers;

import java.io.IOException;

import org.apache.log4j.Logger;

public class InfluencersDiscovererLauncher
{
	private static final Logger logger = Logger.getLogger(InfluencersDiscovererLauncher.class);
	
	public static void main(String[] args) throws IOException
	{
		if (args.length<1)
		{
			logger.info("ERROR! Usage: java -jar <runnable_jar> <configuration_file>");
			System.exit(0);
		}
			
		String configurationFile = args[0];
//		String configurationFile = "/home/godzy/Desktop/trashion_la_perla.properties";

		InfluencersDiscoverer influencersDiscoverer = ConfigurationFileParser.getInfluencersDiscovererFromConfiguration(configurationFile);
		influencersDiscoverer.getInfluencers();
	}
}
