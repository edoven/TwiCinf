package servlets;

import it.cybion.influencers.crawler.Crawler;
import it.cybion.influencers.crawler.launcher.parsing.ProperitesFileParser;
import utils.PropertiesLoader;

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

    private PropertiesLoader pl;
    private Properties generalProperties;

    public CrawlingLauncher() {

        super();
    }

    @Override
    public void init() throws ServletException {

        this.pl = new PropertiesLoader();

        try {
            this.generalProperties = this.pl.loadGeneralProperties();
        } catch (ServletException e) {
            throw new ServletException("cant load general properties", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //        String crawnkerHome = HomePathGetter.getInstance().getHomePath();
        //        final String generalConfigFilePath = crawnkerHome + "general.config";
        final String configFilePath = request.getParameter("configFilePath");

        //        Properties generalProperties = getProperties(generalConfigFilePath);
        final Properties inputFileProperties = getProperties(configFilePath);
        final Properties merged = merge(this.generalProperties, inputFileProperties);

        final Crawler crawler = ProperitesFileParser.buildCrawlerFromProperties(merged);
        final List<Long> crawledUsers = crawler.getInfluencers();

        //output values
        final String crawlingOutputDirPath = this.pl.getCrawlingOutputDirectory();

        String fileName = request.getParameter("fileName");
        if (fileName == null) {
            fileName = UUID.randomUUID().toString();
        }

        String outputFilePath = crawlingOutputDirPath + fileName;
        File file = new File(outputFilePath);
        if (file.exists()) {
            fileName = UUID.randomUUID().toString();
            outputFilePath = crawlingOutputDirPath + fileName;
        }
        writeCrawledUsersToFile(outputFilePath, crawledUsers);

        request.setAttribute("outputFilePath", outputFilePath);
        request.setAttribute("crawledUsers", crawledUsers);
        request.getRequestDispatcher("crawling-result.jsp").forward(request, response);
    }

    private void writeCrawledUsersToFile(String filePath, List<Long> usersIds)
            throws ServletException {

        File outputFile = new File(filePath);
        FileWriter out = null;
        try {
            out = new FileWriter(outputFile);
        } catch (IOException e) {
            throw new ServletException("cant open filewriter to " + outputFile, e);
        }

        BufferedWriter fileWriter = new BufferedWriter(out);

        try {
            for (Long userId : usersIds) {
                fileWriter.write(userId + "\n");
            }
        } catch (IOException e) {
            throw new ServletException("error writing a userId", e);
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new ServletException("failed writing file", e);
            }
        }

    }

    private Properties merge(Properties generalProperties, Properties inputFileProperties) {

        final Properties totalProperties = new Properties();

        for (Object propertyName : generalProperties.keySet()) {
            totalProperties.put(propertyName, generalProperties.get(propertyName));
        }

        for (Object propertyName : inputFileProperties.keySet()) {
            totalProperties.put(propertyName, inputFileProperties.get(propertyName));
        }

        return totalProperties;
    }

    private Properties getProperties(String filePath) throws IOException {

        final InputStream inputStream = new FileInputStream(new File(filePath));
        final Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
