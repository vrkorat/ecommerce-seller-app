package com.task.mytask;

import com.task.mytask.dto.ProductInventory;
import com.task.mytask.dto.SlotRequest;
import com.task.mytask.entitiy.Product;
import com.task.mytask.entitiy.Warehouse;
import com.task.mytask.service.ProductService;
import com.task.mytask.service.SellerService;
import com.task.mytask.service.WarehouseService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class MytaskApplicationTests {

    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SellerService sellerService;

    @Test
    void appTest() {
        LocalDate d1 = LocalDate.of(2021, 1, 1);
        LocalDate d2 = LocalDate.of(2021, 1, 2);
        LocalDate d3 = LocalDate.of(2021, 1, 3);
        LocalDate d4 = LocalDate.of(2021, 1, 4);
        LocalDate d5 = LocalDate.of(2021, 1, 5);
        LocalDate d6 = LocalDate.of(2021, 1, 10);
        LocalDate d7 = LocalDate.of(2021, 1, 11);

        Product p1 = new Product("p1", "product1", 100.0, "s1");
        Product p2 = new Product("p2", "product2", 200.0, "s1");
        Product p3 = new Product("p3", "product3", 300.0, "s2");
        Product p4 = new Product("p4", "product4", 400.0, "s2");

        sellerService.onBoardSeller("s1");
        sellerService.onBoardSeller("s2");

        productService.addProduct(p1, "s1");
        productService.addProduct(p2, "s1");
        productService.addProduct(p3, "s2");
        productService.addProduct(p4, "s2");

        warehouseService.addWarehouse(Warehouse.builder().id("w1")
                .capacities(new ConcurrentHashMap<>())
                .sellers(new ArrayList<>()).build());
        warehouseService.addWarehouse(Warehouse.builder().id("w2")
                .capacities(new ConcurrentHashMap<>())
                .sellers(new ArrayList<>()).build());

        warehouseService.addCapacity("w1", d1, 100);
        warehouseService.addCapacity("w1", d2, 200);
        warehouseService.addCapacity("w1", d3, 300);
//        warehouseService.addCapacity("w1", d4, 150);
//        warehouseService.addCapacity("w1", d6, 250);
//        warehouseService.addCapacity("w1", d7, 10);

        warehouseService.addCapacity("w2", d1, 100);
        warehouseService.addCapacity("w2", d2, 200);
        warehouseService.addCapacity("w2", d3, 50);
//        warehouseService.addCapacity("w2", d4, 100);
//        warehouseService.addCapacity("w2", d6, 50);
//        warehouseService.addCapacity("w2", d7, 50);

        warehouseService.onboardSeller("w1", "s1");
        warehouseService.onboardSeller("w1", "s2");
        warehouseService.onboardSeller("w2", "s1");

        System.out.println("\nGet W1 Warehouse capacity for next 10 days from given date");
        warehouseService.getWarehouseCapacityForNext10DaysFromGivenDate("w1", d1)
                .forEach((k, v) -> System.out.println(k + ", " + v));

        System.out.println("\nGet available Slots from all Warehouses for given date and seller");
        warehouseService.getSlots(SlotRequest.builder().sellerId("s2")
                .startDate(d1).productInventoryList(Arrays.asList(ProductInventory.builder().productId("p1").quantity(50).build(),
                        ProductInventory.builder().productId("p2").quantity(50).build())).build())
                .forEach((k, v) -> System.out.println(k + ", " + v));

        System.out.println("\nReserve available Slot in given Warehouse for given date and seller");
        warehouseService.reserveSlot(SlotRequest.builder().sellerId("s1")
                .startDate(d3).productInventoryList(Collections.singletonList(ProductInventory.builder().productId("p1").quantity(300).build())).build(), "w1");

        System.out.println("\nAfter Reservation Get available Slots from all Warehouses for given date and seller");
        warehouseService.getSlots(SlotRequest.builder().sellerId("s1")
                        .startDate(d3).productInventoryList(Arrays.asList(ProductInventory.builder().productId("p1").quantity(50).build(),
                                ProductInventory.builder().productId("p2").quantity(50).build())).build())
                .forEach((k, v) -> System.out.println(k + ", " + v));


        System.out.println("\nReserve available Slot in given Warehouse for given date and seller");
        warehouseService.reserveSlot(SlotRequest.builder().sellerId("s1")
                .startDate(d3).productInventoryList(Collections.singletonList(ProductInventory.builder().productId("p1").quantity(100).build())).build(), "w1");
        System.out.println("\nReserve available Slot in given Warehouse for given date and seller");
        warehouseService.reserveSlot(SlotRequest.builder().sellerId("s1")
                .startDate(d1).productInventoryList(Collections.singletonList(ProductInventory.builder().productId("p1").quantity(50).build())).build(), "w1");
        System.out.println("\nReserve available Slot in given Warehouse for given date and seller");
        warehouseService.reserveSlot(SlotRequest.builder().sellerId("s1")
                .startDate(d2).productInventoryList(Collections.singletonList(ProductInventory.builder().productId("p1").quantity(100).build())).build(), "w1");

        System.out.println("\nAfter reservation Get W1 Warehouse capacity for next 10 days from given date");
        warehouseService.getWarehouseCapacityForNext10DaysFromGivenDate("w1", d1)
                .forEach((k, v) -> System.out.println(k + ", " + v));

        System.out.println("\nStatus of each seller after reservation");
        System.out.println(sellerService.getSeller("s1"));
        System.out.println(sellerService.getSeller("s2"));

    }

}
