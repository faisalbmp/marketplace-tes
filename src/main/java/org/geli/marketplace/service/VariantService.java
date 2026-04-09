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
    }

    public ResponseEntity<ResponseUtil> findAll(org.springframework.data.jpa.domain.Specification<VariantModel> spec, org.springframework.data.domain.Pageable pageable) {
        ResponseUtil response = new ResponseUtil();
        org.springframework.data.domain.Page<VariantModel> result = variantDao.findAll(spec, pageable);
        response.setStatus(200);
        response.setMessage(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
