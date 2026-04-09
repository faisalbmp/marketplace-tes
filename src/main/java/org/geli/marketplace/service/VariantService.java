package org.geli.marketplace.service;

import org.geli.marketplace.model.VariantModel;
import org.geli.marketplace.repository.VariantRepository;
import org.geli.marketplace.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VariantService {

    @Autowired
    private VariantRepository variantDao;

    public ResponseEntity<ResponseUtil> add(VariantModel request) {
        ResponseUtil response = new ResponseUtil();
        try {

            if (request.getItem() != null && request.getItem().getId() != null) {
                VariantModel existingItem = variantDao.findById(request.getItem().getId()).orElse(null);
                if (existingItem != null) {
                    request.setItem(existingItem.getItem());
                }
            }


            variantDao.save(request);
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
            List<VariantModel> result = variantDao.findAll();
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
