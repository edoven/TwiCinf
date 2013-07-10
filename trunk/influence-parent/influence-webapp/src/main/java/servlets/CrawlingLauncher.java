package servlets;

import it.cybion.influencers.crawler.Crawler;
import it.cybion.influencers.crawler.launcher.parsing.ProperitesFileParser;
import utils.HomePathGetter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


public class CrawlingLauncher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public CrawlingLauncher() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String CRAWNKER_HOME = HomePathGetter.getInstance().getHomePath();
		final String generalConfigFilePath = CRAWNKER_HOME+"general.config";		
		String configFilePath = request.getParameter("configFilePath");
		
		Properties generalProperties = getProperties(generalConfigFilePath);
		Properties inputFileProperties = getProperties(configFilePath);
		Properties totalProperties = getTotalProperties(generalProperties, inputFileProperties);
	
		Crawler crawler = ProperitesFileParser.getCrawlerFromProperties(totalProperties);
		List<Long> crawledUsers = crawler.getInfluencers();
		String outputDirPath = CRAWNKER_HOME+"crawling/output/";
		String fileName = request.getParameter("fileName");	
		if (fileName==null)
			fileName = UUID.randomUUID().toString();
		String outputFilePath = outputDirPath+fileName;
		File file = new File(outputFilePath);
		if (file.exists())
		{
			fileName = UUID.randomUUID().toString();
			outputFilePath = outputDirPath+fileName;
		}
		writeCrawledUsersToFile(outputFilePath, crawledUsers);	
		
		request.setAttribute("outputFilePath", outputDirPath+fileName);
		request.setAttribute("crawledUsers", crawledUsers);
		request.getRequestDispatcher("crawling-result.jsp").forward(request, response);
	}
	
	
	
	private void writeCrawledUsersToFile(String filePath, List<Long> usersIds)
	{    
		try
		{
			File outputFile = new File(filePath);
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFile));
			for (Long userId : usersIds)
				fileWriter.write(userId+"\n");		
			fileWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}    
	}
	
	private Properties getTotalProperties(Properties generalProperties, Properties inputFileProperties)
	{
		Properties totalProperties = new Properties();
		for (Object propertyName : generalProperties.keySet())
			totalProperties.put(propertyName, generalProperties.get(propertyName));
		for (Object propertyName : inputFileProperties.keySet())
			totalProperties.put(propertyName, inputFileProperties.get(propertyName));
		return totalProperties;
	}
	
	
	private Properties getProperties(String filePath) throws IOException
	{
		InputStream inputStream = new FileInputStream(new File(filePath));
		Properties properties = new Properties();
		properties.load(inputStream);
		return properties;
	}	
}
