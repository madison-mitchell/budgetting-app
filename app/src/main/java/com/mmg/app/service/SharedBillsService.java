package com.mmg.app.service;

import com.mmg.app.model.SharedBills;

import java.util.List;

public interface SharedBillsService {
    SharedBills saveSharedBill(SharedBills sharedBill);
    SharedBills updateSharedBill(SharedBills sharedBill);
    void deleteSharedBill(Long id);
    SharedBills getSharedBillById(Long id);
    List<SharedBills> getAllSharedBills();
}
