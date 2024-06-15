package com.mmg.app.service.impl;

import com.mmg.app.model.Bills;
import com.mmg.app.repository.BillsRepository;
import com.mmg.app.service.BillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillsServiceImpl implements BillsService {

    @Autowired
    private BillsRepository billsRepository;

    @Override
    public Bills saveBill(Bills bill) {
        return billsRepository.save(bill);
    }

    @Override
    public Bills updateBill(Bills bill) {
        return billsRepository.save(bill);
    }

    @Override
    public void deleteBill(Long id) {
        billsRepository.deleteById(id);
    }

    @Override
    public Bills getBillById(Long id) {
        return billsRepository.findById(id).orElse(null);
    }

    @Override
    public List<Bills> getAllBills() {
        return billsRepository.findAll();
    }
}
