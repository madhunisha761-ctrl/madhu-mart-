package com.madhunisha.madhumart.service;

import com.madhunisha.madhumart.dao.ProductDao;
import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductDao productDao;

    public ProductService() {
        this(new ProductDao());
    }

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> listForSeller(long sellerId) throws AppException {
        try {
            return productDao.findBySeller(sellerId);
        } catch (SQLException e) {
            log.error("Could not list products", e);
            throw new AppException("Could not load products", e);
        }
    }

    public Product getOwned(long productId, long sellerId) throws AppException {
        try {
            Product p = productDao.findById(productId);
            if (p == null || !p.isActive() || p.getSellerId() != sellerId) {
                throw new AppException("Product not found");
            }
            return p;
        } catch (SQLException e) {
            log.error("Could not load product", e);
            throw new AppException("Could not load product", e);
        }
    }

    public void add(long sellerId, String name, String description, String category,
                    String price, String stock, String imageUrl) throws AppException {
        Product p = build(name, description, category, price, stock, imageUrl);
        p.setSellerId(sellerId);
        try {
            productDao.create(p);
            log.info("Product added by seller {}: {}", sellerId, p.getName());
        } catch (SQLException e) {
            log.error("Could not add product", e);
            throw new AppException("Could not add product", e);
        }
    }

    public void update(long productId, long sellerId, String name, String description,
                       String category, String price, String stock, String imageUrl) throws AppException {
        Product p = build(name, description, category, price, stock, imageUrl);
        p.setId(productId);
        p.setSellerId(sellerId);
        try {
            if (!productDao.update(p)) {
                throw new AppException("Product not found");
            }
            log.info("Product {} updated by seller {}", productId, sellerId);
        } catch (SQLException e) {
            log.error("Could not update product", e);
            throw new AppException("Could not update product", e);
        }
    }

    public void delete(long productId, long sellerId) throws AppException {
        try {
            if (!productDao.softDelete(productId, sellerId)) {
                throw new AppException("Product not found");
            }
            log.info("Product {} deleted by seller {}", productId, sellerId);
        } catch (SQLException e) {
            log.error("Could not delete product", e);
            throw new AppException("Could not delete product", e);
        }
    }

    private Product build(String name, String description, String category,
                          String price, String stock, String imageUrl) throws AppException {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException("Product name is required");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new AppException("Category is required");
        }
        BigDecimal priceValue;
        int stockValue;
        try {
            priceValue = new BigDecimal(price.trim());
        } catch (Exception e) {
            throw new AppException("Please enter a valid price");
        }
        if (priceValue.compareTo(BigDecimal.ZERO) <= 0 || priceValue.compareTo(new BigDecimal("99999999")) > 0) {
            throw new AppException("Price must be greater than 0");
        }
        try {
            stockValue = Integer.parseInt(stock.trim());
        } catch (Exception e) {
            throw new AppException("Please enter a valid stock number");
        }
        if (stockValue < 0) {
            throw new AppException("Stock cannot be negative");
        }
        Product p = new Product();
        p.setName(name.trim());
        p.setDescription(description == null ? "" : description.trim());
        p.setCategory(category.trim());
        p.setPrice(priceValue.setScale(2, java.math.RoundingMode.HALF_UP));
        p.setStock(stockValue);
        p.setImageUrl(imageUrl == null ? "" : imageUrl.trim());
        return p;
    }

    public List<Product> search(String keyword, String category) throws AppException {
        try {
            return productDao.search(keyword, category);
        } catch (SQLException e) {
            log.error("Could not search products", e);
            throw new AppException("Could not load products", e);
        }
    }

    public List<String> categories() throws AppException {
        try {
            return productDao.findCategories();
        } catch (SQLException e) {
            log.error("Could not load categories", e);
            throw new AppException("Could not load categories", e);
        }
    }

    public Product getPublic(long productId) throws AppException {
        try {
            Product p = productDao.findById(productId);
            if (p == null || !p.isActive()) {
                throw new AppException("Product not found");
            }
            return p;
        } catch (SQLException e) {
            log.error("Could not load product", e);
            throw new AppException("Could not load product", e);
        }
    }
}
