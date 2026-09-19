package com.madhunisha.madhumart.dao;

import com.madhunisha.madhumart.model.CartItem;
import com.madhunisha.madhumart.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDao {

    public List<CartItem> findByUser(long userId) throws SQLException {
        String sql = "SELECT c.id, c.user_id, c.product_id, c.quantity, p.name, p.price "
                   + "FROM cart_items c JOIN products p ON p.id = c.product_id "
                   + "WHERE c.user_id = ? AND p.active = TRUE ORDER BY c.id";
        List<CartItem> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem c = new CartItem();
                    c.setId(rs.getLong("id"));
                    c.setUserId(rs.getLong("user_id"));
                    c.setProductId(rs.getLong("product_id"));
                    c.setQuantity(rs.getInt("quantity"));
                    c.setProductName(rs.getString("name"));
                    c.setPrice(rs.getBigDecimal("price"));
                    list.add(c);
                }
            }
        }
        return list;
    }

    public void add(long userId, long productId, int qty) throws SQLException {
        String upd = "UPDATE cart_items SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
        String ins = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection()) {
            int rows;
            try (PreparedStatement ps = con.prepareStatement(upd)) {
                ps.setInt(1, qty);
                ps.setLong(2, userId);
                ps.setLong(3, productId);
                rows = ps.executeUpdate();
            }
            if (rows == 0) {
                try (PreparedStatement ps = con.prepareStatement(ins)) {
                    ps.setLong(1, userId);
                    ps.setLong(2, productId);
                    ps.setInt(3, qty);
                    ps.executeUpdate();
                }
            }
        }
    }

    public void setQuantity(long userId, long productId, int qty) throws SQLException {
        String sql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setLong(2, userId);
            ps.setLong(3, productId);
            ps.executeUpdate();
        }
    }

    public void remove(long userId, long productId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.executeUpdate();
        }
    }

    public int count(long userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE user_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
