// Ex1

package vn.edu.tdtu.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import vn.edu.tdtu.dao.ProductDAO;
import vn.edu.tdtu.dao.UserDAO;
import vn.edu.tdtu.model.Product;
import vn.edu.tdtu.model.User;
import vn.edu.tdtu.utils.HibernateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/Products")
public class ProductServlet extends HttpServlet {

    SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    ProductDAO productDAO = new ProductDAO();

    @Override
    public void init() throws ServletException {
        sessionFactory = HibernateUtils.getSessionFactory();
        session = sessionFactory.openSession();
        transaction = null;
        System.out.println("Starting Product Servlet!!!");
    }

    @Override
    public void destroy() {
        System.out.println("Deleting Servlet!!!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        System.out.println(price);
        if (name == null) {
            HttpSession session = req.getSession();
            session.setAttribute("flashMessage", "Vui lòng nhập tên sản phẩm!");
            req.getRequestDispatcher("index.jsp").forward(req, res);
        }
        try {
            Product product = new Product(name, Double.valueOf(price));
            Long id = productDAO.add(product);
            if (id != null) {
                res.sendRedirect("/Lab05/Products");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect("/Lab05/Login");
            return;
        }
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            String productIdParam = req.getParameter("id");

            if (productIdParam == null || productIdParam.isEmpty()) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid productId");
                return;
            }

            Long id = Long.valueOf(productIdParam);
            productDAO.removeById(id);
            res.sendRedirect("/Lab05/Products");
            return;
        }
        List<Product> products = productDAO.getAll();
        req.setAttribute("products", products);
        req.getRequestDispatcher("index.jsp").forward(req, res);
    }
}
