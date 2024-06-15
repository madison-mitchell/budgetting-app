package com.mmg.app.controller;

import com.mmg.app.model.SharedBills;
import com.mmg.app.service.SharedBillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/shared-bills")
public class SharedBillsController {

    @Autowired
    private SharedBillsService sharedBillsService;

    @PostMapping
    public SharedBills createSharedBill(@RequestBody SharedBills sharedBill) {
        return sharedBillsService.saveSharedBill(sharedBill);
    }

    @PutMapping("/{id}")
    public SharedBills updateSharedBill(@PathVariable Long id, @RequestBody SharedBills sharedBill) {
        sharedBill.setId(id);
        return sharedBillsService.updateSharedBill(sharedBill);
    }

    @DeleteMapping("/{id}")
    public void deleteSharedBill(@PathVariable Long id) {
        sharedBillsService.deleteSharedBill(id);
    }

    @GetMapping("/{id}")
    public SharedBills getSharedBillById(@PathVariable Long id) {
        return sharedBillsService.getSharedBillById(id);
    }

    @GetMapping
    public List<SharedBills> getAllSharedBills() {
        return sharedBillsService.getAllSharedBills();
    }
}
