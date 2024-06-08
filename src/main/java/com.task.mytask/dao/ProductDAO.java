package com.task.mytask.dao;

import com.task.mytask.entitiy.Product;
import com.task.mytask.entitiy.Seller;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDAO {
    private final List<Product> products = new ArrayList<>();

    public boolean addProduct(Product product) {
        if(products.stream().anyMatch(p -> p.getId().equals(product.getId()))) {
            throw new IllegalArgumentException("Product already exists");
        }
        return products.add(product);
    }
}
