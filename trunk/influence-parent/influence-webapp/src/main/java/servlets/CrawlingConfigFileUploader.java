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

public class CrawlingConfigFileUploader extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CrawlingConfigFileUploader.class);

    private PropertiesLoader pl;

    private DiskFileItemFactory factory;

    public CrawlingConfigFileUploader() {

        super();
    }

    @Override
    public void init() {
        this.pl = new PropertiesLoader();
        this.factory = new DiskFileItemFactory();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        this.factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        String crawlingConfigDirectory = this.pl.getCrawlingConfigDirectory();
        // Parse the request
        try {
            List<FileItem> fileItems = upload.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                LOGGER.info(fileItem.getName());
                FileItemWriter.writeFileItem(fileItem,
                        crawlingConfigDirectory + fileItem.getName());
            }
        } catch (FileUploadException e) {
            String emsg = "cant upload file";
            throw new ServletException(emsg, e);
        } catch (Exception e) {
            String emsg = "exception";
            throw new ServletException(emsg, e);
        }
    }

}
