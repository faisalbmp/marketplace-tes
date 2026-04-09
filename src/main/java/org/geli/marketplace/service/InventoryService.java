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

    public ResponseEntity<ResponseUtil> addStock(Long variantId, int quantityToAdd) {
        ResponseUtil response = new ResponseUtil();
        try {
            InventoryModel inventory = inventoryDao.findByVariantId(variantId).orElse(null);
            
            if (inventory != null) {
                inventory.setQuantity(inventory.getQuantity() + quantityToAdd);
            } else {
                inventory = new InventoryModel();
                VariantModel variant = new VariantModel();
                variant.setId(variantId);
                inventory.setVariant(variant);
                inventory.setQuantity(quantityToAdd);
            }
            
            inventoryDao.save(inventory);
            response.setStatus(200);
            response.setMessage(inventory);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseUtil> add(InventoryModel request) {
        ResponseUtil response = new ResponseUtil();
        try {

            // If request.id exists (update case), set created_date to current time
            if (request.getId() != null) {
                request.setModifiedDate(LocalDateTime.now());
                request.setModifiedBy("system"); // You can replace "system" with actual user info if available
            }
            inventoryDao.save(request);
            response.setStatus(200);
            response.setMessage(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseUtil> findAll() {
        ResponseUtil response = new ResponseUtil();
        try {
            List<InventoryModel> result = inventoryDao.findAll();
            response.setStatus(200);
            response.setMessage(result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
