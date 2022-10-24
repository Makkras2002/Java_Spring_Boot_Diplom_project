package com.makkras.shop.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "products")
public class Product extends CustomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "amount_in_stock")
    private Long amountInStock;

    @Column(name = "picture_path")
    private String picturePath;

    @Column(name = "product_comment")
    private String product_comment;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER, targetEntity = ProductCategory.class)
    @JoinColumn(name = "product_category_id")
    private ProductCategory category;

    @Column(name = "is_available")
    private boolean isAvailable;

    public Product() {
    }

    public Product(String productName, BigDecimal productPrice,
                   Long amountInStock, String picturePath,
                   String product_comment, ProductCategory category,
                   boolean isAvailable) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.amountInStock = amountInStock;
        this.picturePath = picturePath;
        this.product_comment = product_comment;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    public Product(Long productId, String productName,
                   BigDecimal productPrice, Long amountInStock,
                   String picturePath, String product_comment,
                   ProductCategory category, boolean isAvailable) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.amountInStock = amountInStock;
        this.picturePath = picturePath;
        this.product_comment = product_comment;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Long getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(Long amountInStock) {
        this.amountInStock = amountInStock;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getProduct_comment() {
        return product_comment;
    }

    public void setProduct_comment(String product_comment) {
        this.product_comment = product_comment;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (isAvailable != product.isAvailable) return false;
        if (productId != null ? !productId.equals(product.productId) : product.productId != null) return false;
        if (productName != null ? !productName.equals(product.productName) : product.productName != null) return false;
        if (productPrice != null ? !productPrice.equals(product.productPrice) : product.productPrice != null)
            return false;
        if (amountInStock != null ? !amountInStock.equals(product.amountInStock) : product.amountInStock != null)
            return false;
        if (picturePath != null ? !picturePath.equals(product.picturePath) : product.picturePath != null) return false;
        if (product_comment != null ? !product_comment.equals(product.product_comment) : product.product_comment != null)
            return false;
        return category != null ? category.equals(product.category) : product.category == null;
    }

    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (productPrice != null ? productPrice.hashCode() : 0);
        result = 31 * result + (amountInStock != null ? amountInStock.hashCode() : 0);
        result = 31 * result + (picturePath != null ? picturePath.hashCode() : 0);
        result = 31 * result + (product_comment != null ? product_comment.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (isAvailable ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("productId=").append(productId);
        sb.append(", productName='").append(productName).append('\'');
        sb.append(", productPrice=").append(productPrice);
        sb.append(", amountInStock=").append(amountInStock);
        sb.append(", picturePath='").append(picturePath).append('\'');
        sb.append(", product_comment='").append(product_comment).append('\'');
        sb.append(", category=").append(category);
        sb.append(", isAvailable=").append(isAvailable);
        sb.append('}');
        return sb.toString();
    }
}
