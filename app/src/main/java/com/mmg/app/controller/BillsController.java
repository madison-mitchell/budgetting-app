package com.mmg.app.controller;

import com.mmg.app.model.Bills;
import com.mmg.app.service.BillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/bills")
public class BillsController {

    @Autowired
    private BillsService billsService;

    @PostMapping
    public Bills createBill(@RequestBody Bills bill) {
        return billsService.saveBill(bill);
    }

    @PutMapping("/{id}")
    public Bills updateBill(@PathVariable Long id, @RequestBody Bills bill) {
        bill.setId(id);
        return billsService.updateBill(bill);
    }

    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable Long id) {
        billsService.deleteBill(id);
    }

    @GetMapping("/{id}")
    public Bills getBillById(@PathVariable Long id) {
        return billsService.getBillById(id);
    }

    @GetMapping
    public List<Bills> getAllBills() {
        return billsService.getAllBills();
    }
}
