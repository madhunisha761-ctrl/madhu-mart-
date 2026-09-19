package com.madhunisha.madhumart.dao;

import com.madhunisha.madhumart.model.Product;
import com.madhunisha.madhumart.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private static final String SELECT_BASE =
            "SELECT p.*, u.name AS seller_name FROM products p "
          + "JOIN users u ON u.id = p.seller_id ";

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setSellerName(rs.getString("seller_name"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setCategory(rs.getString("category"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setActive(rs.getBoolean("active"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }

    public List<Product> findBySeller(long sellerId) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.seller_id = ? AND p.active = TRUE ORDER BY p.id DESC";
        List<Product> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    public Product findById(long id) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public long create(Product p) throws SQLException {
        String sql = "INSERT INTO products (seller_id, name, description, category, price, stock, image_url) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, p.getSellerId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription());
            ps.setString(4, p.getCategory());
            ps.setBigDecimal(5, p.getPrice());
            ps.setInt(6, p.getStock());
            ps.setString(7, p.getImageUrl());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getLong(1) : -1;
            }
        }
    }

    public boolean update(Product p) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, category = ?, price = ?, "
                   + "stock = ?, image_url = ? WHERE id = ? AND seller_id = ? AND active = TRUE";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setString(3, p.getCategory());
            ps.setBigDecimal(4, p.getPrice());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getImageUrl());
            ps.setLong(7, p.getId());
            ps.setLong(8, p.getSellerId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean softDelete(long productId, long sellerId) throws SQLException {
        String sql = "UPDATE products SET active = FALSE WHERE id = ? AND seller_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, productId);
            ps.setLong(2, sellerId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Product> search(String keyword, String category) throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT_BASE + "WHERE p.active = TRUE ");
        List<String> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String like = "%" + keyword.trim().toLowerCase() + "%";
            params.add(like);
            params.add(like);
        }
        if (category != null && !category.trim().isEmpty()) {
            sql.append("AND p.category = ? ");
            params.add(category.trim());
        }
        sql.append("ORDER BY p.id DESC");
        List<Product> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    public List<String> findCategories() throws SQLException {
        String sql = "SELECT DISTINCT category FROM products WHERE active = TRUE ORDER BY category";
        List<String> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        }
        return list;
    }
}
