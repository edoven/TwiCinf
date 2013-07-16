package servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import utils.FileItemWriter;
import utils.PropertiesLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class FileUploaderServlet
 */
public class TweetsListUploader extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(TweetsListUploader.class);

    private PropertiesLoader pl;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TweetsListUploader() {

        super();
    }

    @Override
    public void init() {

        this.pl = new PropertiesLoader();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        //        String crawnkerHome = HomePathGetter.getInstance().getHomePath();

        final String rankingTopicDirectory = this.pl.getRankingTopicListDirectory();

        // Parse the request
        try {
            List<FileItem> fileItems = upload.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                LOGGER.info(fileItem.getName());
                FileItemWriter.writeFileItem(fileItem, rankingTopicDirectory + fileItem.getName());
            }
        } catch (FileUploadException e) {
            String emsg = "failed uploading file";
            throw new ServletException(emsg, e);
        } catch (Exception e) {
            String emsg = "failed uploading file";
            throw new ServletException(emsg, e);
        }
    }

}
