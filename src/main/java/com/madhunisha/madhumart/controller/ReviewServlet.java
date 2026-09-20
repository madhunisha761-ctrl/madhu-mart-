package com.madhunisha.madhumart.controller;

import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.User;
import com.madhunisha.madhumart.service.ReviewService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            long productId = Long.parseLong(req.getParameter("productId"));
            String base = req.getContextPath() + "/product?id=" + productId;
            try {
                int rating = Integer.parseInt(req.getParameter("rating"));
                reviewService.add(user.getId(), productId, rating, req.getParameter("text"));
                resp.sendRedirect(base);
            } catch (NumberFormatException e) {
                resp.sendRedirect(base + "&error=" + URLEncoder.encode("Invalid rating", "UTF-8"));
            } catch (AppException e) {
                resp.sendRedirect(base + "&error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
