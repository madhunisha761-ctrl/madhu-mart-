package com.madhunisha.madhumart.controller;

import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.User;
import com.madhunisha.madhumart.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            User user = userService.login(email, password);

            HttpSession session = req.getSession(false);
            if (session != null) {
                req.changeSessionId();
            } else {
                session = req.getSession(true);
            }
            session.setMaxInactiveInterval(30 * 60);
            session.setAttribute("user", user);

            resp.sendRedirect(req.getContextPath() + "/");
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
