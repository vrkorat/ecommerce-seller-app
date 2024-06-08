package com.task.mytask.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class SlotRequest {
    String sellerId;
    List<ProductInventory> productInventoryList;
    LocalDate startDate;
}
