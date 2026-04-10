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

    @Autowired
    private ActivityLogService activityLogService;

    public ResponseEntity<ResponseUtil> checkStock(Long variantId) {
        ResponseUtil response = new ResponseUtil();
        try {
            org.geli.marketplace.model.InventoryModel inventory = inventoryDao.findByVariantId(variantId)
                    .orElseThrow(() -> new java.util.NoSuchElementException("Inventory not found for Variant " + variantId));

            response.setStatus(200);
            response.setMessage(java.util.Map.of("variantId", variantId, "stock", inventory.getQuantity()));

            activityLogService.log("STOCK_CHECK", "SUCCESS", "Checked stock", variantId, "inventory", variantId, response);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            activityLogService.log("STOCK_CHECK", "ERROR", e.getMessage(), variantId, "inventory", variantId, null);
            throw e;
        }
    }

    public ResponseEntity<ResponseUtil> addStock(Long variantId, int quantityToAdd) {
        ResponseUtil response = new ResponseUtil();
        java.util.Map<String, Object> requestInputs = java.util.Map.of(
                "variantId", variantId,
                "quantityToAdd", quantityToAdd
        );

        try {
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

            activityLogService.log("STOCK_UPDATE", "SUCCESS", "Updated stock quantity", variantId, "inventory", requestInputs, response);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            activityLogService.log("STOCK_UPDATE", "ERROR", e.getMessage(), variantId, "inventory", requestInputs, null);
            throw e;
        }
    }

    public ResponseEntity<ResponseUtil> add(InventoryModel request) {
        ResponseUtil response = new ResponseUtil();
        try {
            if (request.getId() != null) {
                request.setModifiedDate(LocalDateTime.now());
                request.setModifiedBy("system");
            }
            inventoryDao.save(request);
            response.setStatus(200);
            response.setMessage(request);

            activityLogService.log("INVENTORY_SAVE", "SUCCESS", "Saved inventory record", request.getId(), "inventory", request, response);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            activityLogService.log("INVENTORY_SAVE", "ERROR", e.getMessage(), request.getId(), "inventory", request, null);
            throw e;
        }
    }

    public ResponseEntity<ResponseUtil> findAll(org.springframework.data.jpa.domain.Specification<InventoryModel> spec, org.springframework.data.domain.Pageable pageable) {
        ResponseUtil response = new ResponseUtil();
        org.springframework.data.domain.Page<InventoryModel> result = inventoryDao.findAll(spec, pageable);
        response.setStatus(200);
        response.setMessage(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
