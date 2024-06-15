package com.mmg.app.service;

import com.mmg.app.model.Bills;

import java.util.List;

public interface BillsService {
    Bills saveBill(Bills bill);
    Bills updateBill(Bills bill);
    void deleteBill(Long id);
    Bills getBillById(Long id);
    List<Bills> getAllBills();
}
