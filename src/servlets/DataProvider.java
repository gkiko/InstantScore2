package servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import parsing.ConfigObject;

/**
 * Servlet implementation class DataProvider
 */
@WebServlet("/DataProvider")
public class DataProvider extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataProvider() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File f;
		String fileName, fileParameter;
		
		ConfigObject config = (ConfigObject) getServletContext().getAttribute("config");
		
		fileParameter = request.getParameter("type");
		fileName = config.getFileName(fileParameter);
//		System.out.println(fileParameter+" "+fileName);
		f = new File(fileName);
		FileInputStream in = new FileInputStream(f);
		IOUtils.copy(in, response.getOutputStream());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestType;
		requestType = request.getParameter("type");
		
		if(requestType.equals("new_code")){
			
		}else{
			if(requestType.equals("submit_game")){
				
			}
		}
	}

}
