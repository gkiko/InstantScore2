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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parsing.ConfigObject;
import subscribtion.Result;
import subscribtion.SubscribtionManager;

/**
 * Servlet implementation class DataProvider
 */
@WebServlet("/DataProvider")
public class DataProvider extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataProvider.class);
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
		Result res = null;
		SubscribtionManager subscribtionManager;
		String requestType, phoneNum, code, matchId;
		requestType = request.getParameter("type");
		subscribtionManager = (SubscribtionManager) getServletContext().getAttribute("subscribtion_manager");
		
		phoneNum = request.getParameter("phone_num");
		if(requestType.equals("new_code")){
			res = subscribtionManager.fulfilCodeRequest(phoneNum);
		}else{
			if(requestType.equals("submit_game")){
				code = request.getParameter("security_code");
				matchId = request.getParameter("match_id");
				res = subscribtionManager.fulfilSubscribtionRequest(phoneNum, code, matchId);
			}
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		if(!res.isValid()){
			LOGGER.debug("operation invalid");
			response.getWriter().write(res.getErrorMessage());
		}else{
			response.getWriter().write("operation successful");
		}
		response.getWriter().flush();
		response.getWriter().close();
	}

}
