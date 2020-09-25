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
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/list-user")
public class ListUserServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ListUserServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("==============>>  run doGet");
        List<User> userList = new UserDao().findAllUser();
        List<String> userNames = userList.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        req.setAttribute("userNames", userNames);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/list-user.jsp");
        requestDispatcher.forward(req, resp);
    }
}