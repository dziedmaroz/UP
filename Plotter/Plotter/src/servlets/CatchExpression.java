package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eval.Eval;

/**
 * Servlet implementation class CatchExpression
 */
@WebServlet("/catch")
public class CatchExpression extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CatchExpression() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	if (request.getParameter("expr")!=null)
	    	{
	    	    Eval eval = new Eval(request.getParameter("expr"));
	    	    request.setAttribute("allOk", eval.tryParse());
	    	    if (eval.tryParse())
	    	    {
	    		request.setAttribute("plot", eval.getTable());
	    	    }
	    	    else
	    	    {
	    		request.setAttribute("err", eval.getErr());
	    	    }
	    	    request.getRequestDispatcher("/plot.jsp").forward(request, response);	    		
	    	}
	    	else
	    	{
	    	 request.getRequestDispatcher("/plot.jsp").forward(request, response);	 
	    	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
