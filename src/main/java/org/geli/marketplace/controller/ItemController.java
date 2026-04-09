package org.geli.marketplace.controller;

import org.geli.marketplace.model.ItemModel;
import org.geli.marketplace.service.ItemService;
import org.geli.marketplace.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("item/api/")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("add")
    public ResponseEntity<ResponseUtil> add(@RequestBody ItemModel request) {
        return itemService.add(request);
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseUtil> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startModDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endModDate,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(required = false) Long minStock,
            @RequestParam(required = false) Long maxStock,
            java.lang.String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        
        // Handle sorting logic for mapping "price" to "totalStock"
        org.springframework.data.domain.Sort sorting = org.springframework.data.domain.Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            String sortOrder = sortParams.length > 1 ? sortParams[1] : "asc";
            
            if ("price".equalsIgnoreCase(sortField)) {
                sortField = "totalStock"; // User request: sort price maps to total stock
            }
            
            sorting = sortOrder.equalsIgnoreCase("asc") ? 
                    org.springframework.data.domain.Sort.by(sortField).ascending() : 
                    org.springframework.data.domain.Sort.by(sortField).descending();
        }
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sorting);
        org.springframework.data.jpa.domain.Specification<org.geli.marketplace.model.ItemModel> spec = 
            org.geli.marketplace.specification.ItemSpecification.filter(
                search, startDate, endDate, startModDate, endModDate, minPrice, maxPrice, minStock, maxStock
            );

        return itemService.findAll(spec, pageable);
    }
}
