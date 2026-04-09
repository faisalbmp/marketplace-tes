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
    public ResponseEntity<ResponseUtil> findAll() {
        return itemService.findAll();
    }
}
