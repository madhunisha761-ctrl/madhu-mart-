package com.madhunisha.madhumart.controller;

import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.CartItem;
import com.madhunisha.madhumart.model.User;
import com.madhunisha.madhumart.service.CartService;
import com.madhunisha.madhumart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        show(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String address = req.getParameter("address");
        try {
            long orderId = orderService.placeOrder(user.getId(), address);
            resp.sendRedirect(req.getContextPath() + "/checkout?done=" + orderId);
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("address", address);
            show(req, resp);
        }
    }

    private void show(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            List<CartItem> items = cartService.getCart(user.getId());
            req.setAttribute("items", items);
            req.setAttribute("total", cartService.total(items));
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }
}
