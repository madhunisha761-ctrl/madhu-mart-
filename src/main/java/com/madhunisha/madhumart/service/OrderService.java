package com.madhunisha.madhumart.service;

import com.madhunisha.madhumart.dao.OrderDao;
import com.madhunisha.madhumart.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderDao orderDao;

    public OrderService() {
        this(new OrderDao());
    }

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public long placeOrder(long userId, String address) throws AppException {
        if (address == null || address.trim().length() < 10) {
            throw new AppException("Please enter a full shipping address (at least 10 characters)");
        }
        try {
            long orderId = orderDao.placeOrder(userId, address.trim());
            log.info("Order {} placed by user {}", orderId, userId);
            return orderId;
        } catch (SQLException e) {
            log.error("Could not place order", e);
            throw new AppException("Could not place order. Please try again.", e);
        }
    }
}
