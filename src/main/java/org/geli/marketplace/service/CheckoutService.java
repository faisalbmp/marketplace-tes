package org.geli.marketplace.service;

import org.geli.marketplace.repository.InventoryRepository;
import org.geli.marketplace.model.InventoryModel;
import org.geli.marketplace.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckoutService {

    @Autowired
    private InventoryRepository inventoryDao;

    @Autowired
    private ActivityLogService activityLogService;

    // Transactional is crucial here so the lock check and save happen together
    @Transactional
    public ResponseEntity<ResponseUtil> sellVariant(Long variantId, int quantityToSell) {
        ResponseUtil response = new ResponseUtil();
        java.util.Map<String, Object> requestInputs = java.util.Map.of(
            "variantId", variantId,
            "quantityToSell", quantityToSell
        );

        try {
            if (quantityToSell <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }

            // 1. Fetch current inventory
            InventoryModel inventory = inventoryDao.findByVariantId(variantId)
                    .orElseThrow(() -> new java.util.NoSuchElementException("Inventory not found for this variant"));

            // 2. Check if we have enough stock
            if (inventory.getQuantity() < quantityToSell) {
                throw new IllegalArgumentException("Insufficient stock! Only " + inventory.getQuantity() + " left.");
            }

            // 3. Deduct stock and save
            inventory.setQuantity(inventory.getQuantity() - quantityToSell);
            inventoryDao.save(inventory);

            response.setStatus(200);
            response.setMessage("Successfully sold " + quantityToSell + " items.");
            
            activityLogService.log("CHECKOUT", "SUCCESS", "Sold items", variantId, "inventory", requestInputs, response);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            activityLogService.log("CHECKOUT", "ERROR", e.getMessage(), variantId, "inventory", requestInputs, null);
            throw e; // Re-throw to let GlobalExceptionHandler handle the response
        }
    }
}