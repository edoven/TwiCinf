package servlets;

import org.apache.log4j.Logger;
import utils.HomePathGetter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class TopicCreator
 */
public class TopicCreator extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(TopicCreator.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopicCreator() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String topicName = request.getParameter("topicName");
		LOGGER.info("topicName="+topicName);
		Integer topicFilesCount = Integer.parseInt(request.getParameter("topicFilesCount"))	;
		LOGGER.info("topicFilesCount="+topicFilesCount);
		
		
		List<String> inTopicFiles = new ArrayList<String>();		
		for (int inTopicFileIndex = 0; inTopicFileIndex<topicFilesCount; inTopicFileIndex++)
		{
			String parameter = "inTopic" + inTopicFileIndex;
			String inTopicFileX = request.getParameter(parameter);		
			if (inTopicFileX!=null)
				inTopicFiles.add(inTopicFileX);
		}
		
		List<String> outOfTopicFiles = new ArrayList<String>();		
		for (int outOfTopicFileIndex = 0; outOfTopicFileIndex<topicFilesCount; outOfTopicFileIndex++)
		{
			String parameter = "outOfTopic" + outOfTopicFileIndex;
			String outOfTopicFileX = request.getParameter(parameter);		
			if (outOfTopicFileX!=null)
				outOfTopicFiles.add(outOfTopicFileX);					
		}
			
		String CRAWNKER_HOME = HomePathGetter.getInstance().getHomePath();
		String topicFilePath = CRAWNKER_HOME+"ranking/topic/"+topicName+".txt";
		createTopicFile(topicFilePath, inTopicFiles , outOfTopicFiles);
	}

	private void createTopicFile(String filePath, List<String> inTopicFiles, List<String> outOfTopicFiles) throws IOException
	{
		File file = new File(filePath);
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i=0; i<inTopicFiles.size(); i++)
		{
			String propertyName = "inTopic"+i;
			String fileName = inTopicFiles.get(i);
			bw.write(propertyName+"="+fileName+"\n");
		}			
		bw.write("####\n");
		for (int i=0; i<outOfTopicFiles.size(); i++)
		{
			String propertyName = "outOfTopic"+i;
			String fileName = outOfTopicFiles.get(i);
			bw.write(propertyName+"="+fileName+"\n");
		}	
		bw.close();
	}
}
