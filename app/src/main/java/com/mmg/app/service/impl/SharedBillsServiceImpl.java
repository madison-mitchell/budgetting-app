package com.mmg.app.service.impl;

import com.mmg.app.model.SharedBills;
import com.mmg.app.repository.SharedBillsRepository;
import com.mmg.app.service.SharedBillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SharedBillsServiceImpl implements SharedBillsService {

    @Autowired
    private SharedBillsRepository sharedBillsRepository;

    @Override
    public SharedBills saveSharedBill(SharedBills sharedBill) {
        return sharedBillsRepository.save(sharedBill);
    }

    @Override
    public SharedBills updateSharedBill(SharedBills sharedBill) {
        return sharedBillsRepository.save(sharedBill);
    }

    @Override
    public void deleteSharedBill(Long id) {
        sharedBillsRepository.deleteById(id);
    }

    @Override
    public SharedBills getSharedBillById(Long id) {
        return sharedBillsRepository.findById(id).orElse(null);
    }

    @Override
    public List<SharedBills> getAllSharedBills() {
        return sharedBillsRepository.findAll();
    }
}
