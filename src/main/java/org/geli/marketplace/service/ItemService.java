package org.geli.marketplace.service;

import org.geli.marketplace.model.ItemModel;
import org.geli.marketplace.repository.ItemRepository;
import org.geli.marketplace.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemDao;

    @Autowired
    private ActivityLogService activityLogService;

    public ResponseEntity<ResponseUtil> add(ItemModel request) {
        ResponseUtil response = new ResponseUtil();
        try {
            itemDao.save(request);
            response.setStatus(200);
            response.setMessage(request);

            activityLogService.log("ITEM_CREATE", "SUCCESS", "Created/Updated item", request.getId(), "items", request, response);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            activityLogService.log("ITEM_CREATE", "ERROR", e.getMessage(), request.getId(), "items", request, null);
            throw e;
        }
    }

    public ResponseEntity<ResponseUtil> findAll(org.springframework.data.jpa.domain.Specification<ItemModel> spec, org.springframework.data.domain.Pageable pageable) {
        ResponseUtil response = new ResponseUtil();
        org.springframework.data.domain.Page<ItemModel> result = itemDao.findAll(spec, pageable);
        response.setStatus(200);
        response.setMessage(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
