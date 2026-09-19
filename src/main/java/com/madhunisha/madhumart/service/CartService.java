package com.madhunisha.madhumart.service;

import com.madhunisha.madhumart.dao.CartDao;
import com.madhunisha.madhumart.dao.ProductDao;
import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.CartItem;
import com.madhunisha.madhumart.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartDao cartDao;
    private final ProductDao productDao;

    public CartService() {
        this(new CartDao(), new ProductDao());
    }

    public CartService(CartDao cartDao, ProductDao productDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
    }

    public List<CartItem> getCart(long userId) throws AppException {
        try {
            return cartDao.findByUser(userId);
        } catch (SQLException e) {
            log.error("Could not load cart", e);
            throw new AppException("Could not load cart", e);
        }
    }

    public BigDecimal total(List<CartItem> items) {
        BigDecimal sum = BigDecimal.ZERO;
        for (CartItem i : items) {
            sum = sum.add(i.getSubtotal());
        }
        return sum;
    }

    public int count(long userId) throws AppException {
        try {
            return cartDao.count(userId);
        } catch (SQLException e) {
            throw new AppException("Could not load cart", e);
        }
    }

    public void add(long userId, long productId, int qty) throws AppException {
        if (qty < 1) {
            throw new AppException("Quantity must be at least 1");
        }
        try {
            Product p = productDao.findById(productId);
            if (p == null || !p.isActive()) {
                throw new AppException("Product not found");
            }
            if (p.getSellerId() == userId) {
                throw new AppException("You cannot buy your own product");
            }
            int already = 0;
            for (CartItem c : cartDao.findByUser(userId)) {
                if (c.getProductId() == productId) {
                    already = c.getQuantity();
                }
            }
            if (already + qty > p.getStock()) {
                throw new AppException("Only " + p.getStock() + " in stock");
            }
            cartDao.add(userId, productId, qty);
        } catch (SQLException e) {
            log.error("Could not add to cart", e);
            throw new AppException("Could not add to cart", e);
        }
    }

    public void update(long userId, long productId, int qty) throws AppException {
        try {
            if (qty < 1) {
                cartDao.remove(userId, productId);
                return;
            }
            Product p = productDao.findById(productId);
            if (p == null || !p.isActive()) {
                throw new AppException("Product not found");
            }
            if (qty > p.getStock()) {
                throw new AppException("Only " + p.getStock() + " in stock");
            }
            cartDao.setQuantity(userId, productId, qty);
        } catch (SQLException e) {
            log.error("Could not update cart", e);
            throw new AppException("Could not update cart", e);
        }
    }

    public void remove(long userId, long productId) throws AppException {
        try {
            cartDao.remove(userId, productId);
        } catch (SQLException e) {
            throw new AppException("Could not remove item", e);
        }
    }
}
