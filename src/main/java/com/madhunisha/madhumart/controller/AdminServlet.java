package com.madhunisha.madhumart.controller;

import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.service.AdminService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/panel")
public class AdminServlet extends HttpServlet {

    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        show(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            long id = Long.parseLong(req.getParameter("id"));
            if ("disableUser".equals(action)) {
                adminService.setUserActive(id, false);
            } else if ("enableUser".equals(action)) {
                adminService.setUserActive(id, true);
            } else if ("hideProduct".equals(action)) {
                adminService.setProductActive(id, false);
            } else if ("showProduct".equals(action)) {
                adminService.setProductActive(id, true);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/panel");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            show(req, resp);
        }
    }

    private void show(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("users", adminService.users());
            req.setAttribute("orders", adminService.orders());
            req.setAttribute("products", adminService.products());
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }
}
