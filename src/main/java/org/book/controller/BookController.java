package org.book.controller;

import org.book.model.BookDetailsModel;
import org.book.service.BookService;
import org.book.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("book/api/")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("add")
    public ResponseEntity<ResponseUtil> bookAdd(@RequestBody BookDetailsModel request) {
        try {
            return bookService.bookAdd(request);
        } catch (Exception e) {
            ResponseUtil response = new ResponseUtil();
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("find")
    public ResponseEntity<ResponseUtil> bookFind(@RequestBody BookDetailsModel request) {
        try {
            return bookService.bookFind(request);
        } catch (Exception e) {
            ResponseUtil response = new ResponseUtil();
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseUtil> bookFindAll() {
        try {
            return bookService.bookFindAll();
        } catch (Exception e) {
            ResponseUtil response = new ResponseUtil();
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("update")
    public ResponseEntity<ResponseUtil> bookUpdate(@RequestBody BookDetailsModel request) {
        try {
            return bookService.bookUpdate(request);
        } catch (Exception e) {
            ResponseUtil response = new ResponseUtil();
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
