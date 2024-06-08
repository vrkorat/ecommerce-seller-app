package com.task.mytask.entitiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Product {
    private final String id;
    private final String name;
    private double price;
    private final String sellerId;

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", \n name='" + name + '\'' +
                ", \n price=" + price +
                ", \n sellerId='" + sellerId + '\'' +
                "} \n ";
    }
}
