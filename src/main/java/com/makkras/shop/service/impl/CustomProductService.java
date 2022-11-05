package com.makkras.shop.service.impl;

import com.makkras.shop.entity.Product;
import com.makkras.shop.entity.ProductCategory;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.repo.CategoryJpaRepository;
import com.makkras.shop.repo.ProductJpaRepository;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomProductService implements ProductService {
    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final FileUploadUtil fileUploadUtil;

    @Autowired
    public CustomProductService(ProductJpaRepository productJpaRepository,
                                CategoryJpaRepository categoryJpaRepository,
                                FileUploadUtil fileUploadUtil) {
        this.productJpaRepository = productJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
        this.fileUploadUtil = fileUploadUtil;
    }

    @Override
    public void addProduct(Product product) {
        if(!categoryJpaRepository
                .existsByCategoryIdOrCategory(product.getCategory().getCategoryId(),
                        product.getCategory().getCategory())) {
            categoryJpaRepository.save(product.getCategory());
        } else {
            product.setCategory(categoryJpaRepository.findByCategory(product.getCategory().getCategory()).get());
        }
        productJpaRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productJpaRepository.findAll();
    }

    @Override
    public List<Product> getAllAvailableAndInStockProducts() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailable(0L,true);
    }

    @Override
    public List<ProductCategory> getAllProductCategories() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public List<Product> getAllAvailableAndInStockProductsAndOrderByPriceAsc() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductPriceAsc(0L,true);
    }

    @Override
    public List<Product> getAllAvailableAndInStockProductsAndOrderByPriceDesc() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductPriceDesc(0L,true);
    }

    @Override
    public List<Product> getAllAvailableAndInStockProductsAndOrderByName() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductName(0L,true);
    }

    @Override
    public List<Product> getAllAvailableAndInStockProductsAndOrderByCategory() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailableOrderByCategoryCategory(0L,true);
    }

    @Override
    public List<Product> getAllProductsAndOrderByPriceAsc() {
        return productJpaRepository.findAllByOrderByProductPriceAsc();
    }

    @Override
    public List<Product> getAllProductsAndOrderByPriceDesc() {
        return productJpaRepository.findAllByOrderByProductPriceDesc();
    }

    @Override
    public List<Product> getAllProductsAndOrderByName() {
        return productJpaRepository.findAllByOrderByProductName();
    }

    @Override
    public List<Product> getAllProductsAndOrderByCategory() {
        return productJpaRepository.findAllByOrderByCategoryCategory();
    }

    @Override
    public List<Product> getAllProductsAndOrderByIsAvailable() {
        return productJpaRepository.findAllByOrderByIsAvailableDesc();
    }

    public boolean updateProductData(Product updatedProduct, String productPictureLocationDir, MultipartFile pictureFile) throws CustomServiceException {
        Long productForUpdateId = updatedProduct.getProductId();
        Optional<Product> oldProductOptional = productJpaRepository.findById(productForUpdateId);
        if(oldProductOptional.isPresent()) {
            Product oldProduct = oldProductOptional.get();
            if(!updatedProduct.getProductName().equals(oldProduct.getProductName())) {
                productJpaRepository.updateProductName(updatedProduct.getProductName(), productForUpdateId);
            }
            if(updatedProduct.getProductPrice().compareTo(oldProduct.getProductPrice())!= 0) {
                productJpaRepository.updateProductPrice(updatedProduct.getProductPrice(),productForUpdateId);
            }
            if(updatedProduct.getAmountInStock() != 0) {
                productJpaRepository.updateProductAmountInStock(oldProduct.getAmountInStock() + updatedProduct.getAmountInStock(), productForUpdateId);
            }
            if(!updatedProduct.getPicturePath().isBlank() && !updatedProduct.getPicturePath().equals(oldProduct.getPicturePath())) {
                try {
                    fileUploadUtil.saveFile(productPictureLocationDir, updatedProduct.getPicturePath(), pictureFile);
                    productJpaRepository.updateProductPicturePath(updatedProduct.getPicturePath(),productForUpdateId);
                } catch (IOException exception) {
                    throw new CustomServiceException(exception.getMessage());
                }
            }
            if(!updatedProduct.getProductComment().equals(oldProduct.getProductComment())) {
                productJpaRepository.updateProductComment(updatedProduct.getProductComment(), productForUpdateId);
            }
            if(updatedProduct.isAvailable() != oldProduct.isAvailable()) {
                productJpaRepository.updateProductAvailability(updatedProduct.isAvailable(),productForUpdateId);
            }
            if(!updatedProduct.getCategory().getCategory().equals(oldProduct.getCategory().getCategory())) {
                if(!categoryJpaRepository.existsByCategoryIdOrCategory(updatedProduct.getCategory().getCategoryId(),updatedProduct.getCategory().getCategory())) {
                    ProductCategory newCategory;
                    newCategory =  categoryJpaRepository.save(updatedProduct.getCategory());
                    productJpaRepository.updateProductCategory(categoryJpaRepository.
                            findById(newCategory.getCategoryId()).
                            orElse(oldProduct.getCategory()).getCategoryId(),productForUpdateId);
                } else {
                    productJpaRepository.updateProductCategory(categoryJpaRepository.
                            findByCategory(updatedProduct.getCategory().getCategory()).
                            orElse(oldProduct.getCategory()).getCategoryId(),productForUpdateId);
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
