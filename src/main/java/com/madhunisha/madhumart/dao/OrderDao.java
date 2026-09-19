package com.madhunisha.madhumart.dao;

import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    public long placeOrder(long userId, String address) throws SQLException, AppException {
        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);
            try {
                List<long[]> lines = new ArrayList<>();
                List<BigDecimal> prices = new ArrayList<>();
                BigDecimal total = BigDecimal.ZERO;

                String cartSql = "SELECT c.product_id, c.quantity, p.price FROM cart_items c "
                               + "JOIN products p ON p.id = c.product_id "
                               + "WHERE c.user_id = ? AND p.active = TRUE";
                try (PreparedStatement ps = con.prepareStatement(cartSql)) {
                    ps.setLong(1, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            long pid = rs.getLong(1);
                            long qty = rs.getInt(2);
                            BigDecimal price = rs.getBigDecimal(3);
                            lines.add(new long[]{pid, qty});
                            prices.add(price);
                            total = total.add(price.multiply(BigDecimal.valueOf(qty)));
                        }
                    }
                }
                if (lines.isEmpty()) {
                    throw new AppException("Your cart is empty");
                }

                long orderId;
                String orderSql = "INSERT INTO orders (buyer_id, total_amount, status, shipping_address) "
                                + "VALUES (?, ?, 'PLACED', ?)";
                try (PreparedStatement ps = con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, userId);
                    ps.setBigDecimal(2, total);
                    ps.setString(3, address);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        orderId = keys.getLong(1);
                    }
                }

                String stockSql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ? AND active = TRUE";
                String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
                for (int i = 0; i < lines.size(); i++) {
                    long pid = lines.get(i)[0];
                    int qty = (int) lines.get(i)[1];
                    try (PreparedStatement ps = con.prepareStatement(stockSql)) {
                        ps.setInt(1, qty);
                        ps.setLong(2, pid);
                        ps.setInt(3, qty);
                        if (ps.executeUpdate() == 0) {
                            throw new AppException("Some item is out of stock. Please update your cart.");
                        }
                    }
                    try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                        ps.setLong(1, orderId);
                        ps.setLong(2, pid);
                        ps.setInt(3, qty);
                        ps.setBigDecimal(4, prices.get(i));
                        ps.executeUpdate();
                    }
                }

                try (PreparedStatement ps = con.prepareStatement("DELETE FROM cart_items WHERE user_id = ?")) {
                    ps.setLong(1, userId);
                    ps.executeUpdate();
                }

                con.commit();
                return orderId;
            } catch (SQLException | AppException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<com.madhunisha.madhumart.model.Order> findByBuyer(long userId) throws SQLException {
        List<com.madhunisha.madhumart.model.Order> orders = new ArrayList<>();
        String orderSql = "SELECT id, total_amount, status, shipping_address, created_at "
                        + "FROM orders WHERE buyer_id = ? ORDER BY id DESC";
        String itemSql = "SELECT oi.product_id, oi.quantity, oi.unit_price, p.name "
                       + "FROM order_items oi JOIN products p ON p.id = oi.product_id "
                       + "WHERE oi.order_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(orderSql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    com.madhunisha.madhumart.model.Order o = new com.madhunisha.madhumart.model.Order();
                    o.setId(rs.getLong("id"));
                    o.setBuyerId(userId);
                    o.setTotalAmount(rs.getBigDecimal("total_amount"));
                    o.setStatus(rs.getString("status"));
                    o.setShippingAddress(rs.getString("shipping_address"));
                    o.setCreatedAt(rs.getTimestamp("created_at"));
                    orders.add(o);
                }
            }
            for (com.madhunisha.madhumart.model.Order o : orders) {
                try (PreparedStatement ips = con.prepareStatement(itemSql)) {
                    ips.setLong(1, o.getId());
                    try (ResultSet irs = ips.executeQuery()) {
                        while (irs.next()) {
                            com.madhunisha.madhumart.model.OrderItem it = new com.madhunisha.madhumart.model.OrderItem();
                            it.setOrderId(o.getId());
                            it.setProductId(irs.getLong("product_id"));
                            it.setQuantity(irs.getInt("quantity"));
                            it.setUnitPrice(irs.getBigDecimal("unit_price"));
                            it.setProductName(irs.getString("name"));
                            o.getItems().add(it);
                        }
                    }
                }
            }
        }
        return orders;
    }
}
