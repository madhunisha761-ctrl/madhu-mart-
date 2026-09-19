package com.madhunisha.madhumart.controller;

import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.User;
import com.madhunisha.madhumart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/seller/products")
public class SellerProductServlet extends HttpServlet {

    private static final String LIST_VIEW = "/WEB-INF/views/seller-products.jsp";
    private static final String FORM_VIEW = "/WEB-INF/views/seller-product-form.jsp";

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        try {
            if ("new".equals(action)) {
                req.getRequestDispatcher(FORM_VIEW).forward(req, resp);
            } else if ("edit".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                req.setAttribute("product", productService.getOwned(id, user.getId()));
                req.getRequestDispatcher(FORM_VIEW).forward(req, resp);
            } else {
                showList(req, resp, user);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            showList(req, resp, user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        String idText = req.getParameter("id");
        try {
            if ("delete".equals(action)) {
                productService.delete(Long.parseLong(idText), user.getId());
            } else {
                String name = req.getParameter("name");
                String description = req.getParameter("description");
                String category = req.getParameter("category");
                String price = req.getParameter("price");
                String stock = req.getParameter("stock");
                String imageUrl = req.getParameter("imageUrl");
                if (idText == null || idText.isEmpty()) {
                    productService.add(user.getId(), name, description, category, price, stock, imageUrl);
                } else {
                    productService.update(Long.parseLong(idText), user.getId(), name, description,
                            category, price, stock, imageUrl);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/seller/products");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            if ("delete".equals(action)) {
                showList(req, resp, user);
            } else {
                req.getRequestDispatcher(FORM_VIEW).forward(req, resp);
            }
        }
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            req.setAttribute("products", productService.listForSeller(user.getId()));
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher(LIST_VIEW).forward(req, resp);
    }
}
