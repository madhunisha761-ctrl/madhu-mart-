package com.madhunisha.madhumart.service;

import com.madhunisha.madhumart.dao.ReviewDao;
import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewDao reviewDao;

    public ReviewService() {
        this(new ReviewDao());
    }

    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    public List<Review> forProduct(long productId) throws AppException {
        try {
            return reviewDao.findByProduct(productId);
        } catch (SQLException e) {
            log.error("Could not load reviews", e);
            throw new AppException("Could not load reviews", e);
        }
    }

    public double average(long productId) throws AppException {
        try {
            return reviewDao.average(productId);
        } catch (SQLException e) {
            throw new AppException("Could not load rating", e);
        }
    }

    public boolean canReview(long userId, long productId) throws AppException {
        try {
            return reviewDao.hasCompletedOrder(userId, productId)
                    && !reviewDao.exists(userId, productId);
        } catch (SQLException e) {
            throw new AppException("Could not check review eligibility", e);
        }
    }

    public void add(long userId, long productId, int rating, String text) throws AppException {
        if (rating < 1 || rating > 5) {
            throw new AppException("Rating must be between 1 and 5");
        }
        if (text != null && text.length() > 1000) {
            throw new AppException("Review is too long");
        }
        try {
            if (!reviewDao.hasCompletedOrder(userId, productId)) {
                throw new AppException("You can review only products from completed orders");
            }
            if (reviewDao.exists(userId, productId)) {
                throw new AppException("You already reviewed this product");
            }
            Review r = new Review();
            r.setUserId(userId);
            r.setProductId(productId);
            r.setRating(rating);
            r.setReviewText(text == null ? "" : text.trim());
            reviewDao.create(r);
            log.info("User {} reviewed product {} with {} stars", userId, productId, rating);
        } catch (SQLException e) {
            log.error("Could not save review", e);
            throw new AppException("Could not save review", e);
        }
    }
}
