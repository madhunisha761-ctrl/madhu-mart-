package com.madhunisha.madhumart.controller;

import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.User;
import com.madhunisha.madhumart.service.ProductService;
import com.madhunisha.madhumart.service.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/product")
public class ProductDetailServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            req.setAttribute("product", productService.getPublic(id));
            req.setAttribute("reviews", reviewService.forProduct(id));
            req.setAttribute("average", reviewService.average(id));
            HttpSession session = req.getSession(false);
            User user = session == null ? null : (User) session.getAttribute("user");
            req.setAttribute("canReview", user != null && reviewService.canReview(user.getId(), id));
            req.getRequestDispatcher("/WEB-INF/views/product.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (AppException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
