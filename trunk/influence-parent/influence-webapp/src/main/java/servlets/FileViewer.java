package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Servlet implementation class FileViewer
 */
public class FileViewer extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public FileViewer() {

        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filePath = request.getParameter("file");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line = bufferedReader.readLine();

        while (line != null) {
            response.getWriter().println(line);
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
    }

}
