package com.task.mytask.dao;

import com.task.mytask.entitiy.Seller;
import com.task.mytask.entitiy.Warehouse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WarehouseDAO {
    private final List<Warehouse> warehouses = new ArrayList<>();


    public boolean addWarehouse(Warehouse warehouse) {
        if(warehouses.stream().anyMatch(w -> w.getId().equals(warehouse.getId()))) {
            throw new IllegalArgumentException("Warehouse already exists");
        }
        return warehouses.add(warehouse);
    }

    public boolean onboardSeller(String warehouseID, String sellerID) {
        Warehouse warehouse = warehouses.stream().filter(w -> w.getId().equals(warehouseID)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
        if(warehouse.getSellers().contains(sellerID)) {
            throw new IllegalArgumentException("Seller already onboarded");
        }
        return warehouse.getSellers().add(sellerID);
    }

    public void addCapacity(String warehouseID, LocalDate date, int capacity) {
        Warehouse warehouse = warehouses.stream().filter(w -> w.getId().equals(warehouseID)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
        warehouse.getCapacities().put(date, capacity);
    }

    public Map<LocalDate, Integer> getWarehouseCapacityForNext10DaysFromGivenDate(String warehouseID, LocalDate date) {
        Warehouse warehouse = warehouses.stream().filter(w -> w.getId().equals(warehouseID)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
        return warehouse.getCapacities().entrySet().stream()
                .filter(entry -> entry.getKey().isAfter(date.minusDays(1)) && entry.getKey().isBefore(date.plusDays(10)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, LocalDate> getAllAvailableWarehousesForSellerAfterGivenDateAndInventory(Seller seller, LocalDate startDate, Integer integer) {
        if(seller.getOnBoardedToWarehouses().stream().noneMatch(warehouseID -> warehouses.stream().anyMatch(warehouse -> warehouse.getId().equals(warehouseID)))) {
            throw new IllegalArgumentException("Seller not onboarded to any warehouse");
        }
        Map<String, LocalDate> result = new HashMap<>();
        for(String warehouseID : seller.getOnBoardedToWarehouses()) {
            Warehouse warehouse = warehouses.stream().filter(w -> w.getId().equals(warehouseID)).findFirst().get();
            warehouse.getCapacities().forEach((date, v) -> {
                if(date.isAfter(startDate) && v >= integer) {
                    result.put(warehouseID, date);
                }
            });
        }
        return result;
    }

    public void reserveSlot(Seller seller, String warehouseID, LocalDate date, Integer quantity) {
        Warehouse warehouse = warehouses.stream().filter(w -> w.getId().equals(warehouseID)).findFirst()
                .filter(w -> w.getCapacities().entrySet().stream().anyMatch(entry -> entry.getKey().equals(date) && entry.getValue() >= quantity))
                .orElseThrow(() -> new IllegalArgumentException("Slot not available"));
        warehouse.getCapacities().put(date, warehouse.getCapacities().get(date) - quantity);
        seller.setTotalItemAddedToWarehouse(seller.getTotalItemAddedToWarehouse() + quantity);
        seller.setRating(1 + seller.getTotalItemAddedToWarehouse()/150);
        seller.setPendingPayment(seller.getPendingPayment() + (10 * (quantity - seller.getRating())));
    }
}
