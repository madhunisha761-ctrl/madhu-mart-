package com.madhunisha.madhumart.dao;

import com.madhunisha.madhumart.model.Review;
import com.madhunisha.madhumart.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {

    public List<Review> findByProduct(long productId) throws SQLException {
        String sql = "SELECT r.id, r.user_id, r.rating, r.review_text, r.created_at, u.name "
                   + "FROM reviews r JOIN users u ON u.id = r.user_id "
                   + "WHERE r.product_id = ? ORDER BY r.id DESC";
        List<Review> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getLong("id"));
                    r.setProductId(productId);
                    r.setUserId(rs.getLong("user_id"));
                    r.setUserName(rs.getString("name"));
                    r.setRating(rs.getInt("rating"));
                    r.setReviewText(rs.getString("review_text"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(r);
                }
            }
        }
        return list;
    }

    public double average(long productId) throws SQLException {
        String sql = "SELECT COALESCE(AVG(rating), 0) FROM reviews WHERE product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        }
    }

    public boolean hasCompletedOrder(long userId, long productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders o JOIN order_items oi ON oi.order_id = o.id "
                   + "WHERE o.buyer_id = ? AND oi.product_id = ? AND o.status = 'COMPLETED'";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean exists(long userId, long productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reviews WHERE user_id = ? AND product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void create(Review r) throws SQLException {
        String sql = "INSERT INTO reviews (product_id, user_id, rating, review_text) VALUES (?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, r.getProductId());
            ps.setLong(2, r.getUserId());
            ps.setInt(3, r.getRating());
            ps.setString(4, r.getReviewText());
            ps.executeUpdate();
        }
    }
}
