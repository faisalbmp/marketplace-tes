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

    // Transactional is crucial here so the lock check and save happen together
    @Transactional
    public ResponseEntity<ResponseUtil> sellVariant(Long variantId, int quantityToSell) {
        ResponseUtil response = new ResponseUtil();

        try {
            if (quantityToSell <= 0) {
                response.setStatus(400);
                response.setMessage("Quantity must be greater than zero.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 1. Fetch current inventory
            InventoryModel inventory = inventoryDao.findByVariantId(variantId)
                    .orElseThrow(() -> new RuntimeException("Inventory not found for this variant"));

            // 2. Check if we have enough stock
            if (inventory.getQuantity() < quantityToSell) {
                response.setStatus(400);
                response.setMessage("Insufficient stock! Only " + inventory.getQuantity() + " left.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 3. Deduct stock and save
            inventory.setQuantity(inventory.getQuantity() - quantityToSell);
            inventoryDao.save(inventory);

            response.setStatus(200);
            response.setMessage("Successfully sold " + quantityToSell + " items.");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (OptimisticLockingFailureException e) {
            // 4. CONCURRENCY PROTECTION: Caught a race condition!
            response.setStatus(409); // 409 Conflict is the standard HTTP code for this
            response.setMessage("The inventory was updated by another transaction. Please refresh and try again.");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);

        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Internal Server Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}