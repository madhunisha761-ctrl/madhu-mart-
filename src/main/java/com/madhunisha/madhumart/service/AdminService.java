package com.madhunisha.madhumart.service;

import com.madhunisha.madhumart.dao.AdminDao;
import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.Order;
import com.madhunisha.madhumart.model.Product;
import com.madhunisha.madhumart.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final AdminDao adminDao;

    public AdminService() {
        this(new AdminDao());
    }

    public AdminService(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    public List<User> users() throws AppException {
        try {
            return adminDao.allUsers();
        } catch (SQLException e) {
            log.error("Could not load users", e);
            throw new AppException("Could not load users", e);
        }
    }

    public List<Order> orders() throws AppException {
        try {
            return adminDao.allOrders();
        } catch (SQLException e) {
            log.error("Could not load orders", e);
            throw new AppException("Could not load orders", e);
        }
    }

    public List<Product> products() throws AppException {
        try {
            return adminDao.allProducts();
        } catch (SQLException e) {
            log.error("Could not load listings", e);
            throw new AppException("Could not load listings", e);
        }
    }

    public void setUserActive(long userId, boolean active) throws AppException {
        try {
            adminDao.setUserActive(userId, active);
            log.info("Admin set user {} active={}", userId, active);
        } catch (SQLException e) {
            log.error("Could not update user", e);
            throw new AppException("Could not update user", e);
        }
    }

    public void setProductActive(long productId, boolean active) throws AppException {
        try {
            adminDao.setProductActive(productId, active);
            log.info("Admin set product {} active={}", productId, active);
        } catch (SQLException e) {
            log.error("Could not update listing", e);
            throw new AppException("Could not update listing", e);
        }
    }
}
