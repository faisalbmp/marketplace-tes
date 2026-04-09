package org.geli.marketplace.controller;

import org.geli.marketplace.model.InventoryModel;
import org.geli.marketplace.service.InventoryService;
import org.geli.marketplace.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("inventory/api/")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("addStock")
    public ResponseEntity<ResponseUtil> addStock(@RequestParam Long variantId, @RequestParam int quantity) {
        return inventoryService.addStock(variantId, quantity);
    }

    @PostMapping("add")
    public ResponseEntity<ResponseUtil> add(@RequestBody InventoryModel request) {
        return inventoryService.add(request);
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseUtil> findAll() {
        return inventoryService.findAll();
    }
}
