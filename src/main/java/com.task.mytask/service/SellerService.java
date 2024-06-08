package com.task.mytask.service;

import com.task.mytask.dao.SellerDAO;
import com.task.mytask.entitiy.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {
    private final SellerDAO sellerDAO;

    @Autowired
    public SellerService(SellerDAO sellerDAO) {
        this.sellerDAO = sellerDAO;
    }

    public boolean onBoardSeller(String sellerID) {
        return sellerDAO.addSeller(sellerID);
    }

    public Seller getSeller(String sellerID) {
        return sellerDAO.getSeller(sellerID);
    }
}
