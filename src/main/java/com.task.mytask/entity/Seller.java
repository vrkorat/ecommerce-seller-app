package com.task.mytask.entitiy;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class Seller {
    private final String id;
    private final List<String> onBoardedToWarehouses = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();
    @Setter
    private Integer rating;
    @Setter
    private Integer pendingPayment;
    @Setter
    private Integer totalItemAddedToWarehouse;
    public boolean onBoarded(String warehouseId) {
        return onBoardedToWarehouses.contains(warehouseId);
    }

    @Override
    public String toString() {
        return "Seller{" +
                "id='" + id + '\'' +
                ", \n onBoardedToWarehouses=" + onBoardedToWarehouses +
                ", \n products=" + products +
                ", \n rating=" + rating +
                ", \n pendingPayment=" + pendingPayment +
                "} \n ";
    }
}
