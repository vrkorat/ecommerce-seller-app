package com.task.mytask.dao;

import com.task.mytask.entitiy.Product;
import com.task.mytask.entitiy.Seller;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SellerDAO {
    private final List<Seller> sellers = new ArrayList<>();

    public boolean addSeller(String sellerID) {
        if(sellers.stream().anyMatch(seller -> seller.getId().equals(sellerID))) {
            throw new IllegalArgumentException("Seller already exists");
        }
        return sellers.add(Seller.builder()
                .id(sellerID)
                .rating(1).pendingPayment(0)
                .totalItemAddedToWarehouse(0).build());
    }

    public boolean onBoardToWarehouse(String warehouseId, String sellerId) {
        Seller seller = sellers.stream().filter(s -> s.getId().equals(sellerId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));
        if(seller.onBoarded(warehouseId)) {
            throw new IllegalArgumentException("Seller already on-boarded to warehouse");
        }
        return seller.getOnBoardedToWarehouses().add(warehouseId);
    }

    public boolean exists(String sellerId) {
        return sellers.stream().anyMatch(seller -> seller.getId().equals(sellerId));
    }

    public boolean addProductToCatalog(String sellerId, Product product) {
        Seller seller = sellers.stream().filter(s -> s.getId().equals(sellerId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));
        return seller.getProducts().add(product);
    }

    public Seller getSeller(String sellerID) {
        return sellers.stream().filter(seller -> seller.getId().equals(sellerID)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));
    }
}
