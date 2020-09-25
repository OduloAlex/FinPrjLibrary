package app.ui;

import app.dao.UserDao;
import app.domain.User;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/sign-in")
public class SignInServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(SignInServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("==============>>  run doGet");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/sign-in.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("==============>>   run doPost");
        String name = req.getParameter("name");
        String password = req.getParameter("pass");
        User user = new UserDao().findUserByLogin(name);
        PrintWriter writer = resp.getWriter();
        if (user==null){
            req.setAttribute("result", "not_found");
        } else {
            if (password.equals(user.getPassword())) {
                req.setAttribute("result", "ok");
            } else {
                req.setAttribute("result", "access_denied");
            }
        }
        req.setAttribute("user_name", name);
        doGet(req, resp);
    }
}