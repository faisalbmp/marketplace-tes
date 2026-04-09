package org.geli.marketplace.service;

import org.geli.marketplace.model.InventoryModel;
import org.geli.marketplace.model.VariantModel;
import org.geli.marketplace.repository.InventoryRepository;
import org.geli.marketplace.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryDao;

    public ResponseEntity<ResponseUtil> checkStock(Long variantId) {
        ResponseUtil response = new ResponseUtil();
        org.geli.marketplace.model.InventoryModel inventory = inventoryDao.findByVariantId(variantId)
                .orElseThrow(() -> new java.util.NoSuchElementException("Inventory not found for Variant " + variantId));
                
        response.setStatus(200);
        response.setMessage(java.util.Map.of("variantId", variantId, "stock", inventory.getQuantity()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResponseUtil> addStock(Long variantId, int quantityToAdd) {
        ResponseUtil response = new ResponseUtil();
        org.geli.marketplace.model.InventoryModel inventory = inventoryDao.findByVariantId(variantId).orElse(null);
        
        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + quantityToAdd);
        } else {
            inventory = new org.geli.marketplace.model.InventoryModel();
            org.geli.marketplace.model.VariantModel variant = new org.geli.marketplace.model.VariantModel();
            variant.setId(variantId);
            inventory.setVariant(variant);
            inventory.setQuantity(quantityToAdd);
        }
        
        inventoryDao.save(inventory);
        response.setStatus(200);
        response.setMessage(inventory);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResponseUtil> add(InventoryModel request) {
        ResponseUtil response = new ResponseUtil();
        if (request.getId() != null) {
            request.setModifiedDate(LocalDateTime.now());
            request.setModifiedBy("system");
        }
        inventoryDao.save(request);
        response.setStatus(200);
        response.setMessage(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResponseUtil> findAll(org.springframework.data.jpa.domain.Specification<InventoryModel> spec, org.springframework.data.domain.Pageable pageable) {
        ResponseUtil response = new ResponseUtil();
        org.springframework.data.domain.Page<InventoryModel> result = inventoryDao.findAll(spec, pageable);
        response.setStatus(200);
        response.setMessage(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
