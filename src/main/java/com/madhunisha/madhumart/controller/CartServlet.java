package com.madhunisha.madhumart.controller;

import com.google.gson.JsonObject;
import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.CartItem;
import com.madhunisha.madhumart.model.User;
import com.madhunisha.madhumart.service.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        showCart(req, resp, user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        boolean ajax = "1".equals(req.getParameter("ajax"));
        String error = null;
        try {
            long productId = Long.parseLong(req.getParameter("productId"));
            if ("add".equals(action)) {
                cartService.add(user.getId(), productId, 1);
            } else if ("update".equals(action)) {
                cartService.update(user.getId(), productId,
                        Integer.parseInt(req.getParameter("quantity")));
            } else if ("remove".equals(action)) {
                cartService.remove(user.getId(), productId);
            }
        } catch (NumberFormatException e) {
            error = "Invalid request";
        } catch (AppException e) {
            error = e.getMessage();
        }
        if (ajax) {
            JsonObject json = new JsonObject();
            json.addProperty("ok", error == null);
            json.addProperty("message", error == null ? "Added to cart" : error);
            try {
                json.addProperty("count", cartService.count(user.getId()));
            } catch (AppException e) {
                json.addProperty("count", 0);
            }
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(json.toString());
        } else if (error == null) {
            resp.sendRedirect(req.getContextPath() + "/cart");
        } else {
            req.setAttribute("error", error);
            showCart(req, resp, user);
        }
    }

    private void showCart(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            List<CartItem> items = cartService.getCart(user.getId());
            req.setAttribute("items", items);
            req.setAttribute("total", cartService.total(items));
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
    }
}
