package com.task.mytask.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductInventory {
    private String productId;
    private Integer quantity;
}
