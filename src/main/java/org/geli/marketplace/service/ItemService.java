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

    public ResponseEntity<ResponseUtil> add(ItemModel request) {
        ResponseUtil response = new ResponseUtil();
        try {
            itemDao.save(request);
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
            List<ItemModel> result = itemDao.findAll();
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
