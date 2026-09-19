package com.madhunisha.madhumart.controller;

import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String q = req.getParameter("q");
        String category = req.getParameter("category");
        try {
            req.setAttribute("products", productService.search(q, category));
            req.setAttribute("categories", productService.categories());
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.setAttribute("q", q);
        req.setAttribute("selectedCategory", category);
        req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
    }
}
