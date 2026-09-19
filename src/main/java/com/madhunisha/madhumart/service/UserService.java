package com.madhunisha.madhumart.service;

import com.madhunisha.madhumart.dao.UserDao;
import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.User;
import com.madhunisha.madhumart.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;

    public UserService() {
        this(new UserDao());
    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User register(String name, String email, String password, String role) throws AppException {
        if (isBlank(name) || isBlank(email) || isBlank(password)) {
            throw new AppException("Name, email and password are required");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new AppException("Please enter a valid email address");
        }
        if (password.length() < 8) {
            throw new AppException("Password must be at least 8 characters");
        }
        if (!"BUYER".equals(role) && !"SELLER".equals(role)) {
            throw new AppException("Please choose Buyer or Seller");
        }
        try {
            String cleanEmail = email.trim().toLowerCase();
            if (userDao.findByEmail(cleanEmail) != null) {
                throw new AppException("Email already registered");
            }
            User user = new User();
            user.setName(name.trim());
            user.setEmail(cleanEmail);
            user.setPasswordHash(PasswordUtil.hash(password));
            user.setRole(role);
            user.setId(userDao.create(user));
            user.setActive(true);
            log.info("New user registered: {}", cleanEmail);
            return user;
        } catch (SQLException e) {
            log.error("Registration failed", e);
            throw new AppException("Could not register. Please try again.", e);
        }
    }

    public User login(String email, String password) throws AppException {
        if (isBlank(email) || isBlank(password)) {
            throw new AppException("Email and password are required");
        }
        try {
            User user = userDao.findByEmail(email.trim().toLowerCase());
            if (user == null || !PasswordUtil.verify(password, user.getPasswordHash())) {
                throw new AppException("Invalid email or password");
            }
            if (!user.isActive()) {
                throw new AppException("This account is disabled");
            }
            log.info("User logged in: {}", user.getEmail());
            return user;
        } catch (SQLException e) {
            log.error("Login failed", e);
            throw new AppException("Could not login. Please try again.", e);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
