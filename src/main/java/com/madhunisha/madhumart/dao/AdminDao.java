package com.madhunisha.madhumart.dao;

import com.madhunisha.madhumart.model.Order;
import com.madhunisha.madhumart.model.Product;
import com.madhunisha.madhumart.model.User;
import com.madhunisha.madhumart.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {

    public List<User> allUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, name, email, role, active, created_at FROM users ORDER BY id";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setActive(rs.getBoolean("active"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(u);
            }
        }
        return list;
    }

    public List<Order> allOrders() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.id, o.buyer_id, u.name, o.total_amount, o.status, o.created_at "
                   + "FROM orders o JOIN users u ON u.id = o.buyer_id ORDER BY o.id DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getLong("id"));
                o.setBuyerId(rs.getLong("buyer_id"));
                o.setBuyerName(rs.getString("name"));
                o.setTotalAmount(rs.getBigDecimal("total_amount"));
                o.setStatus(rs.getString("status"));
                o.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(o);
            }
        }
        return list;
    }

    public List<Product> allProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.category, p.price, p.stock, p.active, u.name AS seller_name "
                   + "FROM products p JOIN users u ON u.id = p.seller_id ORDER BY p.id DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                p.setCategory(rs.getString("category"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setStock(rs.getInt("stock"));
                p.setActive(rs.getBoolean("active"));
                p.setSellerName(rs.getString("seller_name"));
                list.add(p);
            }
        }
        return list;
    }

    public void setUserActive(long userId, boolean active) throws SQLException {
        String sql = "UPDATE users SET active = ? WHERE id = ? AND role <> 'ADMIN'";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public void setProductActive(long productId, boolean active) throws SQLException {
        String sql = "UPDATE products SET active = ? WHERE id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setLong(2, productId);
            ps.executeUpdate();
        }
    }
}
