package servlets;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FilesDeleter extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    public FilesDeleter() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filePath = request.getParameter("file");
		File file = new File(filePath);
		file.delete();
	}

}
