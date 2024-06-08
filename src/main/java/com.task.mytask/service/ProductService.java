package com.task.mytask.service;

import com.task.mytask.dao.ProductDAO;
import com.task.mytask.dao.SellerDAO;
import com.task.mytask.entitiy.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductDAO productDAO;
    private final SellerDAO sellerDAO;

    @Autowired
    public ProductService(ProductDAO productDAO, SellerDAO sellerDAO) {
        this.productDAO = productDAO;
        this.sellerDAO = sellerDAO;
    }

    public boolean addProduct(Product product, String sellerId) {
        if(!sellerDAO.exists(sellerId)) {
            throw new IllegalArgumentException("Seller " + product.getSellerId() + "does not exist");
        }
        sellerDAO.addProductToCatalog(sellerId, product);
        return productDAO.addProduct(product);
    }
}
