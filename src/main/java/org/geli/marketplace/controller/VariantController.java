package org.geli.marketplace.controller;

import org.geli.marketplace.model.VariantModel;
import org.geli.marketplace.service.VariantService;
import org.geli.marketplace.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("variant/api/")
public class VariantController {

    @Autowired
    private VariantService variantService;

    @PostMapping("add")
    public ResponseEntity<ResponseUtil> add(@RequestBody VariantModel request) {
        return variantService.add(request);
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseUtil> findAll() {
        return variantService.findAll();
    }
}
