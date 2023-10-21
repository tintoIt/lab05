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
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    UserDAO userDAO = new UserDAO();

    @Override
    public void init() throws ServletException {
        sessionFactory = HibernateUtils.getSessionFactory();
        session = sessionFactory.openSession();
        transaction = null;
        System.out.println("Starting Login Servlet!!!");
    }

    @Override
    public void destroy() {
        System.out.println("Deleting Servlet!!!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String rememberPassword = req.getParameter("remember");
        try {
            User user = userDAO.findByUsername(username);
            if (user != null) {
                HttpSession session = req.getSession();
                if (user.getPassword().equals(password)) {
                    System.out.println("Loggin successfully");
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("username", user.getName());
                    if (rememberPassword != null) {
                        Cookie userId = new Cookie("userId", user.getId()+"");
                        userId.setMaxAge(30*60*60*24);
                        res.addCookie(userId);
                    }
                    res.sendRedirect("/Lab05/Products");
                }
                else {
                    session.setAttribute("flashMessage", "Incorrect password. Please try again.");
                    req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
                }
            }
            else {
                System.out.println("Loggin failed");
                res.sendRedirect("/Lab05/Register");
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
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
    }
}
