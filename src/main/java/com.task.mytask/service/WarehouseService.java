package com.task.mytask.service;

import com.task.mytask.dao.SellerDAO;
import com.task.mytask.dao.WarehouseDAO;
import com.task.mytask.dto.SlotRequest;
import com.task.mytask.entitiy.Product;
import com.task.mytask.entitiy.Seller;
import com.task.mytask.entitiy.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    private final WarehouseDAO warehouseDAO;
    private final SellerDAO sellerDAO;

    @Autowired
    public WarehouseService(WarehouseDAO warehouseDAO, SellerDAO sellerDAO) {
        this.warehouseDAO = warehouseDAO;
        this.sellerDAO = sellerDAO;
    }

    public boolean addWarehouse(Warehouse warehouse) {
        return warehouseDAO.addWarehouse(warehouse);
    }

    public boolean onboardSeller(String warehouseID, String sellerID) {
        warehouseDAO.onboardSeller(warehouseID, sellerID);
        return sellerDAO.onBoardToWarehouse(warehouseID, sellerID);
    }

    public void addCapacity(String warehouseID, LocalDate date, int capacity) {
        warehouseDAO.addCapacity(warehouseID, date, capacity);
    }

    public Map<LocalDate, Integer> getWarehouseCapacityForNext10DaysFromGivenDate(String warehouseID, LocalDate date) {
        return warehouseDAO.getWarehouseCapacityForNext10DaysFromGivenDate(warehouseID, date);
    }

    public Map<String, LocalDate> getSlots(SlotRequest slotRequest) {
        Seller seller = sellerDAO.getSeller(slotRequest.getSellerId());
        AtomicReference<Integer> totalItems = new AtomicReference<>(0);
        slotRequest.getProductInventoryList().forEach(productInventory -> totalItems.getAndUpdate(v -> v + productInventory.getQuantity()));
        return warehouseDAO.getAllAvailableWarehousesForSellerAfterGivenDateAndInventory(seller, slotRequest.getStartDate(), totalItems.get());
    }

    public void reserveSlot(SlotRequest slotRequest, String warehouseID) {
        Seller seller = sellerDAO.getSeller(slotRequest.getSellerId());
        if(!seller.onBoarded(warehouseID)) {
            throw new IllegalArgumentException("Seller not onboarded to warehouse");
        }
        validateSeller(slotRequest, seller);
        AtomicReference<Integer> totalItems = new AtomicReference<>(0);
        slotRequest.getProductInventoryList().forEach(productInventory -> totalItems.getAndUpdate(v -> v + productInventory.getQuantity()));
        warehouseDAO.reserveSlot(seller, warehouseID, slotRequest.getStartDate(), totalItems.get());
    }

    private void validateSeller(SlotRequest slotRequest, Seller seller) {
        slotRequest.getProductInventoryList().forEach(productInventory -> {
            if(!seller.getProducts().stream().map(Product::getId).collect(Collectors.toList()).contains(productInventory.getProductId()))
                throw new IllegalArgumentException("Seller does not have given products in inventory");
        });
    }
}
