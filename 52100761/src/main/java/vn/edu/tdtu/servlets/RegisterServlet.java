// Ex1

package vn.edu.tdtu.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import vn.edu.tdtu.dao.UserDAO;
import vn.edu.tdtu.model.User;
import vn.edu.tdtu.utils.HibernateUtils;

import java.io.IOException;

@WebServlet("/Register")
public class RegisterServlet extends HttpServlet {
    SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    UserDAO userDAO = new UserDAO();

    @Override
    public void init() throws ServletException {
        sessionFactory = HibernateUtils.getSessionFactory();
        session = sessionFactory.openSession();
        transaction = null;
        System.out.println("Starting Register Servlet!!!");
    }

    @Override
    public void destroy() {
        System.out.println("Deleting Servlet!!!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        res.setContentType("text/html");
        String username = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String passwordCfm = req.getParameter("password-cfm");
        if (!password.equals(passwordCfm)) {
            session.setAttribute("flashMessage", "Password and Password confirm does not match!");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
            return;
        }
        try {
            User user = new User(username, email, password);
            Long id = userDAO.add(user);
            if (id != null) {
                session.setAttribute("userId", id);
                session.setAttribute("username", username);
                res.sendRedirect("/Lab05/Products");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            res.sendRedirect("/Lab05/Products");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
    }
}
