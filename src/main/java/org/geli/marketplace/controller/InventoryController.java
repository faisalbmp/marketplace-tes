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

    @GetMapping("check/{variantId}")
    public ResponseEntity<ResponseUtil> checkStock(@PathVariable Long variantId) {
        return inventoryService.checkStock(variantId);
    }

    @PostMapping("addStock")
    public ResponseEntity<ResponseUtil> addStock(@RequestParam Long variantId, @RequestParam int quantity) {
        return inventoryService.addStock(variantId, quantity);
    }

    @PostMapping("add")
    public ResponseEntity<ResponseUtil> add(@RequestBody InventoryModel request) {
        return inventoryService.add(request);
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseUtil> findAll(
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startModDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endModDate,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock,
            @RequestParam(required = false) java.lang.String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        
        org.springframework.data.domain.Sort sorting = org.springframework.data.domain.Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            String sortOrder = sortParams.length > 1 ? sortParams[1] : "asc";
            if ("stock".equalsIgnoreCase(sortField)) sortField = "quantity";
            sorting = sortOrder.equalsIgnoreCase("asc") ? 
                    org.springframework.data.domain.Sort.by(sortField).ascending() : 
                    org.springframework.data.domain.Sort.by(sortField).descending();
        }
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sorting);
        org.springframework.data.jpa.domain.Specification<org.geli.marketplace.model.InventoryModel> spec = 
            org.geli.marketplace.specification.InventorySpecification.filter(
                startDate, endDate, startModDate, endModDate, minStock, maxStock
            );

        return inventoryService.findAll(spec, pageable);
    }
}
